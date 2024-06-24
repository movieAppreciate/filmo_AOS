package com.teamfilmo.filmo.ui.report.all

import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.domain.GetReportListUseCase
import com.teamfilmo.filmo.domain.bookmark.GetBookmarkListUseCase
import com.teamfilmo.filmo.model.report.ReportItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.combine

@HiltViewModel
class AllMovieReportViewModel
    @Inject
    constructor(
        private val getReportListUseCase: GetReportListUseCase,
        private val getBookmarkListUseCase: GetBookmarkListUseCase,
    ) : BaseViewModel<AllMovieReportEffect, AllMovieReportEvent>() {
        private val _list =
            combine(
                getReportListUseCase(),
                getBookmarkListUseCase(),
            ) { reportList, bookmarkList ->
                reportList.map { reportItem ->
                    ReportItem(
                        reportItem.id,
                        reportItem.title,
                        reportItem.content,
                        reportItem.createdAt,
                        reportItem.updatedAt,
                        bookmarkList.any { it.id == reportItem.id },
                    )
                }
            }
    }
