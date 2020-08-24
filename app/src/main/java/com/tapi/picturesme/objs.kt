package com.tapi.picturesme

import com.google.gson.annotations.SerializedName

data class PhotoItem(
    @SerializedName("id") val id: String,
    @SerializedName("created_at") val created :String,
    @SerializedName("updated_at") val updated: String,
    @SerializedName("urls") val picture: Picture
) {

}

data class Picture(
    @SerializedName("raw")
    val raw: String,

    @SerializedName("full")
    val full: String,

    @SerializedName("regular")
    val regular: String,

    @SerializedName("small")
    val small: String,

    @SerializedName("thumb")
    val thumb: String
) {
    override fun toString(): String {
        return "Picture(raw='$raw', full='$full', regular='$regular', small='$small', thumb='$thumb')"
    }
}



