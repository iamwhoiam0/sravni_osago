package com.example.osagosravni.data.entities

data class OffersData(
    val actionText: String,
    val offers: List<Offer>
)

data class Offer(
    val branding: Branding,
    val name: String,
    val price: Int,
    val rating: Double
)

data class Branding(
    val backgroundColor: String,
    val bankLogoUrlPDF: String?,
    val bankLogoUrlSVG: String?,
    val fontColor: String,
    val iconTitle: String,
    val name: String
)