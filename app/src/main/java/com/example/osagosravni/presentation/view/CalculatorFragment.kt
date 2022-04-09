package com.example.osagosravni.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.osagosravni.utils.AdapterClickable
import com.example.osagosravni.R
import com.example.osagosravni.data.entities.Factor
import com.example.osagosravni.data.entities.RationDetailsData
import com.example.osagosravni.databinding.FragmentCalculatorBinding
import com.example.osagosravni.presentation.adapter.CoefficientAdapter
import com.example.osagosravni.presentation.adapter.InputInfoAdapter
import com.example.osagosravni.presentation.viewModel.CalculatorViewModel
import com.example.osagosravni.utils.*
import com.example.osagosravni.utils.Constants.FIRST_ELEMENT
import com.example.osagosravni.utils.Constants.LAST_ELEMENT
import org.koin.androidx.viewmodel.ext.android.viewModel

class CalculatorFragment : Fragment(), AdapterClickable, NetworkResultHandler {

    private var _binding: FragmentCalculatorBinding? = null

    private val binding get() = _binding!!

    private val viewModel: CalculatorViewModel by viewModel()

    private lateinit var inputInfoAdapter: InputInfoAdapter
    private lateinit var coefficientAdapter: CoefficientAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalculatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()

        viewModel.editTextList.observe(viewLifecycleOwner, { editText ->
                inputInfoAdapter.submitList(editText)
                inputInfoAdapter.notifyDataSetChanged() // Надо как то избавиться от этого :)
                if (listIsFull(editText)) {   // проверка на отсутствие пустых полей
                    lifecycleScope.launchWhenStarted {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.calculateBtn.text = ""
                        val result = viewModel.getCoefficientData()
                        handleCharactersResult(result)
                    }
                }else{
                    binding.calculateBtn.isEnabled = false
                }
            }
        )

        binding.expandLayout.setOnClickListener {
            if (binding.coefficientRv.visibility == View.VISIBLE) {
                binding.coefficientRv.visibility = View.GONE
                binding.expandImgBtn.setImageResource(R.drawable.ic_expand_more_inactive)
            } else {
                binding.coefficientRv.visibility = View.VISIBLE
                binding.expandImgBtn.setImageResource(R.drawable.ic_expand_more_active)
            }
        }

        binding.calculateBtn.setOnClickListener {
            navigate(CalculatorFragmentDirections.actionCalculatorFragmentToPolicyPriceFragment(viewModel.coefficientData.value))
        }
    }

    private fun listIsFull(list:ArrayList<String>):Boolean{
        return !list.any { it.isEmpty() }
    }


    private fun updateCoefficients(factors: ArrayList<Factor>) {
        var newValue = ""
        for (i in FIRST_ELEMENT..LAST_ELEMENT){
            newValue += if(i == LAST_ELEMENT){
                factors[i].headerValue
            } else{
                factors[i].headerValue + "x"
            }
        }
        binding.coefficientValue.text = newValue
    }

    private fun setupAdapter() {
        inputInfoAdapter = InputInfoAdapter(this)
        binding.inputRv.layoutManager = LinearLayoutManager(requireActivity())
        binding.inputRv.adapter = inputInfoAdapter
        binding.inputRv.addItemDecoration(CharacterItemDecoration(12))
        binding.inputRv.hasFixedSize()

        coefficientAdapter = CoefficientAdapter()
        coefficientAdapter.submitList(getCoefficientInitData())
        binding.coefficientRv.layoutManager = LinearLayoutManager(requireActivity())
        binding.coefficientRv.adapter = coefficientAdapter
        binding.coefficientRv.addItemDecoration(CharacterItemDecoration(16))
        binding.coefficientRv.hasFixedSize()
    }

    override fun onItemClick(position: Int) {
        navigate(CalculatorFragmentDirections.actionCalculatorFragmentToBottomSheetFragment(position))
    }

    private fun Fragment.navigate(directions: NavDirections) {
        val controller = findNavController()
        val currentDestination = (controller.currentDestination as? FragmentNavigator.Destination)?.className
            ?: (controller.currentDestination as? DialogFragmentNavigator.Destination)?.className
        if (currentDestination == this.javaClass.name) {
            controller.navigate(directions)
        }
    }

    override fun handleError(errorMessage: String?){
        binding.calculateBtn.isEnabled = false
        binding.calculateBtn.text = context?.getString(R.string.calculate_osago)
        binding.progressBar.visibility = View.GONE
        Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun <T> handleSuccess(data: T) {
        if (data is RationDetailsData){
            updateCoefficients(data.factors)
            coefficientAdapter.submitList(data)
        }
        binding.calculateBtn.isEnabled = true
        binding.calculateBtn.text = context?.getString(R.string.calculate_osago)
        binding.progressBar.visibility = View.GONE
    }

    override fun showEmptyView(){
        // TODO()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCoefficientInitData(): RationDetailsData{
        return RationDetailsData(
            arrayListOf(
                Factor(
                    getString(R.string.calculator_1_coefficient_message),
                    getString(R.string.calculator_1_coefficient_header),
                    getString(R.string.calculator_1_coefficient_name),
                    getString(R.string.calculator_1_coefficient_title),
                    getString(R.string.calculator_1_coefficient_value)
                ),
                Factor(
                    getString(R.string.calculator_2_coefficient_message),
                    getString(R.string.calculator_2_coefficient_header),
                    getString(R.string.calculator_2_coefficient_name),
                    getString(R.string.calculator_2_coefficient_title),
                    getString(R.string.calculator_2_coefficient_value)
                ),
                Factor(
                    getString(R.string.calculator_3_coefficient_message),
                    getString(R.string.calculator_3_coefficient_header),
                    getString(R.string.calculator_3_coefficient_name),
                    getString(R.string.calculator_3_coefficient_title),
                    getString(R.string.calculator_3_coefficient_value)
                ),
                Factor(
                    getString(R.string.calculator_4_coefficient_message),
                    getString(R.string.calculator_4_coefficient_header),
                    getString(R.string.calculator_4_coefficient_name),
                    getString(R.string.calculator_4_coefficient_title),
                    getString(R.string.calculator_4_coefficient_value)
                ),
                Factor(
                    getString(R.string.calculator_5_coefficient_message),
                    getString(R.string.calculator_5_coefficient_header),
                    getString(R.string.calculator_5_coefficient_name),
                    getString(R.string.calculator_5_coefficient_title),
                    getString(R.string.calculator_5_coefficient_value)
                ),
                Factor(
                    getString(R.string.calculator_6_coefficient_message),
                    getString(R.string.calculator_6_coefficient_header),
                    getString(R.string.calculator_6_coefficient_name),
                    getString(R.string.calculator_6_coefficient_title),
                    getString(R.string.calculator_6_coefficient_value)
                ),
            )
        )
    }
}