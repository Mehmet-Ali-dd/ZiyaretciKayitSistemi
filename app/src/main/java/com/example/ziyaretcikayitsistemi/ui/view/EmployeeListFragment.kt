package com.example.ziyaretcikayitsistemi.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ziyaretcikayitsistemi.databinding.FragmentEmployeeListBinding
import com.example.ziyaretcikayitsistemi.ui.adapter.EmployeeAdapter
import com.example.ziyaretcikayitsistemi.ui.viewmodel.EmployeeListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmployeeListFragment : Fragment() {

    private var _binding: FragmentEmployeeListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EmployeeListViewModel by viewModels()
    private lateinit var employeeAdapter: EmployeeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmployeeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        employeeAdapter = EmployeeAdapter { silinecekCalisan ->
            viewModel.deleteEmployee(silinecekCalisan)
            Toast.makeText(
                requireContext(),
                "${silinecekCalisan.firstName} ${silinecekCalisan.lastName} başarıyla silindi!",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.rvEmployees.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = employeeAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.allEmployees.observe(viewLifecycleOwner) { list ->
            employeeAdapter.submitList(list)
        }
    }

    private fun setupClickListeners() {
        binding.toolbarEmployeeList.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.fabAddEmployee.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(com.example.ziyaretcikayitsistemi.R.id.fragment_container, AddEmployeeFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}