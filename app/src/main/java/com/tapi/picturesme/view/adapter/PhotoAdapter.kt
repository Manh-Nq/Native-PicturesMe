package com.tapi.picturesme.view.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tapi.picturesme.R
import com.tapi.picturesme.model.PhotoItem

class PhotoAdapter(
    val context: Context, var listData: List<PhotoItem>,
    private val onClickDownload: (PhotoItem) -> Unit,
    private val onClickItem: (PhotoItem) -> Unit
) :
    RecyclerView.Adapter<PhotoAdapter.PhotoHolder>() {

    inner class PhotoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivImage: ImageView = itemView.findViewById(R.id.iv_image)
        var ivDownload: ImageView = itemView.findViewById(R.id.iv_download)
        var widthView = itemView.getResources().getDisplayMetrics().widthPixels / 2


        fun onBind(item: PhotoItem) {
            Glide.with(context).load(R.drawable.ic_loading).into(ivImage)
            Glide.with(context).load(item.picture.full).centerCrop().override(widthView, widthView)
                .into(ivImage)
            ivDownload.setOnClickListener { onClickDownload(item) }
            ivImage.setOnClickListener { onClickItem(item) }

        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false)
        return PhotoHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        holder.onBind(listData[position])
    }

    override fun getItemCount(): Int {
        return listData.size
    }


}