package com.teamfilmo.filmo.data.remote.model.user

import kotlinx.serialization.Serializable

@Serializable
data class SignUpResponse(
    val uid: String,
    val userId: String,
    val nickname: String,
    val type: String,
    val profileUrl: String? = null,
    val lastLoginDate: String,
    val introduction: String,
    val role: List<String> = emptyList(),
)
