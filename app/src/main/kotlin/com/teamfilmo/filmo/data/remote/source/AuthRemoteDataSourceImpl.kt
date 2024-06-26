package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.remote.model.user.LoginResponse
import com.teamfilmo.filmo.data.remote.model.user.SignUpResponse
import com.teamfilmo.filmo.data.remote.service.AuthService
import com.teamfilmo.filmo.data.source.AuthRemoteDataSource
import javax.inject.Inject

class AuthRemoteDataSourceImpl
    @Inject
    constructor(
        private val authService: AuthService,
    ) : AuthRemoteDataSource {
        override suspend fun signUp(
            uid: String,
            type: String,
            profileURL: String?,
        ): Result<SignUpResponse> {
            return authService.signUp(uid, type, profileURL)
        }

        override suspend fun login(
            uid: String,
            type: String,
        ): Result<LoginResponse> {
            return authService.login(uid, type)
        }
    }
