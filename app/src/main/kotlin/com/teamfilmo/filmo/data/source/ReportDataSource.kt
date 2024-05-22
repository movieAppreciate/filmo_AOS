package com.teamfilmo.filmo.data.source

import com.teamfilmo.filmo.ui.model.bookmark.BookmarkCount
import com.teamfilmo.filmo.ui.model.bookmark.BookmarkList
import com.teamfilmo.filmo.ui.model.bookmark.BookmarkResponse
import com.teamfilmo.filmo.ui.model.report.Report
import com.teamfilmo.filmo.ui.model.report.ReportInfo

interface ReportDataSource {
    suspend fun registBookmark(reportId: String): Result<BookmarkResponse>

    suspend fun deleteBookmark(bookmarkId: Int): Result<String>

    suspend fun getBookmarkList(): Result<BookmarkList>

    suspend fun getBookmarkCount(reportId: String): Result<BookmarkCount>

    suspend fun searchReport(): Result<ReportInfo>

    suspend fun getReport(reportId: String): Result<Report>

    suspend fun registLike(reportId: String): Result<String>

    suspend fun checkLike(reportId: String): Result<Boolean>

    suspend fun cancelLike(reportId: String): Result<String>

    suspend fun countLike(reportId: String): Result<Int>
}
