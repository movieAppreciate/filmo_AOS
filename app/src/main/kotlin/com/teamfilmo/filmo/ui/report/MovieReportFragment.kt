package com.teamfilmo.filmo.ui.report

import android.util.Log
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

        reportViewModel.likeState.observe(viewLifecycleOwner) { likeState ->
            Log.d("좋아요 변경", likeState.toString())

            val index = reportAdapter.reportList.indexOfFirst { it.reportId == likeState.reportId }
            val index2 = reportAdapter2.reportList.indexOfFirst { it.reportId == likeState.reportId }
            if (index != -1) {
                reportAdapter.notifyItemChanged(index, ReportPayload.LikePayload(likeState.isLiked))
            } else {
                reportAdapter2.notifyItemChanged(index2, ReportPayload.LikePayload(likeState.isLiked))
            }
        }

        reportViewModel.likeCount.observe(viewLifecycleOwner) { likeCount ->
            Log.d("좋아요 수 변경", likeCount.count.toString())
            val index = reportAdapter.reportList.indexOfFirst { it.reportId == likeCount.reportId }
            val index2 = reportAdapter2.reportList.indexOfFirst { it.reportId == likeCount.reportId }

            reportAdapter.notifyItemChanged(index, ReportPayload.LikeCountPayload(likeCount.count))
        }

        reportViewModel.bookmarkState.observe(viewLifecycleOwner) { bookmarkState ->
            Log.d("북마크 변경", bookmarkState.isBookmarked.toString())
            val index = reportAdapter.reportList.indexOfFirst { it.reportId == bookmarkState.reportId }
            val index2 = reportAdapter2.reportList.indexOfFirst { it.reportId == bookmarkState.reportId }

            if (index != -1) {
                reportAdapter.notifyItemChanged(index, ReportPayload.BookmarkPayload(bookmarkState.isBookmarked))
            } else {
                reportAdapter2.notifyItemChanged(index2, ReportPayload.BookmarkPayload(bookmarkState.isBookmarked))
            }
        }

        onRefresh()
    }
}
