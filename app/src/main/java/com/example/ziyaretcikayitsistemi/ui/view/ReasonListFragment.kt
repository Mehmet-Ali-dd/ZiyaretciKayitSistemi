package com.example.ziyaretcikayitsistemi.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ziyaretcikayitsistemi.databinding.FragmentReasonListBinding
import com.example.ziyaretcikayitsistemi.ui.adapter.ReasonAdapter
import com.example.ziyaretcikayitsistemi.ui.viewmodel.ReasonListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReasonListFragment : Fragment() {

    private var _binding: FragmentReasonListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReasonListViewModel by viewModels()
    private lateinit var reasonAdapter: ReasonAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReasonListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRcyclerView()
        observeViewModel()
        setupClickListener()
    }

    private fun setupRcyclerView() {
        reasonAdapter = ReasonAdapter { silinecekSebep ->
            viewModel.deleteReason(silinecekSebep)
            Toast.makeText(
                requireContext(),
                "${silinecekSebep.reasonName} başarıyla silindi!",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.rvReasons.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = reasonAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.allReasons.observe(viewLifecycleOwner) { list ->
            reasonAdapter.submitList(list)
        }
    }

    private fun setupClickListener() {
        binding.toolbarReasonList.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.fabAddReason.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(com.example.ziyaretcikayitsistemi.R.id.fragment_container, AddReasonFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}