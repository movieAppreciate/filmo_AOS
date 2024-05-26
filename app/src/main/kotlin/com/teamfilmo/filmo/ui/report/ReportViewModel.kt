package com.teamfilmo.filmo.ui.report

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.domain.repository.BookmarkRepository
import com.teamfilmo.filmo.domain.repository.LikeRepository
import com.teamfilmo.filmo.domain.repository.MovieRepository
import com.teamfilmo.filmo.domain.repository.ReportRepository
import com.teamfilmo.filmo.ui.model.bookmark.BookmarkCount
import com.teamfilmo.filmo.ui.model.bookmark.BookmarkList
import com.teamfilmo.filmo.ui.model.bookmark.BookmarkResponse
import com.teamfilmo.filmo.ui.model.movie.DetailMovieResponse
import com.teamfilmo.filmo.ui.model.report.Report
import com.teamfilmo.filmo.ui.model.report.ReportInfo
import com.teamfilmo.filmo.ui.model.report.ReportItem
import com.teamfilmo.filmo.ui.model.report.ReportList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UiState(
    val reportId: String = "",
    val likeState: Boolean = false,
    val likeCount: Int = 0,
    val bookmarkState: Boolean = false,
    val bookmarkCount: Int = 0,
)

@HiltViewModel
class ReportViewModel
    @Inject
    constructor(
        private val reportRepository: ReportRepository,
        private val movieRepository: MovieRepository,
        private val likeRepository: LikeRepository,
        private val bookmarkRepository: BookmarkRepository,
    ) :
    ViewModel() {
        private val _uiState = MutableStateFlow(UiState())
        val uiState: StateFlow<UiState> = _uiState.asStateFlow()

        private var sortRecommendList: List<Report> = listOf()
        private var _recommendList = MutableLiveData<List<Report>>()
        val recommendList: LiveData<List<Report>>
            get() = _recommendList

        private var _bookmarkList =
            MutableLiveData<List<BookmarkResponse>>()
        val bookmarkList: LiveData<List<BookmarkResponse>>
            get() = _bookmarkList

        private var likeList: MutableList<Boolean> = mutableListOf()

        private var _report =
            MutableLiveData<List<ReportItem>>()
        val report: LiveData<List<ReportItem>>
            get() = _report

        private var reportList: MutableList<Report> = mutableListOf()

        private var _filteredReportList = MutableLiveData<MutableList<Report>>()
        val filteredReportList: LiveData<MutableList<Report>>
            get() = _filteredReportList

        fun updateLike(reportId: String) {
            viewModelScope.launch {
                val result = checkLike(reportId)
                if (result.isSuccess) {
                    val response = result.getOrThrow()
                    if (response) {
                        cancelLike(reportId)
                    } else {
                        registLike(reportId)
                    }
                    updateLikeCount(reportId)
                }
            }
        }

        fun toggleBookmark(reportId: String) {
            viewModelScope.launch {
                getBookmarkResponse()
                val bookmark = _bookmarkList.value?.find { it.reportId == reportId }
                if (bookmark != null) {
                    deleteBookmark(bookmark.bookmarkId)
                } else {
                    registBookmark(reportId)
                }
            }
        }

        private suspend fun registBookmark(reportId: String): Result<BookmarkResponse> {
            _uiState.update { currentState ->
                currentState.copy(
                    reportId = reportId,
                    bookmarkState = true,
                )
            }
            return bookmarkRepository.registBookmark(reportId)
        }

        suspend fun getBookmarkResponse() {
            val result = getBookmark()
            if (result.isSuccess) {
                _bookmarkList.value = result.getOrThrow().bookmarkList
            }
        }

        private suspend fun getBookmark(): Result<BookmarkList> {
            return bookmarkRepository.getBookmarkList()
        }

        suspend fun getBookmarkCount(reportId: String): Result<BookmarkCount> {
            return bookmarkRepository.getBookmarkCount(reportId)
        }

        private suspend fun deleteBookmark(bookmarkId: Int): Result<String> {
            _uiState.update { currentState ->
                currentState.copy(
                    bookmarkState = false,
                )
            }
            return bookmarkRepository.deleteBookmark(bookmarkId)
        }

        private suspend fun searchReport(): Result<ReportInfo> {
            return reportRepository.searchReport()
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
                    }
                } else {
                    result.getOrThrow()
                }
            }
        }

        private fun getReportList() {
            viewModelScope.launch {
                _report.value?.forEach {
                    val result = getReport(it.reportId)
                    if (result.isSuccess) {
                        result.getOrNull()?.let { it1 -> reportList.add(it1) }
                    } else {
                    }
                }
                sortRecommendReport()
            }
        }

        private suspend fun getReport(reportId: String): Result<Report> {
            return reportRepository.getReport(reportId)
        }

        private suspend fun registLike(reportId: String): Result<String> {
            _uiState.update { currentState ->
                currentState.copy(
                    likeState = true,
                    reportId = reportId,
                )
            }
            Log.d("좋아요 뷰모델", "$reportId 좋아요 등록")
            return likeRepository.registLike(reportId)
        }

        private suspend fun cancelLike(reportId: String): Result<String> {
            _uiState.update { currentState ->
                currentState.copy(
                    likeState = false,
                    reportId = reportId,
                )
            }
            Log.d("좋아요 뷰모델", "취소")
            return likeRepository.cancleLike(reportId)
        }

        private suspend fun updateLikeCount(reportId: String) {
            val response = countLike(reportId)
            if (response.isSuccess) {
                _uiState.update { currentState ->
                    currentState.copy(
                        likeCount = response.getOrThrow(),
                    )
                }
            } else {
                Log.d("update like count failed", response.exceptionOrNull().toString())
            }
        }

        private suspend fun checkLike(reportId: String): Result<Boolean> {
            return likeRepository.checkLike(reportId)
        }

        private suspend fun countLike(reportId: String): Result<Int> {
            return likeRepository.countLike(reportId)
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
                val sorted = reportList.sortedBy { it.viewCount.plus(it.likeCount) }
                // 좋아요 수 + 조회수를 기준으로 정렬하기
                if (sorted.size >= 20) {
                    sorted.take(20)
                    sortRecommendList = sorted.getRandomElements(5)
                } else if (sorted.size < 5) {
                    sortRecommendList = sorted
                } else {
                    sortRecommendList = sorted.getRandomElements(5)
                }
                _recommendList.value = sortRecommendList
            }
        }

    /*
    장르 버튼 기능 구현
     */
        suspend fun filterGenreReport(genre: String) {
            var newList = mutableListOf<Report>()
            reportList.forEach { report ->
                val result = searchMovieDetail(report.movieId)
                if (result.isSuccess) {
                    result.getOrNull().apply {
                        Log.d("뷰모델 filter genre result 확인", result.getOrThrow().toString())
                        val isGenre = this?.genres?.find { it.name == genre }
                        Log.d("isGenre", isGenre.toString())
//                        if (isGenre) {
//                            newList.add(report)
//                            Log.d("뷰모델 filter genre true", isGenre.toString())
//                        } else {
//                            Log.d("뷰모델 filter genre else ", isGenre.toString())
//                        }
                    }
                } else {
                    Log.d("filter 실패", result.exceptionOrNull().toString())
                }
            }
            _filteredReportList.value?.addAll(newList)
        }

        private suspend fun searchMovieDetail(movieId: Int): Result<DetailMovieResponse> {
            return movieRepository.searchDetail(movieId = movieId)
        }
    }
