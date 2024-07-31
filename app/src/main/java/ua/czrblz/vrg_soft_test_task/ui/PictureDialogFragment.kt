package ua.czrblz.vrg_soft_test_task.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import ua.czrblz.vrg_soft_test_task.databinding.FragmentPictureDialogBinding
import ua.czrblz.vrg_soft_test_task.listener.SavePictureListener
import ua.czrblz.vrg_soft_test_task.utils.isInListOfImageExtensions
import ua.czrblz.vrg_soft_test_task.utils.loadPictureInLargeFormat

class PictureDialogFragment : DialogFragment() {

    private var _binding: FragmentPictureDialogBinding? = null
    private val binding get() = _binding!!

    private var listener: SavePictureListener? = null

    companion object {

        const val IMAGE_URL = "image_url"
        const val THUMBNAIL_URL = "thumbnail_url"

        @JvmStatic
        fun newInstance(imageUrl: String, thumbnailUrl: String) =
            PictureDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(IMAGE_URL, imageUrl)
                    putString(THUMBNAIL_URL, thumbnailUrl)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        listener = try {
            if (parentFragment != null) {
                parentFragment as SavePictureListener
            } else {
                requireActivity() as SavePictureListener
            }
        } catch (e: java.lang.ClassCastException) {
            throw java.lang.ClassCastException("Calling fragment must implement Callback interface")
        }

        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        _binding = FragmentPictureDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.getString(IMAGE_URL)?.let { imageUrl ->
            arguments?.getString(THUMBNAIL_URL)?.let { thumbnailUrl ->
                with(binding) {
                    val currentPicture = if (imageUrl.isInListOfImageExtensions())
                        imageUrl
                     else
                        thumbnailUrl
                    image.loadPictureInLargeFormat(currentPicture)
                    downloadButton.setOnClickListener {
                        listener?.savePicture(currentPicture)
                    }
                    closeButton.setOnClickListener {
                        dialog?.dismiss()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}