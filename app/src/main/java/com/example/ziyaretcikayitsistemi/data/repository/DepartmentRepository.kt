package com.example.ziyaretcikayitsistemi.data.repository

import android.content.Context
import com.example.ziyaretcikayitsistemi.data.model.Department
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject


class DepartmentRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("departments")

    private val sharedPref = context.getSharedPreferences("FirmPrefs", Context.MODE_PRIVATE)
    private val currentFirmId = sharedPref.getString("FIRM_ID", "") ?: ""

    // Gerçek Zamanlı Veri Çekme
    fun getAllDepartments(): Flow<List<Department>> = callbackFlow {
        val listener = collection
            .whereEqualTo("firmId", currentFirmId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val departments = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Department::class.java)?.apply { id = doc.id }
                    }
                    trySend(departments)
                }
            }
        awaitClose { listener.remove() }
    }

    // Firebase'e Veri Ekleme
    fun insertDepartment(department: Department) {
        department.firmId = currentFirmId
        collection.add(department)
    }

    // Firebase'den Veri Silme
    fun deleteDepartment(departmentId: String) {
        collection.document(departmentId).delete()
    }
}