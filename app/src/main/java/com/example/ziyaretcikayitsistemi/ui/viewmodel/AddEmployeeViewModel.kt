package com.example.ziyaretcikayitsistemi.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ziyaretcikayitsistemi.data.model.Employee
import com.example.ziyaretcikayitsistemi.data.repository.EmployeeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddEmployeeViewModel @Inject constructor(
    private val repository: EmployeeRepository
) : ViewModel() {

    fun insertEmployee(employee: Employee) {
        repository.insertEmployee(employee)
    }
}