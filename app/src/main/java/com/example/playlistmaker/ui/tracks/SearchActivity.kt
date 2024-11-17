package com.example.playlistmaker.ui.tracks

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.App
import com.example.playlistmaker.Creator
import com.example.playlistmaker.ui.main.MainActivity
import com.example.playlistmaker.ui.player.PlayerActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.data.SearchHistoryImpl
import com.example.playlistmaker.domain.Consumer
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.models.TrackData
import com.example.playlistmaker.domain.models.TracksConsumer
import com.example.playlistmaker.presentation.TracksAdapter
import com.google.gson.Gson

class SearchActivity : AppCompatActivity() {
    companion object {
        private const val INPUT_SEARCH = "INPUT_SEARCH"
        const val TRACK = "TRACK"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    enum class SearchState {
        CONNECTION_ERROR, NOT_FOUND, SUCCESS, HISTORY_LIST, SEARCH_PROGRESS
    }

    private val getHistoryTrackListToStorageUseCase by lazy {
        Creator.getSearchHistoryStorage(getSharedPreferences(App.SETTINGS, MODE_PRIVATE))
    }
    private val addHistoryTrackListFromStorageUseCase by lazy {
        Creator.addSearchHistoryStorage(getSharedPreferences(App.SETTINGS, MODE_PRIVATE))
    }
    private val clearHistoryTrackListFromStorageUseCase by lazy {
        Creator.clearSearchHistoryStorage(getSharedPreferences(App.SETTINGS, MODE_PRIVATE))
    }

    private val searchTrackList = mutableListOf<TrackData>()
    private var searchInput: String = ""

    private val trackAdapter = TracksAdapter { clickOnTrack(it) }
    private val searchHistoryAdapter = TracksAdapter { clickOnTrack(it) }
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchTrackList() }

