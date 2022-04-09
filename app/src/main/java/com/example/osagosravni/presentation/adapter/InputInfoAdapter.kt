package com.example.osagosravni.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.osagosravni.R
import com.example.osagosravni.databinding.InputItemBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.example.osagosravni.utils.AdapterClickable
import com.example.osagosravni.utils.Constants


class InputInfoAdapter(
    private val itemClickListener: AdapterClickable
): RecyclerView.Adapter<InputInfoAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val binding = InputItemBinding.bind(itemView)

        private val context: Context = itemView.context

        private val hintList = listOf(
            context.getString(R.string.hint_city_registration),
            context.getString(R.string.hint_power),
            context.getString(R.string.hint_how_many_drivers),
            context.getString(R.string.hint_age),
            context.getString(R.string.hint_min_experience),
            context.getString(R.string.hint_no_accidents)
        )

        fun bind(inputItem:String, position: Int){
            binding.textInputLayout.hint = hintList[position]
            binding.editText.setText(inputItem)
            binding.editText.setOnClickListener { itemClickListener.onItemClick(position) }
        }
    }


    private val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.input_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position], position)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: ArrayList<String>){
        differ.submitList(list)
    }

}