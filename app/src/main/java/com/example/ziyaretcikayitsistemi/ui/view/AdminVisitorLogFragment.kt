package com.example.ziyaretcikayitsistemi.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ziyaretcikayitsistemi.databinding.FragmentAdminVisitorLogBinding
import com.example.ziyaretcikayitsistemi.ui.adapter.VisitorLogAdapter
import com.example.ziyaretcikayitsistemi.ui.viewmodel.VisitorListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminVisitorLogFragment : Fragment() {

    private var _binding: FragmentAdminVisitorLogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VisitorListViewModel by viewModels()
    private lateinit var adapter: VisitorLogAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAdminVisitorLogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarLog.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        adapter = VisitorLogAdapter()
        binding.rvVisitorLog.layoutManager = LinearLayoutManager(requireContext())
        binding.rvVisitorLog.adapter = adapter

        observeViewModel()
    }


    private fun observeViewModel() {
        viewModel.visitorsWithDetails.observe(viewLifecycleOwner) { logListesi ->
            adapter.submitList(logListesi)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}