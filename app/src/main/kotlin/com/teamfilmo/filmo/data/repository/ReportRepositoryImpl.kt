package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.source.ReportDataSource
import com.teamfilmo.filmo.domain.repository.ReportRepository
import com.teamfilmo.filmo.ui.model.Report
import com.teamfilmo.filmo.ui.model.ReportInfo
import javax.inject.Inject

class ReportRepositoryImpl
    @Inject
    constructor(
        private val reportDataSource: ReportDataSource,
    ) : ReportRepository {
        override suspend fun searchReport(): Result<ReportInfo> {
            return reportDataSource.searchReport()
        }

        override suspend fun getReport(reportId: String): Result<Report> {
            return reportDataSource.getReport(reportId)
        }

        override suspend fun registLike(reportId: String): Result<String> {
            return reportDataSource.registLike(reportId)
        }

        override suspend fun checkLike(reportId: String): Result<Boolean> {
            return reportDataSource.checkLike(reportId)
        }

        override suspend fun cancleLike(reportId: String): Result<String> {
            return reportDataSource.cancelLike(reportId)
        }

        override suspend fun countLike(reportId: String): Result<Int> {
            return reportDataSource.countLike(reportId)
        }
    }
