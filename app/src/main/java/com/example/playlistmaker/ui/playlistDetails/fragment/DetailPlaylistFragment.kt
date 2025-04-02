package com.example.playlistmaker.ui.playlistDetails.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistDetailBinding
import com.example.playlistmaker.domain.playlistDetails.model.PlaylistDetails
import com.example.playlistmaker.domain.search.model.TrackData
import com.example.playlistmaker.presentation.search.TrackAdapter
import com.example.playlistmaker.ui.player.fragment.PlayerFragment
import com.example.playlistmaker.ui.player.fragment.PlayerFragment.Companion.TRACK
import com.example.playlistmaker.ui.playlistDetails.view_model.DetailPlaylistViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.abs

class DetailPlaylistFragment : Fragment() {
    private var _binding: FragmentPlaylistDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var trackBottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var menuBottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private val playlistId: Int by lazy {
        requireArguments().getInt(PLAYLIST_ID) ?: -1
    }

    private val viewModel: DetailPlaylistViewModel by viewModel { parametersOf(playlistId) }

    private var _adapter: TrackAdapter? = null
    private val adapter get() = _adapter!!

    private val playlistTracks: MutableList<TrackData> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _adapter = TrackAdapter(playlistTracks)
        adapter.eventListener = addAdapterListener()
        adapter.longClickListener = addLongClickListener()

