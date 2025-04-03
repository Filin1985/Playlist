package com.example.playlistmaker.ui.media.playlist.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediaNewPlaylistBinding
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.ui.media.playlist.view_model.PlaylistEditViewModel
import com.example.playlistmaker.ui.playlistDetails.fragment.DetailPlaylistFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistEditFragment : Fragment() {

    private var _binding: FragmentMediaNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private var playlistId: Int = -1

    private val viewModel by viewModel<PlaylistEditViewModel> { parametersOf(playlistId) }

    private val photoLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { photoUri ->
        if (photoUri != null) {
            Glide.with(this)
                .load(photoUri)
                .transform(
                    CenterCrop(),
                    RoundedCorners(requireActivity().resources.getDimensionPixelSize(R.dimen.corner_radius))
                )
                .into(binding.playlistCover)
            viewModel.setNewPlaylistUri(photoUri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMediaNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistId = arguments?.getInt(DetailPlaylistFragment.PLAYLIST_ID)!!

        renderUI()

        binding.playlistCover.setOnClickListener {
            photoLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.playlistTitleInputEditText.doOnTextChanged { titleAfterEditing, start, before, count ->
            binding.createNewPlaylistButton.isEnabled = titleAfterEditing?.isNotBlank() == true
        }

        binding.createNewPlaylistButton.setOnClickListener {
            saveTitleIfUpdated()
            saveDescriptionIfUpdated()

            viewModel.updatePlaylist()

            findNavController().navigateUp()
        }

        binding.arrowBackPlaylist.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.playlistLiveData.observe(viewLifecycleOwner) {
            renderPlaylistInfo(it)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.playlistLiveData.removeObservers(viewLifecycleOwner)
        saveTitleIfUpdated()
        saveDescriptionIfUpdated()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun saveTitleIfUpdated() {
        val newTitle = binding.playlistTitleInputEditText.text.toString()
        if (newTitle != viewModel.playlistLiveData.value?.title) {
            viewModel.setNewPlaylistTitle(newTitle)
        }
    }

    private fun saveDescriptionIfUpdated() {
        val newDescription = binding.playlistDescriptionInputEditText.text.toString()
        if (newDescription != viewModel.playlistLiveData.value?.description) {
            viewModel.setNewPlaylistDescription(newDescription)
        }
    }

    private fun renderUI() {
        binding.mainTitle.text = resources.getString(R.string.edit_playlist)
        binding.createNewPlaylistButton.text = resources.getString(R.string.save_edit_playlist)
    }

    private fun renderPlaylistInfo(playlist: Playlist) {

        Glide.with(this)
            .load(playlist.coverUri)
            .signature(ObjectKey(playlist.id))
            .apply(
                RequestOptions().placeholder(R.drawable.default_art_work_player)
            )
            .transform(
                CenterCrop(),
                RoundedCorners(requireActivity().resources.getDimensionPixelSize(R.dimen.corner_radius))
            )
            .into(binding.playlistCover)

        binding.playlistTitleInputEditText.setText(playlist.title)

        playlist.description?.let {
            binding.playlistDescriptionInputEditText.setText(playlist.description)
        }
    }
}