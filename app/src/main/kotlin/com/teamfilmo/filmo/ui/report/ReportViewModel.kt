package com.teamfilmo.filmo.ui.report

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.domain.repository.ReportRepository
import com.teamfilmo.filmo.ui.model.Report
import com.teamfilmo.filmo.ui.model.ReportInfo
import com.teamfilmo.filmo.ui.model.ReportList
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ReportViewModel
    @Inject
    constructor(private val reportRepository: ReportRepository) :
    ViewModel() {
        private var _likeCount =
            MutableLiveData<Pair<String, Int>>()
        val likeCount: LiveData<Pair<String, Int>>
            get() = _likeCount
        private var _likeStatus =
            MutableLiveData<Pair<String, Boolean>>()
        val likeStatus: LiveData<Pair<String, Boolean>> = _likeStatus
        private var _report =
            MutableLiveData<ArrayList<ReportList>>()
                .apply {
                    value?.let { Log.d("감상문 뷰모델 _report changed", "inital report value : $it") }
                }
        val report: LiveData<ArrayList<ReportList>>
            get() =
                _report.also {
                    it.value?.let { reportList ->
                        Log.d("감상문 뷰모델 report changed", "report viewmodel $reportList")
                    }
                }

        init {
            Log.d("감상문 뷰모델 init", "init")
        }

        private suspend fun searchReport(): Result<ReportInfo> {
            return reportRepository.searchReport()
        }

        suspend fun requestReport() {
            viewModelScope.launch {
                Log.d("뷰모델", "requestReport() called")
                try {
                    val result = searchReport()
                    if (result.isSuccess) {
                        val response = result.getOrNull()
                        if (response != null) {
                            val reportValue = response.reportList
                            reportValue.forEach {
                                val likeResult = checkLike(it.reportId)
                                if (likeResult.isSuccess) {
                                    it.isLiked = likeResult.getOrNull() == true
                                }
                            }
                            _report.value = reportValue

                            Log.d("좋아요 뷰모델 requestReport", _report.value.toString())
                        } else {
                            Log.d("likeResult failed", "실패")
                        }
                    } else {
                        result.getOrThrow()
                    }
                } catch (e: IOException) {
                    Log.d("뷰모델", "error fetching report:$e")
                }
            }
        }

        private suspend fun getReport(reportId: String): Result<Report> {
            Log.d("좋아요 getreport", "getreport")
            return reportRepository.getReport(reportId)
        }

        private suspend fun isLike(reportId: String): Boolean {
            val response = checkLike(reportId)
            return response.getOrDefault(false)
        }

        private suspend fun registLike(reportId: String): Result<String> {
            _likeStatus.value = reportId to true
            Log.d("좋아요 등록", _likeStatus.value.toString())
            return reportRepository.registLike(reportId)
        }

        suspend fun toggleLike(reportId: String) {
            val isLike = isLike(reportId)
            if (isLike) {
                cancelLike(reportId)
            } else {
                registLike(reportId)
            }
            updateLikeCount(reportId)
        }

        private suspend fun cancelLike(reportId: String): Result<String> {
            _likeStatus.value = reportId to false
            Log.d("좋아요 취소", _likeStatus.value.toString())
            return reportRepository.cancleLike(reportId)
        }

        private suspend fun updateLikeCount(reportId: String) {
            val response = countLike(reportId)
            if (response.isSuccess) {
                _likeCount.value = reportId to response.getOrDefault(0)
            }
        }

        private suspend fun checkLike(reportId: String): Result<Boolean> {
            return reportRepository.checkLike(reportId)
        }

        private suspend fun countLike(reportId: String): Result<Int> {
            return reportRepository.countLike(reportId)
        }

        override fun onCleared() {
            super.onCleared()
        }
    }
