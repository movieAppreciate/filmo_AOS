package com.teamfilmo.filmo.data.di

import com.teamfilmo.filmo.data.remote.source.ReportRepositoryImpl
import com.teamfilmo.filmo.data.repository.AuthRepositoryImpl
import com.teamfilmo.filmo.domain.repository.AuthRepository
import com.teamfilmo.filmo.domain.repository.ReportRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl,
    ): AuthRepository

    @Binds
    abstract fun bindReportRepository(
        reportRepositoryImpl: ReportRepositoryImpl,
    ): ReportRepository
}
