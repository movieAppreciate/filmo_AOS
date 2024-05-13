package com.teamfilmo.filmo.ui.report

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamfilmo.filmo.databinding.FragmentFollowingReportBinding
import com.teamfilmo.filmo.ui.report.recyclerview.FollowingReportAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowingReportFragment : Fragment() {
    private lateinit var binding: FragmentFollowingReportBinding

    private val reportViewModel by activityViewModels<ReportViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("감상문 프래그먼트 following onCreate", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFollowingReportBinding.inflate(inflater, container, false)
        Log.d("감상문 프래그먼트 following onCreateView", "onCreateView")

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("감상문 프래그먼트 following onViewCreated", "onViewCreated")

        reportViewModel.report.observe(viewLifecycleOwner) {
                reportList ->
            if (reportList != null) {
                val followingReviewAdapter = FollowingReportAdapter()
                followingReviewAdapter.setFollowingReportList(reportList)

                binding.followReviewRecyclerview.layoutManager = LinearLayoutManager(context)
                binding.followReviewRecyclerview.adapter = followingReviewAdapter
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("감상문 프래그먼트 following onDestroy", "onDestroy")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("감상문 프래그먼트 following onDestroyView", "onDestroyView")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("감상문 프래그먼트 following onAttach", "onAttach")
    }

    override fun onResume() {
        super.onResume()
        Log.d("감상문 프래그먼트 following onresume", "resume")
    }
}
