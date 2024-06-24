package com.teamfilmo.filmo.domain

import com.teamfilmo.filmo.domain.repository.ReportRepository
import dagger.Reusable
import javax.inject.Inject
import kotlinx.coroutines.flow.flow

@Reusable
class GetReportListUseCase
    @Inject
    constructor(
        private val reportRepository: ReportRepository,
    ) {
        operator fun invoke() =
            flow {
                val result = reportRepository.searchReport()

                emit(result.getOrNull()?.reportList ?: emptyList())
            }
    }
