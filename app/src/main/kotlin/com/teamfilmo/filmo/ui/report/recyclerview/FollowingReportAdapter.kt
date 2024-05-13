package com.teamfilmo.filmo.ui.report.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.ui.model.ReportList

class FollowingReportAdapter() : RecyclerView.Adapter<FollowingReportAdapter.FollowingReportViewHolder>() {
    private lateinit var followingReportList: ArrayList<ReportList>

    fun setFollowingReportList(reportList: ArrayList<ReportList>) {
        this.followingReportList = reportList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): FollowingReportViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.following_report_item, parent, false)
        return FollowingReportViewHolder(v)
    }

    override fun getItemCount(): Int {
        return followingReportList.size
    }

    override fun onBindViewHolder(
        holder: FollowingReportViewHolder,
        position: Int,
    ) {
        holder.bindItems(followingReportList[position])
    }

    // 레이아웃과 데이터를 연결
    inner class FollowingReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(items: ReportList) {
            val title = itemView.findViewById<TextView>(R.id.tvTitle)
            val content = itemView.findViewById<TextView>(R.id.tvContent)
            val likeCount = itemView.findViewById<TextView>(R.id.tvLikeCount)
            val commentCount = itemView.findViewById<TextView>(R.id.tvCommentCount)
            val bookmarkCount = itemView.findViewById<TextView>(R.id.tvBookmarkCount)
            val nickname = itemView.findViewById<TextView>(R.id.tvUserName)

            title.text = items.title
            content.text = items.content
            likeCount.text = items.likeCount.toString()
            commentCount.text = items.replyCount.toString()
            bookmarkCount.text = items.reportCount.toString()
            nickname.text = items.nickname
        }
    }
}
