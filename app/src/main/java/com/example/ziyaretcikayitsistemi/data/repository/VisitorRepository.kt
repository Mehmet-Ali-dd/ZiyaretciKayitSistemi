package com.example.ziyaretcikayitsistemi.data.repository

import android.content.Context
import com.example.ziyaretcikayitsistemi.data.model.Employee
import com.example.ziyaretcikayitsistemi.data.model.VisitReason
import com.example.ziyaretcikayitsistemi.data.model.Visitor
import com.example.ziyaretcikayitsistemi.data.model.VisitorWithDetails
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class VisitorRepository @Inject constructor(
    @ApplicationContext private val context: Context
){
    private val db = FirebaseFirestore.getInstance()

    private val sharedPref = context.getSharedPreferences("FirmPrefs", Context.MODE_PRIVATE)
    private val currentFirmId = sharedPref.getString("FIRM_ID", "") ?: ""

    private fun getRawVisitors(): Flow<List<Visitor>> = callbackFlow {
        val listener = db.collection("visitors")
            .whereEqualTo("firmId", currentFirmId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { close(error); return@addSnapshotListener }
                snapshot?.let {
                    val visitors = it.documents.mapNotNull { doc ->
                        doc.toObject(Visitor::class.java)?.apply { id = doc.id }
                    }
                    trySend(visitors)
                }
            }
        awaitClose { listener.remove() }
    }

    private fun getRawEmployees(): Flow<List<Employee>> = callbackFlow {
        val listener = db.collection("employees")
            .whereEqualTo("firmId", currentFirmId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { close(error); return@addSnapshotListener }
                snapshot?.let {
                    val employees = it.documents.mapNotNull { doc ->
                        doc.toObject(Employee::class.java)?.apply { id = doc.id }
                    }
                    trySend(employees)
                }
            }
        awaitClose { listener.remove() }
    }

    private fun getRawReasons(): Flow<List<VisitReason>> = callbackFlow {
        val listener = db.collection("reasons")
            .whereEqualTo("firmId", currentFirmId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { close(error); return@addSnapshotListener }
                snapshot?.let {
                    val reasons = it.documents.mapNotNull { doc ->
                        doc.toObject(VisitReason::class.java)?.apply { id = doc.id }
                    }
                    trySend(reasons)
                }
            }
        awaitClose { listener.remove() }
    }

    fun getVisitorsWithDetails(): Flow<List<VisitorWithDetails>> {
        return combine(getRawVisitors(), getRawEmployees(), getRawReasons()) { visitors, employees, reasons ->
            visitors.map { visitor ->
                val employee = employees.find { it.id == visitor.hostEmployeeId }
                val reason = reasons.find { it.id == visitor.reasonId }

                VisitorWithDetails(
                    visitorId = visitor.id,
                    visitorFirstName = visitor.firstName,
                    visitorLastName = visitor.lastName,
                    status = visitor.status,
                    hostFirstName = employee?.firstName ?: "Bilinmeyen",
                    hostLastName = employee?.lastName ?: "Çalışan",
                    reasonName = reason?.reasonName ?: "Belirtilmemiş",
                    registeredAt = visitor.registeredAt
                )
            }.sortedByDescending { it.registeredAt }
        }
    }


    fun insertVisitor(visitor: Visitor) {
        visitor.firmId = currentFirmId
        db.collection("visitors").add(visitor)
    }

    fun updateVisitorStatus(visitorId: String, newStatus: String) {
        db.collection("visitors").document(visitorId).update("status", newStatus)
    }
}