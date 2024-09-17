package com.example.wearablelearning

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.math.roundToInt
import org.wlcp.wlcpgameserverapi.client.WLCPGameClient

class TransitionSequenceFragment : Fragment() {

    companion object {
        private var colorBtnClicks = 0
        private var colorSequence: String = ""

        fun newInstance(): TransitionSequenceFragment {
            return TransitionSequenceFragment()
        }
    }

    private lateinit var wlcpGameClient: WLCPGameClient

    fun setWLCPGameClient(wlcpGameClient: WLCPGameClient) {
        this.wlcpGameClient = wlcpGameClient
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transition_sequence, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val submitButton = view.findViewById<Button>(R.id.transition_submit_btn)
        val clearButton = view.findViewById<Button>(R.id.transition_clear_btn)
        val redButton = view.findViewById<Button>(R.id.question_red_btn)
        val greenButton = view.findViewById<Button>(R.id.question_green_btn)
        val blueButton = view.findViewById<Button>(R.id.question_blue_btn)
        val blackButton = view.findViewById<Button>(R.id.question_black_btn)

        val scrollView = view.findViewById<HorizontalScrollView>(R.id.scrollView)
        val leftIcon = view.findViewById<MaterialButton>(R.id.question_temp_btn_left)
        val rightIcon = view.findViewById<MaterialButton>(R.id.question_temp_btn_right)

        scrollView.viewTreeObserver.addOnScrollChangedListener {
            val maxScrollX = scrollView.getChildAt(0).measuredWidth - scrollView.measuredWidth

            if (colorBtnClicks > 6) {
                when (scrollView.scrollX) {
                    0 -> rightIcon.setIconTintResource(R.color.darkgrey)
                    maxScrollX -> leftIcon.setIconTintResource(R.color.darkgrey)
                    else -> {
                        rightIcon.setIconTintResource(R.color.darkgrey)
                        leftIcon.setIconTintResource(R.color.darkgrey)
                    }
                }
            }
        }

        submitButton.setOnClickListener {
            wlcpGameClient.sendSequenceButtonPress(colorSequence)
            clearCurrentSequence(view)
        }

        clearButton.setOnClickListener {
            context?.let { context1 ->
                MaterialAlertDialogBuilder(context1, R.style.Theme_WearableLearning_AlertDialog)
                    .setMessage(resources.getString(R.string.confirm_clear_text))
                    .setNegativeButton(resources.getString(R.string.no_text)) { dialog, _ ->
                        dialog.cancel()
                    }
                    .setPositiveButton(resources.getString(R.string.yes_text)) { _, _ ->
                        clearCurrentSequence(view)
                    }
                    .show()
            }
        }

        redButton.setOnClickListener { handleButtonClick(R.color.red, "1", view) }
        greenButton.setOnClickListener { handleButtonClick(R.color.green, "2", view) }
        blueButton.setOnClickListener { handleButtonClick(R.color.blue, "3", view) }
        blackButton.setOnClickListener { handleButtonClick(R.color.black, "4", view) }
    }

    private fun handleButtonClick(color: Int, keyword: String, view: View) {
        colorBtnClicks++
        addBlockToSolution(color, view)
        addKeywordToSequence(keyword)
    }

    private fun clearCurrentSequence(view: View) {
        val containingView = view.findViewById<LinearLayout>(R.id.linearLayout)
        containingView.removeAllViews()

        view.findViewById<HorizontalScrollView>(R.id.scrollView).isScrollbarFadingEnabled = false
        view.findViewById<MaterialButton>(R.id.question_temp_btn_right).setIconTintResource(R.color.lightgrey)
        view.findViewById<MaterialButton>(R.id.question_temp_btn_left).setIconTintResource(R.color.lightgrey)

        colorBtnClicks = 0
        colorSequence = ""
    }

    private fun addBlockToSolution(color: Int, view: View) {
        val blockNum = colorBtnClicks
        val block = createBlock(blockNum)
        block.setBackgroundColor(ContextCompat.getColor(requireContext(), color))

        val containingView = view.findViewById<LinearLayout>(R.id.linearLayout)
        containingView?.addView(block)

        if (colorBtnClicks > 6) {
            view.findViewById<MaterialButton>(R.id.question_temp_btn_right).setIconTintResource(R.color.darkgrey)
            view.findViewById<HorizontalScrollView>(R.id.scrollView).isScrollbarFadingEnabled = false
        }
    }

    private fun createBlock(blockNum: Int): Button {
        val scale = resources.displayMetrics.density
        val widthPixels = (47 * scale + 0.5f).roundToInt()
        val heightPixels = (50 * scale + 0.5f).roundToInt()
        val marginRightPixels = (4 * scale + 0.5f).roundToInt()

        val newBlock = Button(context)
        newBlock.layoutParams = LinearLayout.LayoutParams(widthPixels, heightPixels).apply {
            setMargins(0, 0, marginRightPixels, 0)
        }
        newBlock.tag = "question_temp_btn_$blockNum"
        newBlock.text = ""

        return newBlock
    }

    private fun addKeywordToSequence(keyword: String) {
        colorSequence += keyword
    }
}
