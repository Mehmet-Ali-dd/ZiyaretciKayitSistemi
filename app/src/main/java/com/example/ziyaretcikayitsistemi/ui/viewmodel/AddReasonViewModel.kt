package com.example.ziyaretcikayitsistemi.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ziyaretcikayitsistemi.data.model.VisitReason
import com.example.ziyaretcikayitsistemi.data.repository.VisitReasonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddReasonViewModel @Inject constructor(
    private val repository: VisitReasonRepository
) : ViewModel() {

    fun insertReason(reason: VisitReason) {
        repository.insertReason(reason)
    }
}