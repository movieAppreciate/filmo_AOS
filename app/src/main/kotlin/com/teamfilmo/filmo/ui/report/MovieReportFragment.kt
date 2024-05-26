package com.teamfilmo.filmo.ui.report

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
    private val reportAdapter2 by lazy {
        ReportAdapter2()
    }

    private fun onRefresh() {
        binding.swiperefresh.setOnRefreshListener {
            lifecycleScope.launch {
                reportViewModel.requestReport()
                reportViewModel.report.value?.let {
                    binding.reportRecyclerview1.apply {
                        adapter = reportAdapter
                        reportAdapter.setReportInfo(it, 0, 2)
                    }
                    binding.reportRecyclerview2.apply {
                        adapter = reportAdapter2
                        reportAdapter2.setReportInfo(it, 3, it.lastIndex)
                    }
                }
                reportViewModel.bookmarkList.value?.let {
                    reportAdapter.setBookmark(it)
                    reportAdapter2.setBookmark(it)
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
        reportViewModel.bookmarkList.observe(viewLifecycleOwner) { bookmarkList ->
            binding.reportRecyclerview1.apply {
                adapter = reportAdapter
                reportAdapter.setBookmark(bookmarkList)
            }

            binding.reportRecyclerview2.apply {
                adapter = reportAdapter2
                reportAdapter2.setBookmark(bookmarkList)
            }
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
                override fun onClick(position: Int) {
                    Toast.makeText(context, "감상문 클릭", Toast.LENGTH_SHORT).show()
                    // todo : 본문 페이지로 이동하기
                }

                override fun onLikeClick(position: Int) {
                    val report = reportAdapter.reportList[position]
                    lifecycleScope.launch {
                        reportViewModel.updateLike(report.reportId)
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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                reportViewModel.uiState.collect { data ->

                    val index = reportAdapter.reportList.indexOfFirst { it.reportId == data.reportId }
                    val index2 = reportAdapter2.reportList.indexOfFirst { it.reportId == data.reportId }

                    if (index != -1) {
                        reportAdapter.notifyItemChanged(index, ReportPayload.LikePayload(data.likeState))
                        reportAdapter.notifyItemChanged(index, ReportPayload.LikeCountPayload(data.likeCount))
                        reportAdapter.notifyItemChanged(index, ReportPayload.BookmarkPayload(data.bookmarkState))
                    } else {
                        reportAdapter2.notifyItemChanged(index2, ReportPayload.LikePayload(data.likeState))
                        reportAdapter2.notifyItemChanged(index2, ReportPayload.LikeCountPayload(data.likeCount))
                        reportAdapter2.notifyItemChanged(index2, ReportPayload.BookmarkPayload(data.bookmarkState))
                    }
                }
            }
        }
        onRefresh()
    }
}
