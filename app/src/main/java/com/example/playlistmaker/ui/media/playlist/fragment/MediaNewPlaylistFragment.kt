package com.example.playlistmaker.ui.media.playlist.fragment

import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediaNewPlaylistBinding
import com.example.playlistmaker.ui.media.playlist.view_model.MediaNewPlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class MediaNewPlaylistFragment : Fragment() {
    private var _binding: FragmentMediaNewPlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MediaNewPlaylistViewModel by viewModel()
    private val dialogModal by lazy { createConfirmExitDialog() }

    private val galleryImage = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { imageUri ->
        if (imageUri != null) {
            Glide.with(this).load(imageUri).transform(
                CenterCrop(),
                RoundedCorners(requireActivity().resources.getDimensionPixelSize(R.dimen.corner_radius))
            ).into(binding.playlistCover)
            viewModel.setUri(imageUri)
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

        viewModel.playlistUriLiveData.value?.let {
            binding.playlistCover.setImageURI(it)
        }

        binding.arrowBackPlaylist.setOnClickListener {
            closeWithoutSaving()
        }

        binding.playlistTitleInputEditText.doOnTextChanged { text, start, before, count ->
            binding.createNewPlaylistButton.isEnabled = text?.isNotBlank() == true
        }

        binding.playlistCover.setOnClickListener {
            galleryImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.createNewPlaylistButton.setOnClickListener {
            val playlistTitle = binding.playlistTitleInputEditText.text.toString()
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.playlist_created).format(playlistTitle),
                Toast.LENGTH_SHORT
            ).show()

            viewModel.playlistUriLiveData.value?.let { saveCoverToStorage(it) }
            viewModel.createNewPlaylist(
                title = playlistTitle,
                description = binding.playlistDescriptionInputEditText.text.toString()
            )

            findNavController().popBackStack()
        }
    }

    private fun closeWithoutSaving() {
        if (binding.playlistDescriptionInputEditText.text?.isNotBlank() == true
            || binding.playlistTitleInputEditText.text?.isNotBlank() == true
            || viewModel.playlistUriLiveData.value != null
        ) {
            dialogModal.show()
        } else {
            findNavController().popBackStack()
        }
    }

    private fun createConfirmExitDialog(): MaterialAlertDialogBuilder {
        val dialogInterface = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    findNavController().popBackStack()
                }

                DialogInterface.BUTTON_NEGATIVE -> null
            }
        }
        return MaterialAlertDialogBuilder(
            requireContext(),
            R.style.ModalStyle
        ).setTitle(resources.getString(R.string.playlist_create_complete))
            .setMessage(resources.getString(R.string.playlist_create_lost))
            .setPositiveButton(
                resources.getString(R.string.playlist_create_accept),
                dialogInterface
            )
            .setNeutralButton(resources.getString(R.string.playlist_create_cancel), dialogInterface)
    }

    private fun saveCoverToStorage(uri: Uri) {
        val coverFilePath =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Covers")
        if (!coverFilePath.exists()) coverFilePath.mkdirs()

        val coverFile = File(coverFilePath, "cover${coverFilePath.listFiles()?.size}.jpg")

        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(coverFile)
        BitmapFactory.decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}