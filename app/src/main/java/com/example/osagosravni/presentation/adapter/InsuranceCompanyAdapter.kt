package com.example.osagosravni.presentation.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.osagosravni.R
import com.example.osagosravni.data.entities.Offer
import com.example.osagosravni.data.entities.OffersData
import com.example.osagosravni.databinding.InsuranceCompanyItemBinding
import com.example.osagosravni.utils.AdapterClickable
import java.text.NumberFormat
import java.util.*

class InsuranceCompanyAdapter(
    private val itemClickListener: AdapterClickable
): RecyclerView.Adapter<InsuranceCompanyAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Offer>() {
        override fun areContentsTheSame(oldItem: Offer, newItem: Offer): Boolean {
            return (
                oldItem.price == newItem.price ||
                oldItem.name == newItem.name ||
                oldItem.rating == newItem.rating ||
                oldItem.branding.fontColor == newItem.branding.fontColor // Пока так, в идеале прописать все думаю
            )
        }

        override fun areItemsTheSame(oldItem: Offer, newItem: Offer): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val binding = InsuranceCompanyItemBinding.bind(itemView)

        fun bind(offer: Offer) {
            binding.nameTv.text = offer.name
            binding.priceTv.text = offer.price.convertPrice()
            binding.ratingTv.text = offer.rating.toString()
            if (offer.branding.bankLogoUrlSVG != null){
                binding.iconImg.backgroundTintList = null
                loadSvg(offer.branding.bankLogoUrlSVG, binding.iconImg)
            }else{
                binding.iconImg.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#" + offer.branding.backgroundColor))
                binding.iconTv.text = offer.branding.iconTitle
                binding.iconTv.setTextColor(Color.parseColor("#" + offer.branding.fontColor))
                binding.iconTv.visibility = View.VISIBLE
            }
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InsuranceCompanyAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.insurance_company_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: InsuranceCompanyAdapter.ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(data: OffersData){
        differ.submitList(data.offers)
    }

    private fun Int.convertPrice():String{
        return NumberFormat.getNumberInstance(Locale.US)
            .format(this)
            .replace(",", " ") + " ₽"
    }

    fun loadSvg(urlSvg: String, iconImg: ImageView) {
        val imageLoader = ImageLoader.Builder(iconImg.context)
            .componentRegistry {
                add(SvgDecoder(iconImg.context))
            }
            .build()

        val request = ImageRequest.Builder(iconImg.context)
            .data(urlSvg)
            .target(iconImg)
            .build()
        imageLoader.enqueue(request)
    }
}