package com.example.playlistmaker

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    companion object {
        private const val INPUT_SEARCH = "INPUT_SEARCH"
    }

    enum class SearchState {
        CONNECTION_ERROR, NOT_FOUND, SUCCESS, HISTORY_LIST
    }

    private val searchTrackList = mutableListOf<TrackData>()
    private var searchInput: String = ""

    private val trackAdapter = TracksAdapter{clickOnTrack(it)}
    private val searchHistoryAdapter = TracksAdapter{clickOnTrack(it)}

    private lateinit var inputEditText: EditText
    private val searchHistory: SearchHistory by lazy {
        SearchHistory(getSharedPreferences(App.SETTINGS, MODE_PRIVATE))
    }
    private lateinit var trackRecycleView : RecyclerView
    private lateinit var searchNotification : LinearLayout
    private lateinit var errorImage : ImageView
    private lateinit var errorText : TextView
    private lateinit var errorConnectionText : TextView
    private lateinit var refreshButton : Button
    private lateinit var clearSearchButton : Button
    private lateinit var historyText : TextView

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
        trackRecycleView = findViewById(R.id.trackSearchRecyclerView)
        trackRecycleView.layoutManager = LinearLayoutManager(this)
        trackRecycleView.adapter = trackAdapter

        val arrowBack = findViewById<ImageView>(R.id.arrow_back)
        val linearLayout = findViewById<FrameLayout>(R.id.container)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        searchNotification = findViewById(R.id.searchNotification)
        refreshButton = findViewById(R.id.refreshButton)
        inputEditText = findViewById(R.id.textSearch)

        errorImage = findViewById(R.id.notFound)
        errorText = findViewById(R.id.errorText)
        errorConnectionText = findViewById(R.id.errorConnectionText)

        // SharedPreference
        historyText = findViewById(R.id.historySearch)
        clearSearchButton = findViewById(R.id.clearSearchButton)

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTrackList()
                true
            }
            false
        }

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if(searchHistory.getTrackList().isNotEmpty() && hasFocus)
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
            searchHistory.clearTrackList()
            showResultNotification(SearchState.SUCCESS)
        }

        if(savedInstanceState != null) {
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
                if(inputEditText.hasFocus()
                    && s.isNullOrEmpty()
                    && searchHistory.getTrackList().isNotEmpty()) {
                    showResultNotification(SearchState.HISTORY_LIST)
                } else {
                    showResultNotification(SearchState.SUCCESS)
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
        return if(inputString.isNullOrEmpty()) {
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
        when(resultType) {
            SearchState.SUCCESS -> {
                trackAdapter.tracks = searchTrackList
                trackRecycleView.adapter = trackAdapter
                trackRecycleView.visibility = View.VISIBLE
                searchNotification.visibility = View.GONE
                refreshButton.visibility = View.GONE
                historyText.visibility = View.GONE
                clearSearchButton.visibility = View.GONE
            }
            SearchState.NOT_FOUND -> {
                historyText.visibility = View.GONE
                clearSearchButton.visibility = View.GONE
                trackRecycleView.visibility = View.GONE
                searchNotification.visibility = View.VISIBLE
                errorText.setText(R.string.not_found_error)
                errorConnectionText.visibility = View.GONE
                refreshButton.visibility = View.GONE
                errorImage.setImageResource(
                    if (isDarkModeOn()) R.drawable.ic_not_found_dark else R.drawable.ic_not_found_light
                )
            }
            SearchState.CONNECTION_ERROR -> {
                historyText.visibility = View.GONE
                clearSearchButton.visibility = View.GONE
                trackRecycleView.visibility = View.GONE
                searchNotification.visibility = View.VISIBLE
                errorText.setText(R.string.error_connection_subtitle)
                errorConnectionText.visibility = View.VISIBLE
                refreshButton.visibility = View.VISIBLE
                errorImage.setImageResource(
                    if (isDarkModeOn()) R.drawable.ic_connection_err_dark else R.drawable.ic_connection_err_light
                )
            }
            SearchState.HISTORY_LIST -> {
                searchHistoryAdapter.tracks = searchHistory.getTrackList()
                trackRecycleView.adapter = searchHistoryAdapter
                trackRecycleView.visibility = View.VISIBLE
                searchNotification.visibility = View.GONE
                refreshButton.visibility = View.GONE
                historyText.visibility = View.VISIBLE
                clearSearchButton.visibility = View.VISIBLE
            }
        }
    }

    private fun isDarkModeOn(): Boolean {
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isDarkModeOn = nightModeFlags == Configuration.UI_MODE_NIGHT_YES
        return isDarkModeOn
    }


    private fun searchTrackList() {
        val itunesService = ITunesService().get()
        if(inputEditText.text.isNotEmpty()) {
            itunesService.search(inputEditText.text.toString()).enqueue(object : Callback<TrackResponse> {
                    override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                        if(response.isSuccessful) {
                            searchTrackList.clear()
                            val resBody = response.body()?.results
                            if(resBody?.isNotEmpty() == true) {
                                searchTrackList.addAll(resBody)
                                trackAdapter.notifyDataSetChanged()
                                showResultNotification(SearchState.SUCCESS)
                            } else {
                                showResultNotification(SearchState.NOT_FOUND)
                            }
                        } else {
                            showResultNotification(SearchState.CONNECTION_ERROR)
                        }
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        showResultNotification(SearchState.CONNECTION_ERROR)
                    }
                }
            )
        }
    }

    private fun clickOnTrack(track: TrackData) {
        searchHistory.addTrackToList(track)
    }
}