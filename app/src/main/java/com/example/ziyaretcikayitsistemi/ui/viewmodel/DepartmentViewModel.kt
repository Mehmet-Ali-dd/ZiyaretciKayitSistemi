package com.example.ziyaretcikayitsistemi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.ziyaretcikayitsistemi.data.model.Department
import com.example.ziyaretcikayitsistemi.data.repository.DepartmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DepartmentViewModel @Inject constructor(
    private val repository: DepartmentRepository
) : ViewModel() {

    val allDepartments = repository.getAllDepartments().asLiveData()

    fun insertDepartment(department: Department) {
        repository.insertDepartment(department)
    }

    fun deleteDepartment(department: Department) {
        repository.deleteDepartment(department.id)
    }
}