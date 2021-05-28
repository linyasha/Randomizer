package com.rsschool.android2021

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

class FirstFragment : Fragment() {

    interface ActionPerformedListenerFragment1 {
        fun onActionPerformedFragment1(min: Int, max: Int)
    }

    //Variables
    private var generateButton: Button? = null
    private var previousResult: TextView? = null
    private var minEditText: EditText? = null
    private var maxEditText: EditText? = null
    private var listener: ActionPerformedListenerFragment1? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as ActionPerformedListenerFragment1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("saveState", "HelloCreatedView")
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
        var min: Int = 0
        var max: Int = 0
        val minLast: Int? = arguments?.getInt(MIN_VALUE)
        val maxLast: Int? = arguments?.getInt(MAX_VALUE)
        //TODO: Поправить костыль
        if(maxLast != 0) {
            minEditText?.setText(minLast.toString())
            maxEditText?.setText(maxLast.toString())
            min = minLast!!
            max = maxLast!!
        }
        if(maxLast == -1){
            minEditText?.setText("0")
            maxEditText?.setText("0")
            min = 0
            max = 0
        }

        generateButton?.isEnabled = (minEditText?.text?.isNotEmpty() == true && maxEditText?.text?.isNotEmpty() == true
                && minEditText?.error == null && minEditText?.error == null)
            //Log.d("saveState", "HelloViewCreated!!!!!!")

        val inputTextWatcher: TextWatcher = object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("saveState", "Hello TextChanges")
                // TODO: Установить выдачу ошибок у EditText при некорректных значениях
                val minString: String = minEditText?.text.toString()
                val maxString: String = maxEditText?.text.toString()
                if (minString.isNotEmpty() && maxString.isNotEmpty()) {


                    min = try { minString.toInt() } catch (e: NumberFormatException) { -1 }
                    max = try { maxString.toInt() } catch (e: NumberFormatException) { -1 }

                    generateButton?.isEnabled = max >= min && max < Int.MAX_VALUE && min >= 0 && max >= 0
                }
                else {
                    generateButton?.isEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        minEditText?.addTextChangedListener(inputTextWatcher)
        maxEditText?.addTextChangedListener(inputTextWatcher)

        generateButton?.setOnClickListener {
            listener?.onActionPerformedFragment1(min, max)
        }
    }

    companion object {

        @JvmStatic
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
    }
}