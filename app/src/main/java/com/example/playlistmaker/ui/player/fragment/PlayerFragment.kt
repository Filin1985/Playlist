package com.example.playlistmaker.ui.player.fragment

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.player.PlayerControl
import com.example.playlistmaker.domain.player.model.MediaPlayerState
import com.example.playlistmaker.domain.player.model.TrackPlaylistState
import com.example.playlistmaker.domain.search.model.TrackData
import com.example.playlistmaker.ui.player.activity.PlayerBottomSheetAdapter
import com.example.playlistmaker.ui.player.view_model.PlayerVewModel
import com.example.playlistmaker.ui.search.fragment.SearchFragment
import com.example.playlistmaker.ui.service.PlayerService
import com.example.playlistmaker.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerFragment : Fragment() {
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private val track: String by lazy {
        requireArguments().getString(TRACK) ?: throw IllegalStateException("Track data not found")
    }
    private val viewModel: PlayerVewModel by viewModel { parametersOf(track) }
    private lateinit var adapter: PlayerBottomSheetAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private var playerControl: PlayerControl? = null
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as PlayerService.PlayerServiceBinder
            playerControl = binder.getService()
            viewModel.playerControlManager(binder.getService())
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            viewModel.removePlayerControl()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            bindMusicService()
        } else {
            Utils.showSnackbar(binding.root, getString(R.string.no_permission), requireContext())
        }
    }

    private fun bindMusicService() {
        val intent = Intent(requireContext(), PlayerService::class.java).putExtra(
            SearchFragment.TRACK,
            track
        )
        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val trackData = Gson().fromJson(track, TrackData::class.java)

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet).also {
            it.state = BottomSheetBehavior.STATE_HIDDEN

            it.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            binding.overlay.isVisible = false
                        }

                        else -> {
                            binding.overlay.isVisible = true
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
        }

        adapter = PlayerBottomSheetAdapter(viewModel.playlistLiveData.value!!)
        adapter.listener = {
            viewModel.addTrackToPlaylist(it)
            showTrackAddingToPlaylistResult(viewModel.playlistStateLiveData.value!!, it)
        }
        binding.recyclerViewPlaylists.adapter = adapter
        binding.recyclerViewPlaylists.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.arrowBackPlayer.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.addToPlaylist.setOnClickListener {
            viewModel.showPlaylists()
            binding.recyclerViewPlaylists.isVisible = true
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        viewModel.playlistLiveData.observe(viewLifecycleOwner) {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
                adapter.notifyDataSetChanged()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            bindMusicService()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.playerState.collect { state ->
                stateRender(state)
            }
        }

        viewModel.timeProgress.observe(viewLifecycleOwner) {
            binding.playerTime.text = it
        }

        viewModel.isTrackInFavoriteLiveData.observe(viewLifecycleOwner) {
            toggleFavoriteTrack(it)
        }

        val albumCover = binding.albumCover
        Glide.with(albumCover)
            .load(trackData.artworkUrl100.replaceAfterLast("/", "512x512bb.jpg"))
            .placeholder(R.drawable.default_art_work)
            .transform(RoundedCorners(albumCover.resources.getDimensionPixelSize(R.dimen.art_work)))
            .into(albumCover)

        binding.playerSongName.text = trackData.trackName

        binding.playerBand.text = trackData.artistName

        if (trackData.collectionName.isEmpty()) {
            binding.albumCover.visibility = View.GONE
        } else binding.playerAlbumData.text = trackData.collectionName

        val formatDate = SimpleDateFormat("yyyy", Locale.getDefault()).parse(trackData.releaseDate)
        val year = formatDate?.let { SimpleDateFormat("yyyy", Locale.getDefault()).format(it) }
        binding.playerYearData.text = year

        binding.playerGenreData.text = trackData.primaryGenreName
        binding.playerCountryData.text = trackData.country

        binding.customButtonView.setOnClickListener {
            viewModel.playbackControl()
            viewModel.startTimer()
        }

        binding.createNewPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_newPlaylistFragment)
        }

        binding.playerLike.setOnClickListener {
            viewModel.toggleTrackFavoriteState()
        }
    }

    override fun onPause() {
        super.onPause()
    }

    private fun stateRender(playerState: MediaPlayerState) {
        when (playerState) {
            MediaPlayerState.STATE_PLAYING -> {}
            MediaPlayerState.STATE_PAUSED -> {}
            MediaPlayerState.STATE_PREPARED -> {
                binding.playerTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(0)
            }

            MediaPlayerState.STATE_DEFAULT -> {}
        }
    }

    private fun playTimeRender(time: Int) {
        when (viewModel.playerState.value) {
            MediaPlayerState.STATE_PLAYING, MediaPlayerState.STATE_PAUSED -> {
                binding.playerTime.text =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
            }

            MediaPlayerState.STATE_PREPARED -> {
                binding.playerTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(0)
            }

            MediaPlayerState.STATE_DEFAULT, null -> {}
        }
    }

    private fun toggleFavoriteTrack(isTrackInFavorite: Boolean?) {
        val likeImage =
            if (isTrackInFavorite == true) R.drawable.ic_player_like_active else R.drawable.ic_player_like
        binding.playerLike.setImageDrawable(ContextCompat.getDrawable(requireContext(), likeImage))
    }

    private fun showTrackAddingToPlaylistResult(
        trackPlaylistRelationship: TrackPlaylistState,
        playlist: Playlist
    ) {
        if (trackPlaylistRelationship == TrackPlaylistState.TRACK_IS_ALREADY_ADDED) {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.track_is_already_in_playlist).format(playlist.title),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.track_is_added_to_playlist).format(playlist.title),
                Toast.LENGTH_SHORT
            ).show()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.pausePlayer()
        viewModel.hideNotification()
        requireContext().unbindService(serviceConnection)
    }

    override fun onStop() {
        super.onStop()
        viewModel.showNotification()
    }

    companion object {
        const val TRACK = "TRACK"

        fun createArgs(encodedTrack: String): Bundle {
            return bundleOf(TRACK to encodedTrack)
        }
    }
}
