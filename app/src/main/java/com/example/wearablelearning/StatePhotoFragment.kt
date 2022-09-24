package com.example.wearablelearning

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

/**
 * The [StatePhotoFragment] class is called by [GameActivity]. This fragment holds a photo.
 */
class StatePhotoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_state_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /** Retrieve the text from the bundle. This may be null/empty in which case no text is displayed. */
        val stateContent = this.requireArguments().getString("text")
        getView()?.findViewById<TextView>(R.id.state_tv)?.text = stateContent

        /** Retrieve the photo name from the bundle and set _state_imageView_ to the photo resource. */
        val stateImage = this.requireArguments().getString("image")
        val imageName = stateImage.toString().split(".")[0]
        val resID = resources.getIdentifier(imageName, "raw", context?.packageName)
        getView()?.findViewById<ImageView>(R.id.state_imageView)?.setImageResource(resID)
    }
}