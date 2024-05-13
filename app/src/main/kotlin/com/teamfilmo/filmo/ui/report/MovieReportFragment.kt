package com.teamfilmo.filmo.ui.report

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamfilmo.filmo.databinding.FragmentMovieReportBinding
import com.teamfilmo.filmo.ui.model.ReportList
import com.teamfilmo.filmo.ui.report.recyclerview.ReportAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieReportFragment : Fragment() {
    private val reportViewModel by activityViewModels<ReportViewModel>()
    private lateinit var binding: FragmentMovieReportBinding
    private lateinit var reportAdapter: ReportAdapter

    private fun onRefresh() {
        binding.swiperefresh.setOnRefreshListener {
            lifecycleScope.launch {
                reportViewModel.requestReport()
                reportViewModel.report.value?.let {
                    Log.d("좋아요 새로고림 setReportInfo", "호출")
                    reportAdapter.setReportInfo(
                        it,
                    )
                }
                binding.swiperefresh.isRefreshing = false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("프래그먼트 review onCreate", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMovieReportBinding.inflate(inflater, container, false)

        Log.d("프래그먼트 review onCreateView", "onCreateView")

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("좋아요 reportList observe 전 ", reportViewModel.report.value.toString())
        val reportObserver =
            Observer<ArrayList<ReportList>> { reportList ->
                reportAdapter = ReportAdapter()
                reportAdapter.setReportInfo(reportList)
                initRecyclerView()
            }

        reportViewModel.report.observe(viewLifecycleOwner, reportObserver)

        reportViewModel.report.observe(viewLifecycleOwner) { reportList ->
            Log.d("좋아요 reportList observe 후", reportList.toString())
            reportAdapter = ReportAdapter()
            reportAdapter.setReportInfo(reportList)
            initRecyclerView()
        }

        reportViewModel.likeStatus.observe(viewLifecycleOwner) { (reportId, isLiked) ->
            Log.d("좋아요 isLiked 변경 확인하기", isLiked.toString())
            val index = reportAdapter.reportList.indexOfFirst { it.reportId == reportId }
            if (index != -1) {
                reportAdapter.notifyItemChanged(index, isLiked.toString())
            }
        }
        reportViewModel.likeCount.observe(viewLifecycleOwner) { (reportId, likeCount) ->
            val index = reportAdapter.reportList.indexOfFirst { it.reportId == reportId }
            if (index != -1) {
                reportAdapter.notifyItemChanged(index, likeCount)
            }
        }
        onRefresh()
    }

    private fun initRecyclerView() {
        Log.d("좋아요", "initRecyclerView 호출")

        binding.reviewRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = reportAdapter

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
                }
        }
    }
}
