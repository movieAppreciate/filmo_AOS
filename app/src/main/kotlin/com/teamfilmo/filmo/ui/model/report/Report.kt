package com.teamfilmo.filmo.ui.model.report

import kotlinx.serialization.Serializable

@Serializable
data class Report(
    val reportId: String,
    val title: String,
    val content: String,
    val userId: String,
    val movieId: String,
    val tagString: String,
    val complaintCount: Int,
    val replyCount: Int,
    val likeCount: Int,
    val isLike: Boolean,
    val viewCount: Int,
    val imageUrl: String?,
    val createDate: String,
    val lastModifiedDate: String,
)
