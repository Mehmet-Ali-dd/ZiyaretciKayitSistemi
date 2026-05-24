package com.example.ziyaretcikayitsistemi.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ziyaretcikayitsistemi.data.repository.VisitorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VisitorDetailViewModel @Inject constructor(
    private val repository: VisitorRepository
) : ViewModel() {

    fun updateVisitorStatus(visitorId: String, newStatus: String) {
        repository.updateVisitorStatus(visitorId, newStatus)
    }
}