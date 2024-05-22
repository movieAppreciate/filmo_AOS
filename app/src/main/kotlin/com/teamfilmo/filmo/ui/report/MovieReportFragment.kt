package com.teamfilmo.filmo.ui.report

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentMovieReportBinding
import com.teamfilmo.filmo.ui.report.recyclerview.ReportAdapter
import com.teamfilmo.filmo.ui.report.recyclerview.ReportAdapter2
import com.teamfilmo.filmo.ui.report.recyclerview.ReportPayload
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieReportFragment : BaseFragment<FragmentMovieReportBinding>(FragmentMovieReportBinding::inflate) {
    private val reportViewModel by activityViewModels<ReportViewModel>()
    val reportAdapter by lazy {
        ReportAdapter()
    }
    val reportAdapter2 by lazy {
        ReportAdapter2()
    }

    private fun onRefresh() {
        binding.swiperefresh.setOnRefreshListener {
            lifecycleScope.launch {
                reportViewModel.requestReport()
                reportViewModel.report.value?.let {
                    Log.d("좋아요 새로고림 setReportInfo", "호출")

                    binding.reportRecyclerview1.apply {
                        adapter = reportAdapter
                        reportAdapter.setReportInfo(it, 0, 2)
                    }

                    binding.reportRecyclerview2.apply {
                        adapter = reportAdapter2
                        reportAdapter.setReportInfo2(it, 3, it.lastIndex)
                    }
                }
                reportViewModel.bookmarkList.value?.let {
                    reportAdapter.setBookmark(it)
                }
                binding.swiperefresh.isRefreshing = false
            }
        }
    }

    override fun initViews() {
        super.initViews()

        childFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.recommend_fragment_container_view, RecommendReportFragment())
        }

        reportViewModel.report.observe(viewLifecycleOwner) { reportList ->

            binding.reportRecyclerview1.apply {
                adapter = reportAdapter
                reportAdapter.setReportInfo(reportList, 0, 2)
            }
            binding.reportRecyclerview2.apply {
                adapter = reportAdapter2
                reportAdapter2.setReportInfo(reportList, 3, reportList.lastIndex)
            }
        }
        reportAdapter.itemClick =
            object : ReportAdapter.ItemClick {
                override fun onClick(
                    view: View,
                    position: Int,
                ) {
                    Toast.makeText(context, "감상문 클릭", Toast.LENGTH_SHORT).show()
                    // todo : 본문 페이지로 이동하기
                }

                override fun onLikeClick(position: Int) {
                    val report = reportAdapter.reportList[position]
                    lifecycleScope.launch {
                        reportViewModel.toggleLike(report.reportId)
                    }
                }

                override fun onBookmarkClick(position: Int) {
                    val report = reportAdapter.reportList[position]
                    lifecycleScope.launch {
                        reportViewModel.toggleBookmark(report.reportId)
                    }
                }
            }
    }

    override fun initObserver() {
        super.initObserver()

        reportViewModel.likeStatus.observe(viewLifecycleOwner) { (reportId, isLiked) ->
            val index = reportAdapter.reportList.indexOfFirst { it.reportId == reportId }
            if (index != -1) {
                reportAdapter.notifyItemChanged(index, ReportPayload.LikePayload(isLiked))
            }
        }
        reportViewModel.likeCount.observe(viewLifecycleOwner) { (reportId, likeCount) ->
            val index = reportAdapter.reportList.indexOfFirst { it.reportId == reportId }
            if (index != -1) {
                reportAdapter.notifyItemChanged(index, ReportPayload.LikeCountPayload(likeCount))
            }
        }

        reportViewModel.bookmarkInfo.observe(viewLifecycleOwner) { (reportId, isBookmarked) ->
            val index = reportAdapter.reportList.indexOfFirst { it.reportId == reportId }
            if (index != -1) {
                reportAdapter.notifyItemChanged(index, ReportPayload.BookmarkPayload(isBookmarked))
            }
        }

        reportViewModel.bookmarkList.observe(viewLifecycleOwner) { bookmark ->
            reportAdapter.setBookmark(bookmark)
        }
        onRefresh()
    }
}
