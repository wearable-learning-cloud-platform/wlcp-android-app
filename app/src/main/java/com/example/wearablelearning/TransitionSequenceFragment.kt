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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** The _id_ is the current transition's id. */
        val id = this.requireArguments().getString("id")

        /** The _solution_ is the current transition's correct answer (i.e. expected user input). */
        val solution = this.requireArguments().getString("content")

        /** The _submitButton_ checks the current answer to _solution_. */
        val submitButton = view.findViewById<Button>(R.id.transition_submit_btn)

        /** The _clearButton_ clears the current answer completely (includes confirmation dialog). */
        val clearButton = view.findViewById<Button>(R.id.transition_clear_btn)

        /** 4 colored buttons to build sequence: red, green, blue, and black.*/
        val redButton = view.findViewById<Button>(R.id.question_red_btn)
        val greenButton = view.findViewById<Button>(R.id.question_green_btn)
        val blueButton = view.findViewById<Button>(R.id.question_blue_btn)
        val blackButton = view.findViewById<Button>(R.id.question_black_btn)

        /**
         * There is no max size of sequence. Thus, _scrollView_ allows the user to scroll left and
         * right in their inputted sequence. The _leftIcon_ denotes if there are more colors
         * to be seen left of the current view. The _rightIcon_ denotes if there are more colors
         * to be seen right of the current view.
         */
        val scrollView = view.findViewById<HorizontalScrollView>(R.id.scrollView)
        val leftIcon = view.findViewById<MaterialButton>(R.id.question_temp_btn_left)
        val rightIcon = view.findViewById<MaterialButton>(R.id.question_temp_btn_right)

        /**
         * The user scrolls left and right within the answer input space if the inputted sequence
         * is larger than the space. Icons indicate whether the user can scroll left or right.
         */
        scrollView.viewTreeObserver.addOnScrollChangedListener {
            val maxScrollX = scrollView.getChildAt(0).measuredWidth - scrollView.measuredWidth

            if(colorBtnClicks > 6) {
                //Scroll is at leftmost position (right icon on).
                if (scrollView.scrollX == 0) {
                    rightIcon.setIconTintResource(R.color.darkgrey)
                } else {
                    rightIcon.setIconTintResource(R.color.lightgrey)
                }

                //Scroll is at rightmost position (left icon on).
                if (scrollView.scrollX == maxScrollX) {
                    leftIcon.setIconTintResource(R.color.darkgrey)
                } else {
                    leftIcon.setIconTintResource(R.color.lightgrey)
                }

                //Scroll is somewhere between leftmost and rightmost positions (both icons on).
                if (scrollView.scrollX != 0 && scrollView.scrollX != maxScrollX) {
                    rightIcon.setIconTintResource(R.color.darkgrey)
                    leftIcon.setIconTintResource(R.color.darkgrey)
                }
            }
        }

        /**
         * The user's answer is compared to the solution. If correct, callTransition is called in
         * [GameActivity]. Otherwise, player does not move on.
         */
        submitButton.setOnClickListener {
            /**
             * Retrieve the [GameInfo] object from the intent that started [GameActivity].
             *
             * The _gameInfo_ is a [GameInfo] object and is used to track user input about the game
             * (e.g., gamePin, name, etc. - See the [GameInfo] class for all relevant fields).
             */
            val gameInfo = (activity as GameActivity).gameInfo

            gameInfo.interactionType = "submitButton"

            if(colorSequence == solution) {
                colorBtnClicks = 0
                colorSequence = ""
                id?.let { it1 -> (activity as GameActivity).callTransition(it1, false, solution, "color_sequence") }
            }
            else {
                gameInfo.currTransAnswer = colorSequence
                context?.let { context -> LogUtils.logGamePlay("player", (activity as GameActivity).gameInfo, false, context) }
                context?.let { context -> LogUtils.logGamePlay("gamePlay", (activity as GameActivity).gameInfo, false, context) }
            }
        }

        /**
         * A confirmation dialog is displayed to confirm the clearing of a solution space. If
         * confirmed, the user's current answer is erased. Otherwise, nothing happens to the user's
         * current answer.
         */
        clearButton.setOnClickListener {
            context?.let { context1 ->
                MaterialAlertDialogBuilder(context1, R.style.Theme_WearableLearning_AlertDialog)
                    .setMessage(resources.getString(R.string.confirm_clear_text))
                    .setNegativeButton(resources.getString(R.string.no_text)) { dialog, _ ->
                        dialog.cancel()
                    }
                    .setPositiveButton(resources.getString(R.string.yes_text)) { _, _ ->
                        val containingView = view.findViewById<LinearLayout>(R.id.linearLayout)
                        containingView.removeAllViews()

                        view.findViewById<HorizontalScrollView>(R.id.scrollView).isScrollbarFadingEnabled =
                            false
                        rightIcon.setIconTintResource(R.color.lightgrey)
                        leftIcon.setIconTintResource(R.color.lightgrey)

                        /**
                         * Retrieve the [GameInfo] object from the intent that started [GameActivity].
                         *
                         * The _gameInfo_ is a [GameInfo] object and is used to track user input about the game
                         * (e.g., gamePin, name, etc. - See the [GameInfo] class for all relevant fields).
                         */
                        val gameInfo = (activity as GameActivity).gameInfo

                        gameInfo.interactionType = "clearButton"
                        gameInfo.currTransAnswer = colorSequence
                        context?.let { context -> LogUtils.logGamePlay("player", (activity as GameActivity).gameInfo, false, context) }
                        context?.let { context -> LogUtils.logGamePlay("gamePlay", (activity as GameActivity).gameInfo, false, context) }

                        colorBtnClicks = 0
                        colorSequence = ""
                    }
                    .show()
            }
        }

        redButton.setOnClickListener {
            colorBtnClicks++
            addBlockToSolution(R.color.red, view)
            addKeywordToSequence("RD")
        }

        greenButton.setOnClickListener {
            colorBtnClicks++
            addBlockToSolution(R.color.green, view)
            addKeywordToSequence("GR")
        }

        blueButton.setOnClickListener {
            colorBtnClicks++
            addBlockToSolution(R.color.blue, view)
            addKeywordToSequence("BL")
        }

        blackButton.setOnClickListener {
            colorBtnClicks++
            addBlockToSolution(R.color.black, view)
            addKeywordToSequence("BK")
        }
    }

    /**
     * The [addBlockToSolution] function adds a colored block to the user's answer. For example,
     * if the user pressed the RED BUTTON, then this function will add a red block to the solution
     * space.
     * @param [color] The color of the pressed block.
     * @param [view] The view of the fragment.
     */
    private fun addBlockToSolution(color: Int, view: View) {
        val blockNum = colorBtnClicks

        val block = createBlock(blockNum)
        block.setBackgroundColor(ContextCompat.getColor(requireContext(), color))

        if(colorBtnClicks > 6) {
            val rightIcon = view.findViewById<MaterialButton>(R.id.question_temp_btn_right)
            rightIcon.setIconTintResource(R.color.darkgrey)

            view.findViewById<HorizontalScrollView>(R.id.scrollView).isScrollbarFadingEnabled = false
        }
    }

    /**
     * The [createBlock] function creates a block to be added in the solution space by adding an
     * non-clickable button with coordinates to the right of the last added block.
     * @param [blockNum] One more than the current number of blocks in the space before creating this block.
     */
    private fun createBlock(blockNum: Int): Button {
        val containingView = view?.findViewById<LinearLayout>(R.id.linearLayout)

        val scale = resources.displayMetrics.density
        val widthPixels = (47 * scale + 0.5f)
        val heightPixels = (50 * scale + 0.5f)
        val marginRightPixels = (4 * scale + 0.5f)

        val newBlock = Button(context)
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

    /**
     * The [addKeywordToSequence] function adds RD, BL, GR, or BK to the current solution string.
     * The current solution string _colorSequence_ is compared to _solution_ on submit.
     * @param [keyword] The abbreviated color (RD, BL, GR, or BK).
     */
    private fun addKeywordToSequence(keyword: String) {
        colorSequence = if(colorSequence.isEmpty()) {
            keyword
        } else {
            "$colorSequence&$keyword"
        }
    }
}
