package com.example.playlistmaker.ui.search.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity.INPUT_METHOD_SERVICE
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.search.model.SearchState
import com.example.playlistmaker.domain.search.model.TrackData
import com.example.playlistmaker.presentation.search.TrackAdapter
import com.example.playlistmaker.ui.player.activity.PlayerActivity
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment: Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModel()

    private lateinit var trackAdapter: TrackAdapter

    private lateinit var historyTrackListAdapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trackAdapter = TrackAdapter(viewModel.searchTrackListLiveData.value!!)
        val historyTrackList = viewModel.searchHistoryTrackListLiveData.value!!
        historyTrackListAdapter = TrackAdapter(historyTrackList)

        binding.trackSearchRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        trackAdapter.eventListener = clickOnTrackListener()

        binding.trackSearchHistoryRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        historyTrackListAdapter.eventListener = clickOnTrackListener()

        viewModel.liveDataState.observe(viewLifecycleOwner) {
            showResultNotification(it)
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

        binding.textSearch.doOnTextChanged { text, start, before, count ->
            if (binding.textSearch.hasFocus() && text.isNullOrEmpty() && historyTrackList.isNotEmpty()) {
                binding.recycleHistoryContainer.visibility = View.VISIBLE
            } else {
                binding.recycleHistoryContainer.visibility = View.GONE
            }

            if (text.isNullOrEmpty()) binding.clearIcon.visibility = View.GONE
            else binding.clearIcon.visibility = View.VISIBLE

            viewModel.startDebounceSearch(text.toString())
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

        val view = activity?.currentFocus;
        if (view != null) {
            val imm = activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun showResultNotification(resultType: SearchState) {
        when (resultType) {
            SearchState.SUCCESS -> {
                historyTrackListAdapter.notifyDataSetChanged()
                trackAdapter.notifyDataSetChanged()
                binding.recycleContainer.visibility = View.VISIBLE
                binding.trackSearchRecyclerView.visibility = View.VISIBLE
                binding.searchNotification.visibility = View.GONE
                binding.clearIcon.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }

            SearchState.NOT_FOUND -> {
                historyTrackListAdapter.notifyDataSetChanged()
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
                binding.trackSearchRecyclerView.visibility = View.GONE
                binding.clearIcon.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            }

            SearchState.CONNECTION_ERROR -> {
                historyTrackListAdapter.notifyDataSetChanged()
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
                binding.recycleContainer.visibility = View.GONE
                binding.searchNotification.visibility = View.GONE
                binding.errorConnectionText.visibility = View.GONE
            }
        }
    }

    private fun clickOnTrackListener(): (TrackData) -> Unit {
        return { track: TrackData ->
            if (viewModel.isClickAllowedLiveData.value!!) {
                viewModel.clickOnTrackDebounce(track)
                findNavController().navigate(
                    R.id.action_searchFragment_to_playerActivity,
                    PlayerActivity.createArgs(Gson().toJson(track))
                )
                viewModel.writeTrackToList(track)
                historyTrackListAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        clearSearchForm()
        viewModel.cancelSearch()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val INPUT_SEARCH = "INPUT_SEARCH"
        const val TRACK = "TRACK"
    }
}