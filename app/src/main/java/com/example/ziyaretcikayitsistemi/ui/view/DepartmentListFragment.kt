package com.example.ziyaretcikayitsistemi.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ziyaretcikayitsistemi.R
import com.example.ziyaretcikayitsistemi.databinding.FragmentDepartmentListBinding
import com.example.ziyaretcikayitsistemi.ui.adapter.DepartmentAdapter
import com.example.ziyaretcikayitsistemi.ui.viewmodel.DepartmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DepartmentListFragment : Fragment() {

    private var _binding: FragmentDepartmentListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DepartmentViewModel by viewModels()
    private lateinit var departmentAdapter: DepartmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDepartmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        departmentAdapter = DepartmentAdapter { silinecekDepartman ->
            viewModel.deleteDepartment(silinecekDepartman)
            Toast.makeText(
                requireContext(),
                "${silinecekDepartman.departmentName} başarıyla silindi!",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.rvDepartments.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = departmentAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.allDepartments.observe(viewLifecycleOwner) { list ->
            departmentAdapter.submitList(list)
        }
    }

    private fun setupClickListeners() {
        binding.toolbarDepartmentList.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.fabAddDepartment.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AddDepartmentFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}