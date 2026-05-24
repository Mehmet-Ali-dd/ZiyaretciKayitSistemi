package com.example.ziyaretcikayitsistemi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.ziyaretcikayitsistemi.data.model.VisitReason
import com.example.ziyaretcikayitsistemi.data.repository.VisitReasonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReasonListViewModel @Inject constructor(
    private val repository: VisitReasonRepository
) : ViewModel() {

    val allReasons = repository.getAllReasons().asLiveData()

    fun deleteReason(reason: VisitReason) {
        repository.deleteReason(reason.id)
    }
}