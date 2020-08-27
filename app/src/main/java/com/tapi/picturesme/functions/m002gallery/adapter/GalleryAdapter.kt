package com.tapi.picturesme.functions.m002gallery.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tapi.picturesme.R
import com.tapi.picturesme.core.database.entity.PhotoEntity

class GalleryAdapter(val context: Context) :
    ListAdapter<PhotoEntity, GalleryAdapter.GalleryHolder>(GalleryDiffUnit()) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryHolder {
        var item: View = LayoutInflater.from(context).inflate(R.layout.item_gallery, parent, false)
        return GalleryHolder(item)
    }

    override fun onBindViewHolder(holder: GalleryHolder, position: Int) {
        var photoEntity = getItem(position)

        Glide.with(context).load(photoEntity.path).centerCrop().override(holder.width, holder.width)
            .into(holder.ivImage)

        holder.ivImage.tag = photoEntity
    }



    inner class GalleryHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var ivImage = itemView.findViewById<ImageView>(R.id.iv_gallery)
        var width = itemView.resources.displayMetrics.widthPixels / 2
        var view = itemView
        init {
            ivImage.setOnClickListener(this)
        }



        override fun onClick(p0: View?) {
            val item = ivImage.tag as PhotoEntity
            if (p0 != null) {
                when (p0.id) {
                    R.id.iv_gallery -> showDetail(item)
                }
            }
        }

        private fun showDetail(item: PhotoEntity) {
            callback.showDetail(item)
        }


    }

    private lateinit var callback: clickItemListener

    fun setClickItemListener(event: clickItemListener) {
        callback = event
    }

    interface clickItemListener {
        fun showDetail(data: PhotoEntity)
    }
}


class GalleryDiffUnit : DiffUtil.ItemCallback<PhotoEntity>() {
    override fun areItemsTheSame(oldItem: PhotoEntity, newItem: PhotoEntity): Boolean {
        return oldItem.path == newItem.path
    }

    override fun areContentsTheSame(oldItem: PhotoEntity, newItem: PhotoEntity): Boolean {
        TODO("Not yet implemented")
    }

}