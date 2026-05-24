package com.example.ziyaretcikayitsistemi.data.repository

import android.content.Context
import com.example.ziyaretcikayitsistemi.data.model.VisitReason
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class VisitReasonRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("reasons")

    private val sharedPref = context.getSharedPreferences("FirmPrefs", Context.MODE_PRIVATE)
    private val currentFirmId = sharedPref.getString("FIRM_ID", "") ?: ""

    fun getAllReasons(): Flow<List<VisitReason>> = callbackFlow {
        val listener = collection
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

    fun insertReason(reason: VisitReason) {
        reason.firmId = currentFirmId
        collection.add(reason)
    }

    fun deleteReason(reasonId: String) {
        collection.document(reasonId).delete()
    }
}