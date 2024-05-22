package com.teamfilmo.filmo.ui.report

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.domain.repository.ReportRepository
import com.teamfilmo.filmo.ui.model.bookmark.BookmarkCount
import com.teamfilmo.filmo.ui.model.bookmark.BookmarkList
import com.teamfilmo.filmo.ui.model.bookmark.BookmarkResponse
import com.teamfilmo.filmo.ui.model.report.Report
import com.teamfilmo.filmo.ui.model.report.ReportInfo
import com.teamfilmo.filmo.ui.model.report.ReportItem
import com.teamfilmo.filmo.ui.model.report.ReportList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ReportViewModel
    @Inject
    constructor(private val reportRepository: ReportRepository) :
    ViewModel() {
        private var sortRecommendList: List<Report> = listOf()
        private var _recommendList = MutableLiveData<List<Report>>()
        val recommendList: LiveData<List<Report>>
            get() = _recommendList

        private var _bookmarkList =
            MutableLiveData<List<BookmarkResponse>>().apply {
                Log.d("뷰모델 _bookmarkList", "업뎃")
            }
        val bookmarkList: LiveData<List<BookmarkResponse>>
            get() = _bookmarkList

        private var _bookmarkInfo =
            MutableLiveData<Pair<String, Boolean>>()

        val bookmarkInfo: LiveData<Pair<String, Boolean>>
            get() = _bookmarkInfo

        private var likeList: MutableList<Boolean> = mutableListOf()

        private var _likeCount =
            MutableLiveData<Pair<String, Int>>()
        val likeCount: LiveData<Pair<String, Int>>
            get() = _likeCount

        private var _likeStatus =
            MutableLiveData<Pair<String, Boolean>>()
        val likeStatus: LiveData<Pair<String, Boolean>> = _likeStatus

        private var _report =
            MutableLiveData<List<ReportItem>>()
        val report: LiveData<List<ReportItem>>
            get() = _report

        private var reportList: MutableList<Report> = mutableListOf()

        init {
            Log.d("감상문 뷰모델 init", "init")
        }

        fun toggleBookmark(reportId: String) {
            viewModelScope.launch {
                _bookmarkList.value = getBookmark() ?: return@launch
                val bookmark = _bookmarkList.value!!.find { it.reportId == reportId }
                Log.d("뷰모델 북마크 ", bookmark.toString())
                if (bookmark != null) {
                    deleteBookmark(bookmark.bookmarkId)
                    _bookmarkInfo.value = reportId to false
                } else {
                    registBookmark(reportId)
                }
            }
        }

        private suspend fun registBookmark(reportId: String): Result<BookmarkResponse> {
            _bookmarkInfo.value = reportId to true
            return reportRepository.registBookmark(reportId)
        }

        private suspend fun getBookmarkList(): Result<BookmarkList> {
            return reportRepository.getBookmarkList()
        }

        suspend fun getBookmarkCount(reportId: String): Result<BookmarkCount> {
            return reportRepository.getBookmarkCount(reportId)
        }

        private suspend fun deleteBookmark(bookmarkId: Int): Result<String> {
            return reportRepository.deleteBookmark(bookmarkId)
        }

        private suspend fun searchReport(): Result<ReportInfo> {
            return reportRepository.searchReport()
        }

        suspend fun getBookmark(): List<BookmarkResponse>? {
            val result = getBookmarkList()
            if (result.isSuccess) {
                _bookmarkList.value = result.getOrThrow().bookmarkList
            }
            return _bookmarkList.value
        }

        private fun mapForReportItem(
            reportList: List<ReportList>,
            likeList: MutableList<Boolean>,
        ): List<ReportItem> {
            return reportList.mapIndexed { index, reportItem ->
                ReportItem(
                    reportId = reportItem.reportId,
                    title = reportItem.title,
                    content = reportItem.content,
                    createDate = reportItem.createDate,
                    imageUrl = reportItem.imageUrl,
                    nickname = reportItem.nickname,
                    likeCount = reportItem.likeCount,
                    replyCount = reportItem.replyCount,
                    bookmarkCount = reportItem.bookmarkCount,
                    isLiked = likeList[index],
                )
            }
        }

        fun requestReport() {
            viewModelScope.launch {
                val result = searchReport()
                if (result.isSuccess) {
                    val response = result.getOrNull()
                    if (response != null) {
                        response.reportList.forEach {
                            val likeResult = checkLike(it.reportId)
                            if (likeResult.isSuccess) {
                                likeResult.getOrNull()?.let { isLiked ->
                                    likeList.add(isLiked)
                                }
                            }
                        }
                        _report.value = mapForReportItem(response.reportList, likeList)
                        getReportList()
                    } else {
                        Log.d("likeResult failed", "실패")
                    }
                } else {
                    Log.d("뷰모델", "result는 null")
                    result.getOrThrow()
                }
            }
        }

        private fun getReportList() {
            viewModelScope.launch {
                _report.value?.forEach {
                    Log.d("뷰모델 reportList _report", _report.value.toString())
                    val result = getReport(it.reportId)
                    if (result.isSuccess) {
                        result.getOrNull()?.let { it1 -> reportList.add(it1) }
                        Log.d("뷰모델 getReportList", reportList.toString())
                    } else {
                        Log.d("getreportlist 실패", "실패 ${result.exceptionOrNull()}")
                    }
                }
                sortRecommendReport()
            }
        }

        private suspend fun getReport(reportId: String): Result<Report> {
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
                Log.d("좋아요 수 뷰모델 업데이트", response.getOrThrow().toString())
                _likeCount.value = reportId to response.getOrThrow()
            } else {
                Log.d("좋아요 수 에러", response.exceptionOrNull().toString())
            }
        }

        private suspend fun checkLike(reportId: String): Result<Boolean> {
            return reportRepository.checkLike(reportId)
        }

        private suspend fun countLike(reportId: String): Result<Int> {
            return reportRepository.countLike(reportId)
        }

        /**
         * 추천 감상문 데이터 처리 로직
         */

        private fun <T> List<T>.getRandomElements(count: Int): List<T> {
            require(count in 0..size)
            return this.shuffled().take(count)
        }

        private fun sortRecommendReport() {
            viewModelScope.launch {
                Log.d("추천 sort", reportList.toString())
                val sorted = reportList.sortedBy { it.viewCount.plus(it.likeCount) }
                Log.d("추천 감상문 sort", sorted.toString())
                // 좋아요 수 + 조회수를 기준으로 정렬하기
                if (sorted.size >= 20) {
                    sorted.take(20)
                    sortRecommendList = sorted.getRandomElements(5)
                    Log.d("뷰모델 recommend sortRecommendList", sortRecommendList.toString())
                } else if (sorted.size < 5) {
                    sortRecommendList = sorted
                    Log.d("뷰모델 recommend sortRecommendList", sortRecommendList.toString())
                } else {
                    sortRecommendList = sorted.getRandomElements(5)
                    Log.d("뷰모델 recommend sortRecommendList", sortRecommendList.toString())
                }
                _recommendList.value = sortRecommendList
                Log.d("결과 뷰모델 sort", _recommendList.value.toString())
            }
        }
    }
