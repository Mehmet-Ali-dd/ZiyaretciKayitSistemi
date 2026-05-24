package com.example.ziyaretcikayitsistemi.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.ziyaretcikayitsistemi.R
import com.example.ziyaretcikayitsistemi.data.model.Employee
import com.example.ziyaretcikayitsistemi.databinding.FragmentAddEmployeeBinding
import com.example.ziyaretcikayitsistemi.ui.viewmodel.AddEmployeeViewModel
import com.example.ziyaretcikayitsistemi.ui.viewmodel.DepartmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEmployeeFragment : Fragment() {
    private var _binding: FragmentAddEmployeeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddEmployeeViewModel by viewModels()

    private val departmentViewModel: DepartmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEmployeeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeDepartments()
        setupClickListener()
    }

    private fun observeDepartments() {
        departmentViewModel.allDepartments.observe(viewLifecycleOwner) { departmentList ->
            val departmentNames = departmentList.map { it.departmentName }.toTypedArray()

            val finalNames = if (departmentNames.isEmpty()) arrayOf("Departman Bulunamadı") else departmentNames

            val adapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, R.id.tvDropdownItem, finalNames)
            binding.actvEmployeeDepartment.setAdapter(adapter)
        }
    }

    private fun setupClickListener() {
        binding.toolbarAddEmployee.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        // Kaydet Butonu
        binding.btnSaveEmployee.setOnClickListener {
            val firstName = binding.etEmployeeFirstName.text.toString().trim()
            val lastName = binding.etEmployeeLastName.text.toString().trim()
            val department = binding.actvEmployeeDepartment.text.toString().trim()

            if (firstName.isEmpty() || lastName.isEmpty() || department.isEmpty() || department == "Departman Bulunamadı") {
                Toast.makeText(requireContext(), "Lütfen tüm alanları geçerli şekilde doldurun!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newEmployee = Employee(
                firstName = firstName,
                lastName = lastName,
                department = department
            )

            viewModel.insertEmployee(newEmployee)

            Toast.makeText(requireContext(), "$firstName $lastName başarıyla eklendi!", Toast.LENGTH_SHORT).show()

            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}