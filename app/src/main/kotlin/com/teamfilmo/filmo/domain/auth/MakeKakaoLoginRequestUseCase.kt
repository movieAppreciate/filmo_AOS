package com.teamfilmo.filmo.domain.auth

import com.teamfilmo.filmo.data.repository.AuthRepository
import javax.inject.Inject

class MakeKakaoLoginRequestUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) {
        suspend operator fun invoke(email: String) {
            authRepository.login(email, "kakao")
        }
    }