        binding.tracksRecycler.adapter = adapter
        binding.tracksRecycler.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, true
        ).also { it.stackFromEnd = true }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.getPlaylistInfo()

        viewModel.playlistLiveData.observe(viewLifecycleOwner) {
            render(it)
            showPlaylistTracksBottomSheet(it)
        }

        binding.iconShare.setOnClickListener {
            sendPlaylistMessage()
        }

        binding.menuBtn.setOnClickListener {
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.menuBottomSheet.visibility = View.VISIBLE
        }

        binding.shareMenuBottomSheet.setOnClickListener {
            sendPlaylistMessage()
        }

        binding.deleteMenuBottomSheet.setOnClickListener {
            val deletePlaylistDialog = createRemovePlaylistDialog()
            deletePlaylistDialog.show()
        }

        binding.editMenuBottomSheet.setOnClickListener {
            findNavController().navigate(
                R.id.action_playlistFragment_to_playlistEditFragment,
                createArgs(playlistId)
            )
        }

        setBottomSheets()
    }

    private fun addAdapterListener(): (TrackData) -> Unit {
        return { track: TrackData ->
            if (viewModel.isClickOnTrackAllowedLiveData.value!!) {
                viewModel.clickOnTrackDebounce()

                findNavController().navigate(
                    R.id.action_playlistDetails_to_playerActivity,
                    PlayerFragment.createArgs(Gson().toJson(track))
                )
            }
        }
    }

    private fun setBottomSheets() {
        trackBottomSheetBehavior = BottomSheetBehavior.from(binding.tracksBottomSheet).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }
        menuBottomSheetBehavior = BottomSheetBehavior.from(binding.menuBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            binding.overlay.visibility = View.GONE
                        }

                        else -> {
                            binding.overlay.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    binding.overlay.alpha = 1 - abs(slideOffset)
                }
            })
        }
    }

    private fun render(playlistDetails: PlaylistDetails) {

        val playlist = playlistDetails.playlist

        Glide.with(this)
            .load(playlist.coverUri)
            .signature(ObjectKey(playlistDetails.playlist.id))
            .apply(
                RequestOptions().placeholder(R.drawable.default_art_work_player)
            )
            .transform(
                CenterCrop(),
            )
            .into(binding.playlistCover)

        binding.apply {
            playlistTitle.text = playlist.title
            if (!playlist.description.isNullOrBlank()) {
                playlistDescription.text = playlist.description
                playlistDescription.isVisible = true
            }
            duration.text = resources.getQuantityString(R.plurals.playlist_details_plurals, playlistDetails.durationInMinutes, playlistDetails.durationInMinutes)
            tracksTotal.text = resources.getQuantityString(R.plurals.playlist_plurals, playlist.size, playlist.size)
        }

        playlistTracks.clear()
        playlistTracks.addAll(playlistDetails.tracks)
        adapter?.notifyDataSetChanged()

        binding.placeholderEmptyMessage.isVisible = playlistDetails.tracks.isEmpty()
        binding.tracksRecycler.isVisible = playlistDetails.tracks.isNotEmpty()
    }

    private fun showPlaylistTracksBottomSheet(playlistInfo: PlaylistDetails) {
        val playlist = playlistInfo.playlist

        binding.playlistMenuBottomSheet.apply {
            Glide.with(this@DetailPlaylistFragment)
                .load(playlist.coverUri)
                .signature(ObjectKey(playlistInfo.playlist.id))
                .apply(
                    RequestOptions().placeholder(R.drawable.default_art_work)
                )
                .transform(
                    CenterCrop(),
                    RoundedCorners(resources.getDimensionPixelSize(R.dimen.corner_radius_item))
                )
                .into(cover)
            title.text = playlist.title
            size.text = resources.getQuantityString(R.plurals.playlist_plurals, playlist.size, playlist.size)
        }
    }

    private fun addLongClickListener(): (TrackData) -> Unit {
        return { trackRepresentation: TrackData ->
            val deleteTrackDialog = confirmDeleteTrackDialog(trackRepresentation)
            deleteTrackDialog.show()
        }
    }

    private fun confirmDeleteTrackDialog(track: TrackData): MaterialAlertDialogBuilder {
        val dialogListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    viewModel.deleteTrack(track)
                }
            }
        }
        return MaterialAlertDialogBuilder(requireContext(), R.style.ModalStyle)
            .setTitle(resources.getString(R.string.want_to_delete_track))
            .setPositiveButton(resources.getString(R.string.yes), dialogListener)
            .setNegativeButton(resources.getString(R.string.no), dialogListener)
    }

    private fun sendPlaylistMessage() {
        viewModel.playlistLiveData.value?.let {
            if (it.tracks.isEmpty()) {
                Toast.makeText(requireContext(), R.string.playlist_detail_no_tracks, Toast.LENGTH_SHORT).show()
            } else {
                viewModel.sharePlaylist(
                    formMessage(it)
                )
            }
        }
    }

    private fun formMessage(playlistInfo: PlaylistDetails): String {
        val playlist = playlistInfo.playlist
        val message = StringBuilder().apply {
            appendLine(playlist.title)
            if (playlist.description?.isNotBlank() == true) appendLine(playlist.description)
            appendLine(resources.getQuantityString(R.plurals.playlist_plurals, playlist.size, playlist.size))
        }

        playlistInfo.tracks.forEach {
            message.appendLine(
                resources.getString(R.string.track_detail_message)
                    .format(playlistInfo.tracks.indexOf(it) + 1, it.artistName, it.trackName, it.trackTimeMillis)
            )
        }

        return message.toString()
    }

    private fun createRemovePlaylistDialog(): MaterialAlertDialogBuilder {
        val dialogListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    viewModel.deletePlaylist()
                    findNavController().navigateUp()
                }
            }
        }
        return MaterialAlertDialogBuilder(requireContext(), R.style.ModalStyle)
            .setTitle(resources.getString(R.string.playlist_detail_more_delete))
            .setMessage(resources.getString(R.string.want_to_delete_playlist))
            .setPositiveButton(resources.getString(R.string.yes), dialogListener)
            .setNegativeButton(resources.getString(R.string.no), dialogListener)
    }

    override fun onResume() {
        super.onResume()
        trackBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _adapter = null
    }

    companion object {

        const val PLAYLIST_ID = "PLAYLIST_ID"

        fun createArgs(playlistId: Int) = bundleOf(PLAYLIST_ID to playlistId)
    }
}