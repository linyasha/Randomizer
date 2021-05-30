package com.rsschool.android2021

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

class SecondFragment : Fragment() {

    //Communication with Activity
    interface ActionPerformedListenerFragment2 {
        fun onActionPerformedFragment2(result: Int, min: Int, max: Int)
    }

    //Variables
    private var backButton: Button? = null
    private var result: TextView? = null
    private var resultInt: Int = 0
    private var min: Int = 0
    private var max: Int = 0
    private var listener: SecondFragment.ActionPerformedListenerFragment2? = null

    //Attach listener to MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as SecondFragment.ActionPerformedListenerFragment2
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        result = view.findViewById(R.id.result)
        backButton = view.findViewById(R.id.back)
        val min = arguments?.getInt(MIN_VALUE_KEY) ?: 0
        val max = arguments?.getInt(MAX_VALUE_KEY) ?: 0
        //Don't generate new random number when screen location changed
        if(savedInstanceState == null) {
            if(min == max) {
                result?.text = "$min"
                resultInt = min
            }
            else {
                resultInt = generate(min, max)
                result?.text = resultInt.toString()
            }
        }
        else {
            resultInt = savedInstanceState.getInt(PREVIOUS_RANDOM_NUMBER_KEY)
            result?.text = resultInt.toString()
        }

        backButton?.setOnClickListener {
            if(max == 0) {
                listener?.onActionPerformedFragment2(resultInt, -1, -1)
            }
            else {
                listener?.onActionPerformedFragment2(resultInt, min, max)
            }

        }

        //Override backward button with my logic
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(max == 0) {
                    listener?.onActionPerformedFragment2(resultInt, -1, -1)
                }
                else {
                    listener?.onActionPerformedFragment2(resultInt, min, max)
                }

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callback)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(PREVIOUS_RANDOM_NUMBER_KEY, resultInt)
        super.onSaveInstanceState(outState)
    }

    private fun generate(min: Int, max: Int): Int {
        return (min..max).random()
    }

    companion object {
        @JvmStatic
        fun newInstance(min: Int, max: Int): SecondFragment {
            val fragment = SecondFragment()
            val args = Bundle()
            args.putInt(SecondFragment.MIN_VALUE_KEY, min)
            args.putInt(SecondFragment.MAX_VALUE_KEY, max)
            fragment.arguments = args
            return fragment
        }

        private const val MIN_VALUE_KEY = "MIN_VALUE"
        private const val MAX_VALUE_KEY = "MAX_VALUE"
        private const val PREVIOUS_RANDOM_NUMBER_KEY = "PREVIOUS_RANDOM_NUMBER"
        private const val ZERO_CASE = -1
    }
}