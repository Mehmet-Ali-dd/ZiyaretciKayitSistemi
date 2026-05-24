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
import com.example.ziyaretcikayitsistemi.data.model.VisitReason
import com.example.ziyaretcikayitsistemi.data.model.Visitor
import com.example.ziyaretcikayitsistemi.databinding.FragmentAddVisitorBinding
import com.example.ziyaretcikayitsistemi.ui.viewmodel.AddVisitorViewModel
import com.example.ziyaretcikayitsistemi.ui.viewmodel.EmployeeListViewModel
import com.example.ziyaretcikayitsistemi.ui.viewmodel.ReasonListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddVisitorFragment : Fragment() {

    private var _binding: FragmentAddVisitorBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddVisitorViewModel by viewModels()

    private val employeeViewModel: EmployeeListViewModel by viewModels()
    private val reasonViewModel: ReasonListViewModel by viewModels()

    private var currentEmployees: List<Employee> = emptyList()
    private var currentReasons: List<VisitReason> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddVisitorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        observeData()
        setupSaveButton()
    }

    private fun setupToolbar() {
        binding.toolbarAddVisitor.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun observeData() {
        employeeViewModel.allEmployees.observe(viewLifecycleOwner) { employees ->
            currentEmployees = employees
            val employeeDisplayList = employees.map { "${it.firstName} ${it.lastName} (${it.department})" }
            val hostAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, R.id.tvDropdownItem, employeeDisplayList)
            binding.actvHostEmployee.setAdapter(hostAdapter)
        }


        reasonViewModel.allReasons.observe(viewLifecycleOwner) { reasons ->
            currentReasons = reasons
            val reasonList = reasons.map { it.reasonName }
            val reasonAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, R.id.tvDropdownItem, reasonList)
            binding.actvVisitReason.setAdapter(reasonAdapter)
        }
    }

    private fun setupSaveButton() {
        binding.btnSaveVisitor.setOnClickListener {
            val fName = binding.etVisitorFirstName.text.toString().trim()
            val lName = binding.etVisitorLastName.text.toString().trim()
            val phone = binding.etVisitorPhone.text.toString().trim()
            val selectedEmployeeText = binding.actvHostEmployee.text.toString()
            val selectedReasonText = binding.actvVisitReason.text.toString()

            // Boş alan kontrolü
            if (fName.isEmpty() || lName.isEmpty() || phone.isEmpty() || selectedEmployeeText.isEmpty() || selectedReasonText.isEmpty()) {
                Toast.makeText(requireContext(), "Lütfen tüm alanları doldurun!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val matchedEmployee = currentEmployees.find {
                "${it.firstName} ${it.lastName} (${it.department})" == selectedEmployeeText
            }
            val matchedReason = currentReasons.find {
                it.reasonName == selectedReasonText
            }

            if (matchedEmployee == null || matchedReason == null) {
                Toast.makeText(requireContext(), "Lütfen listeden geçerli bir çalışan ve sebep seçin!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newVisitor = Visitor(
                firstName = fName,
                lastName = lName,
                phoneNumber = phone,
                hostEmployeeId = matchedEmployee.id,
                reasonId = matchedReason.id,
                status = "Bekliyor"                  // Varsayılan başlangıç durumu
            )

            viewModel.insertVisitor(newVisitor)

            Toast.makeText(requireContext(), "Ziyaretçi kaydı başarılı!", Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}