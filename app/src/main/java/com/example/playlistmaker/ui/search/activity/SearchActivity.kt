package com.example.playlistmaker.ui.search.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.search.model.SearchState
import com.example.playlistmaker.domain.search.model.TrackData
import com.example.playlistmaker.presentation.TrackAdapter
import com.example.playlistmaker.ui.player.activity.PlayerActivity
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {
    companion object {
        private const val INPUT_SEARCH = "INPUT_SEARCH"
        const val TRACK = "TRACK"
    }

    private lateinit var binding: ActivitySearchBinding

    private val viewModel: SearchViewModel by viewModel()

    private lateinit var trackAdapter: TrackAdapter

    private lateinit var historyTrackListAdapter: TrackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        trackAdapter = TrackAdapter(viewModel.searchTrackListLiveData.value!!)
        val historyTrackList = viewModel.searchHistoryTrackListLiveData.value!!
        historyTrackListAdapter = TrackAdapter(historyTrackList)

        binding.trackSearchRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        trackAdapter.eventListener = clickOnTrackListener()

        binding.trackSearchHistoryRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        historyTrackListAdapter.eventListener = clickOnTrackListener()

        viewModel.liveDataState.observe(this) {
            showResultNotification(it)
        }

        binding.arrowBack.setOnClickListener {
            finish()
        }

        binding.trackSearchRecyclerView.adapter = trackAdapter
        binding.trackSearchHistoryRecyclerView.adapter = historyTrackListAdapter

        if (historyTrackList.isEmpty()) {
            binding.recycleHistoryContainer.visibility = View.GONE
        }

        binding.textSearch.setOnFocusChangeListener { view, hasFocus ->
            if (historyTrackList.isNotEmpty() && hasFocus)
                showResultNotification(SearchState.HISTORY_LIST)
        }

        binding.arrowBack.setOnClickListener {
            finish()
        }

        binding.clearIcon.setOnClickListener {
            clearSearchForm()
            if (historyTrackList.isEmpty()) {
                binding.recycleHistoryContainer.visibility = View.GONE
            }
        }

        binding.refreshButton.setOnClickListener {
            viewModel.startImmediateSearch(binding.textSearch.text.toString())
        }

        binding.clearSearchButton.setOnClickListener {
            viewModel.clearHistory()
            historyTrackListAdapter.notifyDataSetChanged()
            binding.recycleHistoryContainer.visibility = View.GONE
            showResultNotification(SearchState.EMPTY_DATA)
        }

        if (savedInstanceState != null) {
            binding.textSearch.setText(INPUT_SEARCH)
        }

        binding.textSearch.requestFocus()

        binding.textSearch.doOnTextChanged { input, start, before, count ->
            if (binding.textSearch.hasFocus() && input.isNullOrEmpty() && historyTrackList.isNotEmpty()) {
                binding.recycleHistoryContainer.visibility = View.VISIBLE
            } else {
                binding.recycleHistoryContainer.visibility = View.GONE
            }

            if (input.isNullOrEmpty()) binding.clearIcon.visibility = View.GONE
            else binding.clearIcon.visibility = View.VISIBLE

            viewModel.startDebounceSearch(input.toString())
        }

        binding.textSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.startImmediateSearch(binding.textSearch.text.toString())
                true
            }
            false
        }

        binding.textSearch.setOnFocusChangeListener { view, hasFocus ->
            binding.recycleHistoryContainer.visibility =
                if (hasFocus && historyTrackList.isNotEmpty() && binding.textSearch.text.isEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }
    }

    private fun clearSearchForm() {
        binding.textSearch.setText("")
        viewModel.clear()
        historyTrackListAdapter.notifyDataSetChanged()

        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun showResultNotification(resultType: SearchState) {
        Log.d("STATE-----", "$resultType")
        when (resultType) {
            SearchState.SUCCESS -> {
                trackAdapter.notifyDataSetChanged()
                binding.recycleContainer.visibility = View.VISIBLE
                binding.searchNotification.visibility = View.GONE
                binding.clearIcon.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }

            SearchState.NOT_FOUND -> {
                trackAdapter.notifyDataSetChanged()
                binding.clearIcon.visibility = View.GONE
                binding.recycleContainer.visibility = View.GONE
                binding.searchNotification.visibility = View.VISIBLE
                binding.errorText.setText(R.string.not_found_error)
                binding.errorConnectionText.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                binding.notFound.setImageResource(R.drawable.ic_not_found_dark)
            }

            SearchState.SEARCH_PROGRESS -> {
                binding.recycleContainer.visibility = View.VISIBLE
                binding.clearIcon.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            }

            SearchState.CONNECTION_ERROR -> {
                trackAdapter.notifyDataSetChanged()
                binding.clearIcon.visibility = View.GONE
                binding.recycleContainer.visibility = View.GONE
                binding.searchNotification.visibility = View.VISIBLE
                binding.errorText.setText(R.string.error_connection_subtitle)
                binding.errorConnectionText.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                binding.notFound.setImageResource(R.drawable.ic_connection_err_dark)
            }

            SearchState.HISTORY_LIST -> {
                historyTrackListAdapter.notifyDataSetChanged()
                trackAdapter.notifyDataSetChanged()
                binding.recycleHistoryContainer.visibility = View.VISIBLE
                binding.recycleContainer.visibility = View.GONE
                binding.searchNotification.visibility = View.GONE
                binding.clearIcon.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
            }

            SearchState.EMPTY_DATA -> {
                historyTrackListAdapter.notifyDataSetChanged()
                binding.recycleContainer.visibility = View.GONE
            }
        }
    }

    private fun clickOnTrackListener(): (TrackData) -> Unit {
        return { track: TrackData ->
            if (viewModel.isClickAllowedLiveData.value!!) {
                viewModel.clickOnTrackDebounce(track)
                val playerIntent = Intent(this, PlayerActivity::class.java)
                playerIntent.putExtra(TRACK, Gson().toJson(track))
                startActivity(playerIntent)

                historyTrackListAdapter.notifyDataSetChanged()
            }
        }
    }
}