    private lateinit var inputEditText: EditText
    private lateinit var trackRecycleView: RecyclerView
    private lateinit var searchNotification: LinearLayout
    private lateinit var errorImage: ImageView
    private lateinit var errorText: TextView
    private lateinit var errorConnectionText: TextView
    private lateinit var refreshButton: Button
    private lateinit var clearSearchButton: Button
    private lateinit var historyText: TextView
    private lateinit var recycleContainer: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var arrowBack: ImageView
    private lateinit var clearButton: ImageView
    private lateinit var searchTracksUseCase: TracksInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // RecycleView
        init()
        trackRecycleView.layoutManager = LinearLayoutManager(this)
        trackRecycleView.adapter = trackAdapter

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTrackList()
                true
            }
            false
        }

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (getHistoryTrackListToStorageUseCase.execute().isNotEmpty() && hasFocus)
                showResultNotification(SearchState.HISTORY_LIST)
        }

        arrowBack.setOnClickListener {
            val mainDisplay = Intent(this, MainActivity::class.java)
            startActivity(mainDisplay)
        }

        clearButton.setOnClickListener {
            clearSearchForm()
        }

        refreshButton.setOnClickListener {
            searchTrackList()
        }

        clearSearchButton.setOnClickListener {
            clearHistoryTrackListFromStorageUseCase.execute()
            showResultNotification(SearchState.SUCCESS)
        }

        if (savedInstanceState != null) {
            inputEditText.setText(INPUT_SEARCH)
        }

        inputEditText.requestFocus()

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                searchInput = s.toString()
                if (inputEditText.hasFocus()
                    && s.isNullOrEmpty()
                    && getHistoryTrackListToStorageUseCase.execute().isNotEmpty()
                ) {
                    showResultNotification(SearchState.HISTORY_LIST)
                } else {
                    searchDebounce()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        inputEditText.addTextChangedListener(simpleTextWatcher)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_SEARCH, searchInput)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchInput = savedInstanceState.getString(INPUT_SEARCH, "")
    }

    private fun clearButtonVisibility(inputString: CharSequence?): Int {
        return if (inputString.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun clearSearchForm() {
        inputEditText.setText("")
        searchTrackList.clear()
        trackAdapter.notifyDataSetChanged()

        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun showResultNotification(resultType: SearchState) {
        when (resultType) {
            SearchState.SUCCESS -> {
                trackAdapter.tracks = searchTrackList
                trackRecycleView.adapter = trackAdapter
                trackRecycleView.visibility = View.VISIBLE
                searchNotification.visibility = View.GONE
                refreshButton.visibility = View.GONE
                historyText.visibility = View.GONE
                clearSearchButton.visibility = View.GONE
                progressBar.visibility = View.GONE
            }

            SearchState.NOT_FOUND -> {
                trackRecycleView.visibility = View.GONE
                historyText.visibility = View.GONE
                clearSearchButton.visibility = View.GONE
                recycleContainer.visibility = View.GONE
                searchNotification.visibility = View.VISIBLE
                errorText.setText(R.string.not_found_error)
                errorConnectionText.visibility = View.GONE
                refreshButton.visibility = View.GONE
                progressBar.visibility = View.GONE
                errorImage.setImageResource(
                    if (isDarkModeOn()) R.drawable.ic_not_found_dark else R.drawable.ic_not_found_light
                )
            }

            SearchState.SEARCH_PROGRESS -> {
                recycleContainer.visibility = View.VISIBLE
                trackRecycleView.visibility = View.GONE
                historyText.visibility = View.GONE
                clearSearchButton.visibility = View.GONE
                historyText.visibility = View.GONE
                clearSearchButton.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }

            SearchState.CONNECTION_ERROR -> {
                historyText.visibility = View.GONE
                clearSearchButton.visibility = View.GONE
                trackRecycleView.visibility = View.GONE
                recycleContainer.visibility = View.GONE
                searchNotification.visibility = View.VISIBLE
                errorText.setText(R.string.error_connection_subtitle)
                errorConnectionText.visibility = View.VISIBLE
                refreshButton.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                errorImage.setImageResource(
                    if (isDarkModeOn()) R.drawable.ic_connection_err_dark else R.drawable.ic_connection_err_light
                )
            }

            SearchState.HISTORY_LIST -> {
                searchHistoryAdapter.tracks = getHistoryTrackListToStorageUseCase.execute()
                trackRecycleView.adapter = searchHistoryAdapter
                trackRecycleView.visibility = View.VISIBLE
                searchNotification.visibility = View.GONE
                refreshButton.visibility = View.GONE
                historyText.visibility = View.VISIBLE
                clearSearchButton.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun isDarkModeOn(): Boolean {
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isDarkModeOn = nightModeFlags == Configuration.UI_MODE_NIGHT_YES
        return isDarkModeOn
    }

    private fun searchTrackList() {
        if (inputEditText.text.isNotEmpty()) {
            showResultNotification(SearchState.SEARCH_PROGRESS)
            searchTracksUseCase.searchTracks(
                inputEditText.text.toString(),
                consumer = object : Consumer<List<TrackData>> {
                    override fun consume(data: TracksConsumer<List<TrackData>>) {
                        handler.post {
                            when (data) {
                                is TracksConsumer.Data -> {
                                    searchTrackList.clear()
                                    searchTrackList.addAll(data.value)
                                    trackAdapter.notifyDataSetChanged()
                                    showResultNotification(SearchState.SUCCESS)
                                }

                                is TracksConsumer.EmptyData -> {
                                    searchTrackList.clear()
                                    trackAdapter.notifyDataSetChanged()
                                    showResultNotification(SearchState.NOT_FOUND)
                                }

                                is TracksConsumer.Error -> {
                                    searchTrackList.clear()
                                    trackAdapter.notifyDataSetChanged()
                                    showResultNotification(SearchState.CONNECTION_ERROR)
                                }
                            }
                        }
                    }
                }
            )
        }
    }

    private fun clickOnTrack(track: TrackData) {
        if (clickDebounce()) {
            addHistoryTrackListFromStorageUseCase.execute(track)
            val playerIntent = Intent(this, PlayerActivity::class.java)
            playerIntent.putExtra(TRACK, Gson().toJson(track))
            startActivity(playerIntent)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun init() {
        trackRecycleView = findViewById(R.id.trackSearchRecyclerView)
        arrowBack = findViewById(R.id.arrowBack)
        clearButton = findViewById(R.id.clearIcon)
        searchNotification = findViewById(R.id.searchNotification)
        refreshButton = findViewById(R.id.refreshButton)
        inputEditText = findViewById(R.id.textSearch)
        recycleContainer = findViewById(R.id.recycleContainer)
        progressBar = findViewById(R.id.progressBar)

        errorImage = findViewById(R.id.notFound)
        errorText = findViewById(R.id.errorText)
        errorConnectionText = findViewById(R.id.errorConnectionText)

        historyText = findViewById(R.id.historySearch)
        clearSearchButton = findViewById(R.id.clearSearchButton)
        searchTracksUseCase = Creator.provideSearchTrackInteractor()
    }
}