package com.rsschool.android2021

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment

class FirstFragment : Fragment() {

    //Communication with activity
    interface ActionPerformedListenerFragment1 {
        fun onActionPerformedFragment1(min: Int, max: Int)
    }

    //Variables
    private var generateButton: Button? = null
    private var previousResult: TextView? = null
    private var minEditText: EditText? = null
    private var maxEditText: EditText? = null
    private var min: Int = 0
    private var max: Int = 0
    private var listener: ActionPerformedListenerFragment1? = null

    //Attach listener to MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as ActionPerformedListenerFragment1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        previousResult = view.findViewById(R.id.previous_result)
        val result = arguments?.getInt(PREVIOUS_RESULT_KEY)
        previousResult?.text = "Previous result: ${result.toString()}"

        generateButton = view.findViewById(R.id.generate)
        minEditText = view.findViewById(R.id.min_value)
        maxEditText = view.findViewById(R.id.max_value)

        //Last values when user switch on the second fragment
        val minLast: Int = arguments?.getInt(MIN_VALUE) ?: 0
        val maxLast: Int = arguments?.getInt(MAX_VALUE) ?: 0
        when {
            maxLast == ZERO_CASE -> {
                minEditText?.setText("0")
                maxEditText?.setText("0")
            }
             maxLast > 0 -> {
                 minEditText?.setText(minLast.toString())
                 maxEditText?.setText(maxLast.toString())
                 min = minLast
                 max = maxLast
            }
        }
        //Change enabled button to false if editText have some errors or isEmpty
        generateButton?.isEnabled = (minEditText?.text?.isNotEmpty() == true && maxEditText?.text?.isNotEmpty() == true
                && minEditText?.error == null && minEditText?.error == null)

        //Logic when user change EditText field
        val inputTextWatcher: TextWatcher = object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val minString: String = minEditText?.text.toString()
                val maxString: String = maxEditText?.text.toString()
                //Checking each of the fields for going out of bounds
                min = checkStringNumber(minString, minEditText)
                max = checkStringNumber(maxString, maxEditText)
                if (minString.isNotEmpty() && maxString.isNotEmpty()) {
                    if(max < min) {
                        if(maxEditText?.error != NUMBER_EXCEEDS_RANGE) maxEditText?.error = MAX_NUMBER_LESS_THAN_MIN
                    }
                    else {
                        maxEditText?.error = null
                    }
                    generateButton?.isEnabled = minEditText?.error == null && maxEditText?.error == null
                }
                else {
                    generateButton?.isEnabled = false
                }
            }

            private fun checkStringNumber(needString: String, needEditText: EditText?): Int {
                var result: Int
                if (needString.isNotEmpty()) {
                    var enterOfLimit: Boolean = true
                    try {
                        result = needString.toInt()
                        needEditText?.error = null
                    }
                    catch (e: NumberFormatException) {
                        enterOfLimit = false
                        result = 0
                    }
                    if(!enterOfLimit || result < 0) {
                        needEditText?.error = NUMBER_EXCEEDS_RANGE
                        return 0
                    }
                }
                else {
                    needEditText?.error = null
                    result = 0
                }
                return result
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        minEditText?.addTextChangedListener(inputTextWatcher)
        maxEditText?.addTextChangedListener(inputTextWatcher)

        generateButton?.setOnClickListener {
            listener?.onActionPerformedFragment1(min, max)
        }
    }

    companion object {
        @JvmStatic   // For using FirstFragment.newInstance in Java class but not FirstFragment.Companion.newInstance()
        fun newInstance(previousResult: Int, minValue: Int, maxValue: Int): FirstFragment {
            val fragment = FirstFragment()
            val args = Bundle()
            args.putInt(PREVIOUS_RESULT_KEY, previousResult)
            args.putInt(MIN_VALUE, minValue)
            args.putInt(MAX_VALUE, maxValue)
            fragment.arguments = args
            return fragment
        }

        private const val PREVIOUS_RESULT_KEY = "PREVIOUS_RESULT"
        private const val MIN_VALUE = "MIN_VALUE"
        private const val MAX_VALUE = "MAX_VALUE"
        private const val NUMBER_EXCEEDS_RANGE = "Number is too large"
        private const val MAX_NUMBER_LESS_THAN_MIN = "Maximum limit is less than minimum"
        private const val ZERO_CASE = -1
    }
}