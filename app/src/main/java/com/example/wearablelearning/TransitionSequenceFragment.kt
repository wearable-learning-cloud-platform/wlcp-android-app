package com.example.wearablelearning

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.math.roundToInt


class TransitionSequenceFragment : Fragment() {
    companion object {
        private var colorBtnClicks = 0
        private var colorSequence: String = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transition_sequence, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = this.requireArguments().getString("id")
        val solution = this.requireArguments().getString("content")

        val submitButton = view.findViewById<Button>(R.id.transition_submit_btn)
        val clearButton = view.findViewById<Button>(R.id.transition_clear_btn)
        val redButton = view.findViewById<Button>(R.id.question_red_btn)
        val greenButton = view.findViewById<Button>(R.id.question_green_btn)
        val blueButton = view.findViewById<Button>(R.id.question_blue_btn)
        val blackButton = view.findViewById<Button>(R.id.question_black_btn)

        var scrollView = view.findViewById<HorizontalScrollView>(R.id.scrollView)
        var leftIcon = view.findViewById<MaterialButton>(R.id.question_temp_btn_left)
        var rightIcon = view.findViewById<MaterialButton>(R.id.question_temp_btn_right)

        scrollView.viewTreeObserver.addOnScrollChangedListener {
            var maxScrollX = scrollView.getChildAt(0).measuredWidth - scrollView.measuredWidth

            if(colorBtnClicks > 6) {
                if (scrollView.scrollX == 0) {
                    rightIcon.setIconTintResource(R.color.darkgrey)
                } else {
                    rightIcon.setIconTintResource(R.color.lightgrey)
                }

                if (scrollView.scrollX == maxScrollX) {
                    leftIcon.setIconTintResource(R.color.darkgrey)
                } else {
                    leftIcon.setIconTintResource(R.color.lightgrey)
                }

                if (scrollView.scrollX != 0 && scrollView.scrollX != maxScrollX) {
                    rightIcon.setIconTintResource(R.color.darkgrey)
                    leftIcon.setIconTintResource(R.color.darkgrey)
                }
            }
        }

        submitButton.setOnClickListener { v ->
            if(colorSequence == solution) {
                colorBtnClicks = 0
                colorSequence = ""
                id?.let { it1 -> (activity as GameActivity).callTransition(it1) }
            }
        }

        clearButton.setOnClickListener {
            context?.let { context1 ->
                MaterialAlertDialogBuilder(context1, R.style.Theme_WearableLearning_AlertDialog)
                    .setMessage(resources.getString(R.string.confirm_clear_text))
                    .setNegativeButton(resources.getString(R.string.no_text)) { dialog, _ ->
                        dialog.cancel()
                    }
                    .setPositiveButton(resources.getString(R.string.yes_text)) { _, _ ->
                        val containingView = view?.findViewById<LinearLayout>(R.id.linearLayout)
                        containingView.removeAllViews()

                        view?.findViewById<HorizontalScrollView>(R.id.scrollView).isScrollbarFadingEnabled =
                            false
                        rightIcon.setIconTintResource(R.color.lightgrey)
                        leftIcon.setIconTintResource(R.color.lightgrey)

                        colorBtnClicks = 0
                        colorSequence = ""
                    }
                    .show()
            }
        }

        redButton.setOnClickListener {
            colorBtnClicks++
            addBlockToSolution(greenButton, R.color.red, view)
            addKeywordToSequence("RD")
        }

        greenButton.setOnClickListener {
            colorBtnClicks++
            addBlockToSolution(greenButton, R.color.green, view)
            addKeywordToSequence("GR")
        }

        blueButton.setOnClickListener {
            colorBtnClicks++
            addBlockToSolution(blueButton, R.color.blue, view)
            addKeywordToSequence("BL")
        }

        blackButton.setOnClickListener {
            colorBtnClicks++
            addBlockToSolution(blackButton, R.color.black, view)
            addKeywordToSequence("BK")
        }
    }

    private fun addBlockToSolution(clickedButton: Button, color: Int, view: View) {
        var blockNum = colorBtnClicks

        var block = createBlock(blockNum)
        block.setBackgroundColor(ContextCompat.getColor(requireContext(), color))

        if(colorBtnClicks > 6) {
            var rightIcon = view.findViewById<MaterialButton>(R.id.question_temp_btn_right)
            rightIcon.setIconTintResource(R.color.darkgrey)

            view?.findViewById<HorizontalScrollView>(R.id.scrollView).isScrollbarFadingEnabled = false
        }
    }

    private fun createBlock(blockNum: Int): Button {
        var containingView = view?.findViewById<LinearLayout>(R.id.linearLayout)

        var scale = resources.displayMetrics.density
        var widthPixels = (47 * scale + 0.5f)
        var heightPixels = (50 * scale + 0.5f)
        var marginRightPixels = (4 * scale + 0.5f)

        var newBlock = Button(context)
        containingView?.addView(newBlock)

        newBlock.layoutParams.width = widthPixels.roundToInt()
        newBlock.layoutParams.height = heightPixels.roundToInt()
        newBlock.tag = "question_temp_btn_$blockNum"
        newBlock.text = ""

        val param = newBlock.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0, 0, marginRightPixels.roundToInt(), 0)
        newBlock.layoutParams = param

        return newBlock
    }

    private fun addKeywordToSequence(keyword: String) {
        if(colorSequence.isEmpty()) {
            colorSequence = keyword
        } else {
            colorSequence = "$colorSequence&$keyword"
        }
    }
}
