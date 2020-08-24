package com.tapi.picturesme.functions.m001home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tapi.picturesme.R
import com.tapi.picturesme.functions.m001home.PhotoItemView

class PhotoAdapter(val context: Context) :
    ListAdapter<PhotoItemView, PhotoAdapter.PhotoHolder>(PhotoItemViewDiffUnit()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false)
        return PhotoHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        var photoItemView: PhotoItemView = getItem(position)

        if (!photoItemView.isDownloaded) {
            Glide.with(context).load(photoItemView.photoItem.picture.thumb)
                .override(holder.widthView, holder.widthView + 20).into(holder.ivImage)

        } else if (photoItemView.isDownloaded) {

            Glide.with(context).load(photoItemView.photoItem.picture.thumb)
                .override(holder.widthView, holder.widthView).into(holder.ivImage)
            holder.ivCircle.visibility = View.GONE
            holder.ivDownload.visibility = View.GONE
            holder.viewBg.visibility = View.GONE
            holder.progress.visibility = View.GONE
            holder.tvDownload.visibility = View.GONE
        }
        holder.ivImage.tag = photoItemView


    }



    inner class PhotoHolder(itemView: View) : RecyclerView.ViewHolder(itemView),

        View.OnClickListener {
        var ivImage: ImageView = itemView.findViewById(R.id.iv_image)
        var tvDownload = itemView.findViewById<TextView>(R.id.tv_loading)
        var progress = itemView.findViewById<ProgressBar>(R.id.progress_item)
        var ivCircle = itemView.findViewById<ImageView>(R.id.iv_circle)
        var viewBg = itemView.findViewById<View>(R.id.view_background)
        var ivDownload: ImageView = itemView.findViewById(R.id.iv_download)
        var widthView = itemView.resources.displayMetrics.widthPixels / 2

        init {
            ivDownload.setOnClickListener(this)
        }


        override fun onClick(p0: View) {
            val item = ivImage.tag as PhotoItemView
            when (p0.id) {
                R.id.iv_download -> downLoad(item)

            }
        }

        private fun downLoad(item: PhotoItemView) {
            tvDownload.visibility = View.GONE
            ivCircle.visibility = View.GONE
            progress.visibility = View.VISIBLE
            ivDownload.visibility = View.GONE
            callback.downLoad(item, progress, viewBg, ivCircle, ivDownload, tvDownload)
        }

    }

    private lateinit var callback: adapterListener

    fun setCallBackAdapterHome(event: adapterListener) {
        callback = event
    }

    interface adapterListener {
        fun downLoad(
            item: PhotoItemView,
            progress: ProgressBar,
            viewBg: View,
            ivCircle: ImageView,
            ivDownload: ImageView,
            tvDownload: TextView
        )

    }

}

class PhotoItemViewDiffUnit : DiffUtil.ItemCallback<PhotoItemView>() {
    override fun areItemsTheSame(oldItem: PhotoItemView, newItem: PhotoItemView): Boolean {
        return oldItem.photoItem == newItem.photoItem
    }

    override fun areContentsTheSame(oldItem: PhotoItemView, newItem: PhotoItemView): Boolean {
        return oldItem.photoItem.picture == oldItem.photoItem.picture
    }

}
