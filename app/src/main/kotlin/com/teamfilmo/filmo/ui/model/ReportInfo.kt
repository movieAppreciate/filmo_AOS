package com.teamfilmo.filmo.ui.model

import kotlinx.serialization.Serializable

@Serializable
data class ReportInfo(
    val searchReportCount: Int,
    val reportList: ArrayList<ReportList>,
    val hasNext: Boolean,
)

@Serializable
data class ReportList(
    val reportId: String,
    val title: String,
    val content: String,
    val createDate: String,
    val imageUrl: String?,
    val nickname: String,
    val likeCount: Int,
    val replyCount: Int?,
    val reportCount: Int,
    var isLiked: Boolean,
)
