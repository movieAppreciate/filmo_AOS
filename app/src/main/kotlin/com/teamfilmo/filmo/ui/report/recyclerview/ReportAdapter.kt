package com.teamfilmo.filmo.ui.report.recyclerview

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.ui.model.ReportList

class ReportAdapter() : RecyclerView.Adapter<ReportAdapter.ReviewViewHolder>() {
    lateinit var reportList: ArrayList<ReportList>

    interface ItemClick {
        fun onClick(
            view: View,
            position: Int,
        )

        fun onLikeClick(position: Int)
    }

    var itemClick: ItemClick? = null

    fun setReportInfo(
        reportList: ArrayList<ReportList>,
    ) {
        this.reportList = reportList
        Log.d("좋아요 어댑터 setReportInfo", "호출 ${reportList.get(0).isLiked}")
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ReviewViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(
                R.layout.movie_item,
                parent,
                false,
            )
        return ReviewViewHolder(v)
    }

    override fun onBindViewHolder(
        holder: ReviewViewHolder,
        position: Int,
    ) {
        Log.d("좋아요 어댑터 그냥 onBindViewHolder", "호출")
        if (itemClick != null) {
            holder.itemView.findViewById<ImageView>(R.id.movie_image).setOnClickListener {
                    v ->
                itemClick?.onClick(v, position)
            }
        }
        holder.bindItems(reportList[position])
        holder.bindLikeImage(reportList[position].isLiked)
    }

    override fun onBindViewHolder(
        holder: ReviewViewHolder,
        position: Int,
        payloads: MutableList<Any>,
    ) {
        if (payloads.isEmpty()) {
            Log.d("좋아요 어댑터 payload empty", "empty")
            super.onBindViewHolder(holder, position, payloads)
        } else {
            payloads.forEach {
                if (it.toString() == "true") {
                    Log.d("좋아요 어댑터 payload true", it.toString())
                    this.reportList[position].isLiked = true
                    holder.bindLikeButton(R.drawable.ic_like_selected)
                } else if (it.toString() == "false") {
                    this.reportList[position].isLiked = false
                    Log.d("좋아요 어댑터 payload false", it.toString())
                    holder.bindLikeButton(R.drawable.ic_like)
                } else {
                    holder.bindLikeCount(it.toString())
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return reportList.size
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val likeButton: ImageButton = itemView.findViewById(R.id.likeButton)

        init {
            likeButton.setOnClickListener {
                itemClick?.onLikeClick(adapterPosition)
            }
        }

        fun bindItems(item: ReportList) {
            val title = itemView.findViewById<TextView>(R.id.tv_title)
            val subTitle = itemView.findViewById<TextView>(R.id.tv_subtitle)
            val replyCount = itemView.findViewById<TextView>(R.id.tv_reply_count)
            val nickName = itemView.findViewById<TextView>(R.id.tv_nick_name)

            title.text = item.title
            subTitle.text = item.content
            replyCount.text = item.replyCount.toString()
            nickName.text = item.nickname
        }

        fun bindLikeButton(imageSrc: Int) {
            likeButton.setImageResource(imageSrc)
        }

        fun bindLikeCount(count: String) {
            val likeCount = itemView.findViewById<TextView>(R.id.tv_like_count)
            likeCount.text = count
        }

        fun bindLikeImage(isLiked: Boolean) {
            if (isLiked) {
                likeButton.setImageResource(R.drawable.ic_like_selected)
            } else {
                likeButton.setImageResource(R.drawable.ic_like)
            }
        }
    }
}
