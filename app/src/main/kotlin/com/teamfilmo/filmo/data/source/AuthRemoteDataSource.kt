package com.teamfilmo.filmo.data.source

import com.teamfilmo.filmo.ui.model.report.AuthResponse

interface AuthRemoteDataSource {
    suspend fun signUp(
        uid: String,
        type: String,
        profileURL: String?,
    ): Result<String>

    suspend fun login(
        uid: String,
        type: String,
    ): Result<AuthResponse>
}
