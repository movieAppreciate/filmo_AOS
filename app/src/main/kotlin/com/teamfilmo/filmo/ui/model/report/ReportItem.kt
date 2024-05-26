package com.teamfilmo.filmo.ui.model.report

class ReportItem(
    val reportId: String,
    val title: String,
    val content: String,
    val createDate: String,
    val imageUrl: String?,
    val nickname: String,
    var likeCount: Int,
    val replyCount: Int,
    val bookmarkCount: Int,
    var isLiked: Boolean,
)
