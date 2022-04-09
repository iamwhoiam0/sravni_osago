package com.example.osagosravni.presentation.view

import android.app.Dialog
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import com.example.osagosravni.R
import com.example.osagosravni.databinding.FragmentBottomSheetBinding
import com.example.osagosravni.presentation.viewModel.CalculatorViewModel
import com.example.osagosravni.utils.BottomSheetType
import com.example.osagosravni.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.view.MotionEvent
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View.OnTouchListener
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import android.widget.EditText
import androidx.navigation.fragment.navArgs
import com.example.osagosravni.utils.Constants.CURRENT_POSITION
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlin.properties.Delegates

class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CalculatorViewModel by viewModel()
    private var currentType: MutableLiveData<BottomSheetType> = MutableLiveData()
    private val firstElement = 0
    private val lastElement = 5
    private var newList: ArrayList<String>? = arrayListOf()

    private val args by navArgs<BottomSheetFragmentArgs>()
    private var currentPosition by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val dialog = super.onCreateDialog(savedInstanceState)
            if (dialog is BottomSheetDialog) {
                dialog.behavior.skipCollapsed = true
                dialog.behavior.state = STATE_EXPANDED
            }
            return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val hintList = listOf(
            context?.getString(R.string.hint_city_registration),
            context?.getString(R.string.hint_power),
            context?.getString(R.string.hint_how_many_drivers),
            context?.getString(R.string.hint_age),
            context?.getString(R.string.hint_min_experience),
            context?.getString(R.string.hint_no_accidents)
        )

        newList = viewModel.editTextList.value

        currentPosition = savedInstanceState?.getInt(CURRENT_POSITION, 0) ?: args.position

        binding.titleTv.text = hintList[currentPosition]
        binding.inputEt.setText(newList?.get(currentPosition))

        determineTheType()

        binding.nextBtn.setOnClickListener {
            newList?.set(currentPosition, binding.inputEt.text.toString())
            if (currentPosition<lastElement){
                currentPosition++
                determineTheType()
                binding.inputEt.showKeyboard()
            }else{
                dismiss()
            }
        }

        binding.backBtn.setOnClickListener {
            newList?.set(currentPosition, binding.inputEt.text.toString())
            currentPosition--
            determineTheType()
            binding.inputEt.showKeyboard()
        }

        currentType.observe(viewLifecycleOwner, { type ->
                binding.titleTv.text = hintList[currentPosition]
                binding.inputEt.setText(newList?.get(currentPosition))
                when(type){
                    BottomSheetType.FIRST->{
                        binding.inputEt.inputType = InputType.TYPE_CLASS_TEXT
                        binding.backBtn.visibility = View.INVISIBLE
                        binding.nextBtn.text = context?.resources?.getString(R.string.next_btn)
                        binding.nextBtn.setCompoundDrawables(null, null, null, null)
                    }
                    BottomSheetType.MIDDLE->{
                        binding.inputEt.inputType = InputType.TYPE_CLASS_NUMBER
                        binding.backBtn.visibility = View.VISIBLE
                        binding.nextBtn.text = context?.resources?.getString(R.string.next_btn)
                        binding.nextBtn.setCompoundDrawables(null, null, null, null)
                    }
                    BottomSheetType.LAST ->{
                        binding.inputEt.inputType = InputType.TYPE_CLASS_NUMBER
                        binding.backBtn.visibility = View.VISIBLE
                        binding.nextBtn.text = context?.resources?.getString(R.string.confirm_btn)
                        binding.nextBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_right_arrow, 0)
                    }
                    else->{}
                }
            }
        )

        clearTouchListener()
        editTextListener()
    }

    private fun editTextListener() {
        binding.inputEt.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if(s.count() == 0){
                    binding.inputEt.setCompoundDrawables(null, null, null, null)
                }else{
                    binding.inputEt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cross, 0)
                }
            }
        })
    }

    private fun clearTouchListener() {
        binding.inputEt.setOnTouchListener(OnTouchListener { v, event ->
            val drawable: Drawable =
                binding.inputEt.compoundDrawables[2] ?: return@OnTouchListener false // Если изображения справа нет, обработка прекращается
            if (event.action != MotionEvent.ACTION_UP) return@OnTouchListener false // Если это не пресс-событие, больше не обрабатывать его
            if (event.x > (binding.inputEt.width
                        - binding.inputEt.paddingRight
                        - drawable.intrinsicWidth)
            ) {
                binding.inputEt.text.clear()
            }
            false
        })
    }

    override fun onResume() {
        super.onResume()

        binding.inputEt.showKeyboard()
    }

    private fun EditText.showKeyboard() {
        if (requestFocus()) {
            (activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
                .showSoftInput(this, SHOW_IMPLICIT)
            setSelection(text.length)
        }
    }

    private fun determineTheType(){
        currentType.value = when(currentPosition){
            firstElement -> BottomSheetType.FIRST
            lastElement -> BottomSheetType.LAST
            else -> BottomSheetType.MIDDLE
        }
    }

    override fun onPause() {
        super.onPause()

        newList?.let {
            it[currentPosition] = binding.inputEt.text.toString()
            viewModel.updateEditTextList(it)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(CURRENT_POSITION, currentPosition)
    }

}