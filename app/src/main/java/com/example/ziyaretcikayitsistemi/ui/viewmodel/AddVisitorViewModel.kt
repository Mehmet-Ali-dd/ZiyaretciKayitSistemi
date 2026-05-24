package com.example.ziyaretcikayitsistemi.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ziyaretcikayitsistemi.data.model.Visitor
import com.example.ziyaretcikayitsistemi.data.repository.VisitorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddVisitorViewModel @Inject constructor(
    private val repository: VisitorRepository
) : ViewModel() {

    fun insertVisitor(visitor: Visitor) {
        repository.insertVisitor(visitor)
    }
}