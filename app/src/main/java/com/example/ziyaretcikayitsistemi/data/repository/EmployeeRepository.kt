package com.example.ziyaretcikayitsistemi.data.repository

import android.content.Context
import com.example.ziyaretcikayitsistemi.data.model.Employee
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject


class EmployeeRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("employees")


    private val sharedPref = context.getSharedPreferences("FirmPrefs", Context.MODE_PRIVATE)
    private val currentFirmId = sharedPref.getString("FIRM_ID", "") ?: ""

    fun getAllEmployees(): Flow<List<Employee>> = callbackFlow {
        val listener = collection
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

    fun insertEmployee(employee: Employee) {
        employee.firmId = currentFirmId
        collection.add(employee)
    }

    fun deleteEmployee(employeeId: String) {
        collection.document(employeeId).delete()
    }
}