package com.example.ziyaretcikayitsistemi.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ziyaretcikayitsistemi.R
import com.example.ziyaretcikayitsistemi.databinding.FragmentFirmRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@AndroidEntryPoint
class FirmRegisterFragment : Fragment() {

    private var _binding: FragmentFirmRegisterBinding? = null
    private val binding get() = _binding!!

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFirmRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarRegister.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.btnCompleteRegister.setOnClickListener {
            val firmName = binding.etFirmName.text.toString().trim()
            val contactName = binding.etContactName.text.toString().trim()
            val firmEmail = binding.etFirmEmail.text.toString().trim().lowercase()
            val firmPassword = binding.etFirmPassword.text.toString().trim()

            val adminEmail = binding.etAdminEmail.text.toString().trim().lowercase()
            val adminPassword = binding.etAdminPassword.text.toString().trim()

            val securityEmail = binding.etSecurityEmail.text.toString().trim().lowercase()
            val securityPassword = binding.etSecurityPassword.text.toString().trim()

            // Güvenlik Kontrolü
            if (firmName.isEmpty() || contactName.isEmpty() || firmEmail.isEmpty() || firmPassword.isEmpty() ||
                adminEmail.isEmpty() || adminPassword.isEmpty() || securityEmail.isEmpty() || securityPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Lütfen tüm alanları doldurun!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(firmEmail, firmPassword).addOnSuccessListener { firmAuthResult ->
                val firmUid = firmAuthResult.user?.uid ?: return@addOnSuccessListener

                val firmData = hashMapOf(
                    "firmName" to firmName,
                    "contactName" to contactName,
                    "firmEmail" to firmEmail,
                    "adminEmail" to adminEmail,
                    "securityEmail" to securityEmail,
                    "createdAt" to com.google.firebase.Timestamp.now()
                )

                db.collection("firmalar").document(firmUid).set(firmData).addOnSuccessListener {

                    auth.createUserWithEmailAndPassword(adminEmail, adminPassword).addOnSuccessListener { adminAuthResult ->

                        auth.createUserWithEmailAndPassword(securityEmail, securityPassword).addOnSuccessListener { securityAuthResult ->

                            val roles = listOf(
                                hashMapOf("email" to firmEmail, "role" to "FIRM", "firmId" to firmUid), // Ana Firma Rolü
                                hashMapOf("email" to adminEmail, "role" to "ADMIN", "firmId" to firmUid),
                                hashMapOf("email" to securityEmail, "role" to "SECURITY", "firmId" to firmUid)
                            )

                            val batch = db.batch()
                            roles.forEach { roleMap ->
                                val docRef = db.collection("users").document(roleMap["email"] as String)
                                batch.set(docRef, roleMap)
                            }

                            batch.commit().addOnSuccessListener {
                                Toast.makeText(requireContext(),"Firma ve alt hesaplar başarıyla oluşturuldu!", Toast.LENGTH_LONG).show()

                                auth.signOut()
                                requireActivity().supportFragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, WelcomeFragment())
                                    .commit()

                            }.addOnFailureListener { e ->
                                Toast.makeText(requireContext(), "Kullanıcı rolleri kaydedilemedi: ${e.message}", Toast.LENGTH_SHORT).show()
                            }

                        }.addOnFailureListener { e ->
                            Toast.makeText(requireContext(), "Danışma kaydı hatası: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener { e ->
                        Toast.makeText(requireContext(), "Yönetici kaydı hatası: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Firma veritabanı hatası: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Firma (Ana Hesap) kaydı hatası: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}