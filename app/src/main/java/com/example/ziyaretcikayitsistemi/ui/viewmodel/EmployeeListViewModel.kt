package com.example.ziyaretcikayitsistemi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.ziyaretcikayitsistemi.data.model.Employee
import com.example.ziyaretcikayitsistemi.data.repository.EmployeeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EmployeeListViewModel @Inject constructor(
    private val repository: EmployeeRepository
) : ViewModel() {

    val allEmployees = repository.getAllEmployees().asLiveData()

    fun deleteEmployee(employee: Employee) {
        repository.deleteEmployee(employee.id)
    }
}