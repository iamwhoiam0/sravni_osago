package com.example.osagosravni.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.osagosravni.R
import com.example.osagosravni.data.entities.Factor
import com.example.osagosravni.data.entities.OffersData
import com.example.osagosravni.data.entities.RationDetailsData
import com.example.osagosravni.databinding.FragmentPolicyPriceBinding
import com.example.osagosravni.presentation.adapter.CoefficientAdapter
import com.example.osagosravni.presentation.adapter.InsuranceCompanyAdapter
import com.example.osagosravni.presentation.viewModel.PolicePriceViewModel
import com.example.osagosravni.utils.CharacterItemDecoration
import com.example.osagosravni.utils.AdapterClickable
import com.example.osagosravni.utils.Constants.FIRST_ELEMENT
import com.example.osagosravni.utils.Constants.LAST_ELEMENT
import com.example.osagosravni.utils.NetworkResultHandler
import org.koin.androidx.viewmodel.ext.android.viewModel

class PolicyPriceFragment : Fragment(), AdapterClickable, NetworkResultHandler {

    private var _binding: FragmentPolicyPriceBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<PolicyPriceFragmentArgs>()
    private var myCoefficientData: RationDetailsData? = null

    private lateinit var insuranceCompanyAdapter: InsuranceCompanyAdapter
    private lateinit var coefficientAdapter: CoefficientAdapter

    private val viewModel: PolicePriceViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPolicyPriceBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myCoefficientData = args.myCoefficient
        myCoefficientData?.let { updateCoefficients(it.factors) }
        setupAdapter()

        binding.expandLayout.setOnClickListener {
            if (binding.coefficientRv.visibility == View.VISIBLE) {
                binding.coefficientRv.visibility = View.GONE
                binding.expandImgBtn.setImageResource(R.drawable.ic_expand_more_inactive)
            } else {
                binding.coefficientRv.visibility = View.VISIBLE
                binding.expandImgBtn.setImageResource(R.drawable.ic_expand_more_active)
            }
        }

        binding.backBtn.setOnClickListener {
            view.findNavController().popBackStack()
        }

        lifecycleScope.launchWhenStarted {
            binding.shimmerFl.visibility = View.VISIBLE
            binding.shimmerFl.startShimmer()
            binding.progressBar.visibility = View.VISIBLE
            binding.calculatePriceBtn.text = ""
            val result = viewModel.getInsuranceCompanyData()
            handleCharactersResult(result)
        }
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

    private fun setupAdapter(){
        insuranceCompanyAdapter = InsuranceCompanyAdapter(this)
        binding.insuranceCompaniesRv.layoutManager = LinearLayoutManager(requireActivity())
        binding.insuranceCompaniesRv.adapter = insuranceCompanyAdapter
        binding.insuranceCompaniesRv.addItemDecoration(CharacterItemDecoration(16))

        coefficientAdapter = CoefficientAdapter()
        myCoefficientData?.let { coefficientAdapter.submitList(it) }
        binding.coefficientRv.layoutManager = LinearLayoutManager(requireActivity())
        binding.coefficientRv.adapter = coefficientAdapter
        binding.coefficientRv.addItemDecoration(CharacterItemDecoration(16))
        binding.coefficientRv.hasFixedSize()
    }

    override fun onItemClick(position: Int) {}

    override fun handleError(errorMessage: String?) {
        binding.calculatePriceBtn.isEnabled = false
        binding.calculatePriceBtn.text = context?.getString(R.string.calculate_price)
        binding.progressBar.visibility = View.GONE
        Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun <T> handleSuccess(data: T) {
        if (data is OffersData){
            insuranceCompanyAdapter.submitList(data)
        }
        binding.shimmerFl.stopShimmer()
        binding.shimmerFl.visibility = View.GONE
        binding.insuranceCompaniesRv.visibility = View.VISIBLE
        binding.calculatePriceBtn.isEnabled = true
        binding.calculatePriceBtn.text = context?.getString(R.string.calculate_price)
        binding.progressBar.visibility = View.GONE
    }

    override fun showEmptyView() {
        TODO("Not yet implemented")
    }

}