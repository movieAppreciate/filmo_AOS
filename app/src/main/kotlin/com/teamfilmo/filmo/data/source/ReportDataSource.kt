package com.teamfilmo.filmo.data.source

import com.teamfilmo.filmo.ui.model.Report
import com.teamfilmo.filmo.ui.model.ReportInfo

interface ReportDataSource {
    //    fun searchReport() : Flow<ReportInfo>
    suspend fun searchReport(): Result<ReportInfo>

    suspend fun getReport(reportId: String): Result<Report>

    suspend fun registLike(reportId: String): Result<String>

    suspend fun checkLike(reportId: String): Result<Boolean>

    suspend fun cancelLike(reportId: String): Result<String>

    suspend fun countLike(reportId: String): Result<Int>
}
