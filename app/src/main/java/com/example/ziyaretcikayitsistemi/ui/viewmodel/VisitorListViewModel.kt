package com.example.ziyaretcikayitsistemi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.ziyaretcikayitsistemi.data.repository.VisitorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VisitorListViewModel @Inject constructor(
    private val repository: VisitorRepository
) : ViewModel() {

    val visitorsWithDetails = repository.getVisitorsWithDetails().asLiveData()
}