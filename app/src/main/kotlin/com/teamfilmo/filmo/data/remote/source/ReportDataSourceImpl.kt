package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.remote.service.BookmarkService
import com.teamfilmo.filmo.data.remote.service.LikeService
import com.teamfilmo.filmo.data.remote.service.ReportService
import com.teamfilmo.filmo.data.source.ReportDataSource
import com.teamfilmo.filmo.ui.model.bookmark.BookmarkCount
import com.teamfilmo.filmo.ui.model.bookmark.BookmarkList
import com.teamfilmo.filmo.ui.model.bookmark.BookmarkResponse
import com.teamfilmo.filmo.ui.model.report.Report
import com.teamfilmo.filmo.ui.model.report.ReportInfo
import javax.inject.Inject

class ReportDataSourceImpl
    @Inject
    constructor(
        private val reportService: ReportService,
        private val likeService: LikeService,
        private val bookmarkService: BookmarkService,
    ) : ReportDataSource {
        override suspend fun registBookmark(reportId: String): Result<BookmarkResponse> {
            return bookmarkService.registerBookmark(reportId)
        }

        override suspend fun deleteBookmark(bookmarkId: Int): Result<String> {
            return bookmarkService.deleteBookmark(bookmarkId)
        }

        override suspend fun getBookmarkList(): Result<BookmarkList> {
            return bookmarkService.getBookmarkList()
        }

        override suspend fun getBookmarkCount(reportId: String): Result<BookmarkCount> {
            return bookmarkService.getBookmarkCount(reportId)
        }

        override suspend fun searchReport(): Result<ReportInfo> {
            return reportService.searchReport()
        }

        override suspend fun getReport(reportId: String): Result<Report> {
            return reportService.getReport(reportId)
        }

        override suspend fun registLike(reportId: String): Result<String> {
            return likeService.regist(reportId)
        }

        override suspend fun checkLike(reportId: String): Result<Boolean> {
            return likeService.check(reportId)
        }

        override suspend fun cancelLike(reportId: String): Result<String> {
            return likeService.cancel(reportId)
        }

        override suspend fun countLike(reportId: String): Result<Int> {
            return likeService.count(reportId)
        }
    }
