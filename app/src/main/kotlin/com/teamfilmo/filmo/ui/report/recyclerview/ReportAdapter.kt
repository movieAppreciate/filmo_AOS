package com.teamfilmo.filmo.ui.report.recyclerview

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.databinding.MovieItemBinding
import com.teamfilmo.filmo.ui.model.bookmark.BookmarkResponse
import com.teamfilmo.filmo.ui.model.report.ReportItem

sealed class ReportPayload {
    data class BookmarkPayload(var isBookmarked: Boolean) : ReportPayload()

    data class LikePayload(val isLiked: Boolean) : ReportPayload()

    data class LikeCountPayload(val likeCount: Int) : ReportPayload()
}

class ReportAdapter() : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {
    private var bookmarkList: List<BookmarkResponse> = listOf()
    var reportList: List<ReportItem> = listOf()

    interface ItemClick {
        fun onClick(
            view: View,
            position: Int,
        )

        fun onLikeClick(position: Int)

        fun onBookmarkClick(position: Int)
    }

    var itemClick: ItemClick? = null

    fun setReportInfo(
        newReportList: List<ReportItem>,
        startIndex: Int,
        endIndex: Int,
    ) {
        this.reportList = newReportList.subList(startIndex, endIndex + 1)
        Log.d("어댑터 reportList", reportList.get(0).title)
        notifyItemRangeChanged(startIndex, reportList.size)
    }

    fun setReportInfo2(
        newReportList: List<ReportItem>,
        startIndex: Int,
        endIndex: Int,
    ) {
        this.reportList = newReportList
        Log.d("어댑터 reportList", reportList.get(0).title)
        notifyItemRangeChanged(3, reportList.size)
    }

    fun setBookmark(bookmarkList: List<BookmarkResponse>) {
        this.bookmarkList = bookmarkList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ReportViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReportViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ReportViewHolder,
        position: Int,
    ) {
        holder.bindItems(reportList[position])
        holder.bindLikeImage(reportList[position].isLiked)
        val isBookmarked =
            bookmarkList.any {
                it.reportId == reportList[position].reportId
            }
        holder.bindBookmarkButton(isBookmarked)

        if (itemClick != null) {
            holder.itemView.findViewById<ImageView>(R.id.movie_image).setOnClickListener { v ->
                itemClick?.onClick(v, position)
            }
        }
    }

    override fun onBindViewHolder(
        holder: ReportViewHolder,
        position: Int,
        payloads: MutableList<Any>,
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            payloads.forEach {
                when (val payload = it as ReportPayload) {
                    is ReportPayload.BookmarkPayload -> {
                        holder.bindBookmarkButton(payload.isBookmarked)
                    }

                    is ReportPayload.LikePayload -> {
                        this.reportList[position].isLiked = payload.isLiked
                        holder.bindLikeButton(if (payload.isLiked) R.drawable.ic_like_selected else R.drawable.ic_like_unselected)
                    }

                    is ReportPayload.LikeCountPayload -> {
                        holder.bindLikeCount(payload.likeCount)
                    }

                    else -> {
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        Log.d("데이터 size", reportList.size.toString())
        return reportList.size
    }

    inner class ReportViewHolder(private val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val likeButton: ImageButton = binding.likeButton
        private val bookmarkButton: ImageButton = binding.bookmarkButton

        init {
            likeButton.setOnClickListener {
                itemClick?.onLikeClick(adapterPosition)
            }
            bookmarkButton.setOnClickListener {
                itemClick?.onBookmarkClick(adapterPosition)
            }
        }

        fun bindItems(item: ReportItem) {
            val title = binding.tvTitle
            val content = binding.tvContent
            val replyCount = binding.tvReplyCount
            val likeCount = binding.tvLikeCount
            val nickName = binding.tvNickName
            title.text = item.title
            content.text = item.content
            replyCount.text = item.replyCount.toString()
            nickName.text = item.nickname
            likeCount.text = item.likeCount.toString()
        }

        fun bindBookmarkButton(isBookmarked: Boolean) {
            if (isBookmarked) {
                bookmarkButton.setImageResource(R.drawable.ic_bookmark_selected)
            } else {
                bookmarkButton.setImageResource(R.drawable.ic_bookmark_unselected)
            }
        }

        fun bindLikeButton(imageSrc: Int) {
            likeButton.setImageResource(imageSrc)
        }

        fun bindLikeCount(count: Int) {
            val likeCount = binding.tvLikeCount
            likeCount.text = count.toString()
        }

        fun bindLikeImage(isLiked: Boolean) {
            Log.d("좋아요 어댑터 ", isLiked.toString())
            if (isLiked) {
                likeButton.setImageResource(R.drawable.ic_like_selected)
            } else {
                likeButton.setImageResource(R.drawable.ic_like_unselected)
            }
        }
    }
}