package com.example.ziyaretcikayitsistemi.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.ziyaretcikayitsistemi.data.model.Department
import com.example.ziyaretcikayitsistemi.databinding.FragmentAddDepartmentBinding
import com.example.ziyaretcikayitsistemi.ui.viewmodel.DepartmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddDepartmentFragment : Fragment() {

    private var _binding: FragmentAddDepartmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DepartmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddDepartmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarAddDepartment.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        // Kaydet Butonu
        binding.btnSaveDepartment.setOnClickListener {
            val departmentName = binding.etDepartmentName.text.toString().trim()

            if (departmentName.isEmpty()) {
                binding.tilDepartmentName.error = "Lütfen bir departman adı girin"
                return@setOnClickListener
            }

            binding.tilDepartmentName.error = null

            val newDepartment = Department(departmentName = departmentName)
            viewModel.insertDepartment(newDepartment)

            Toast.makeText(requireContext(), "$departmentName başarıyla eklendi!", Toast.LENGTH_SHORT).show()

            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}