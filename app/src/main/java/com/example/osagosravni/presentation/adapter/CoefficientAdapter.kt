package com.example.osagosravni.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.osagosravni.R
import com.example.osagosravni.data.entities.Factor
import com.example.osagosravni.data.entities.RationDetailsData
import com.example.osagosravni.databinding.MyCoefficientItemBinding

class CoefficientAdapter : RecyclerView.Adapter<CoefficientAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Factor>() {
        override fun areContentsTheSame(oldItem: Factor, newItem: Factor): Boolean {
            return oldItem.value == newItem.value
        }

        override fun areItemsTheSame(oldItem: Factor, newItem: Factor): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val binding = MyCoefficientItemBinding.bind(itemView)

        fun bind(factor: Factor) {
            binding.title.text = factor.title
            binding.name.text = factor.name
            binding.value.text = factor.value
            binding.detailText.text = factor.detailText
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_coefficient_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(data: RationDetailsData){
        differ.submitList(data.factors)
    }

}