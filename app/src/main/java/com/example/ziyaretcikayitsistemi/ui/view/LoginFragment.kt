package com.example.ziyaretcikayitsistemi.ui.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ziyaretcikayitsistemi.R
import com.example.ziyaretcikayitsistemi.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences("FirmPrefs", Context.MODE_PRIVATE)
        val currentFirmId = sharedPref.getString("FIRM_ID", null)

        if (currentFirmId != null) {
            db.collection("firmalar").document(currentFirmId).get().addOnSuccessListener { document ->
                val firmName = document.getString("firmName") ?: "Firma"
                binding.tvFirmTitle.text = "$firmName Ziyaretçi Paneli"
            }
        }

        binding.toolbarLogin.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.btnLogoutFirm.setOnClickListener {
            sharedPref.edit().clear().apply()
            auth.signOut()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, WelcomeFragment())
                .commit()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim().lowercase()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Lütfen e-posta ve şifre girin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val expectedRole = if (binding.toggleRole.checkedButtonId == R.id.btnRoleAdmin) {
                "ADMIN"
            } else {
                "SECURITY"
            }

            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {

                db.collection("users").document(email).get().addOnSuccessListener { document ->
                    if (document.exists()) {
                        val userRole = document.getString("role")
                        val userFirmId = document.getString("firmId")

                        if (userFirmId == currentFirmId) {

                            if (userRole == expectedRole) {
                                Toast.makeText(requireContext(), "Giriş Başarılı!", Toast.LENGTH_SHORT).show()


                                val destinationFragment = if (userRole == "ADMIN") {
                                    AdminDashboardFragment()
                                } else {
                                    VisitorListFragment()
                                }

                                requireActivity().supportFragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, destinationFragment)
                                    .commit()

                            } else {
                                val rolAdi = if (userRole == "ADMIN") "Yönetici" else "Danışma"
                                Toast.makeText(requireContext(), "Yetkisiz Giriş! Sizin hesabınız bir $rolAdi hesabıdır.", Toast.LENGTH_LONG).show()
                                auth.signOut()
                            }

                        } else {
                            Toast.makeText(requireContext(), "Güvenlik İhlali: Bu hesap başka bir firmaya ait!", Toast.LENGTH_LONG).show()
                            auth.signOut()
                        }
                    } else {
                        Toast.makeText(requireContext(), "Kullanıcı veritabanında bulunamadı.", Toast.LENGTH_SHORT).show()
                        auth.signOut()
                    }
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "Veritabanı bağlantı hatası.", Toast.LENGTH_SHORT).show()
                    auth.signOut()
                }

            }.addOnFailureListener {
                Toast.makeText(requireContext(), "E-posta veya şifre hatalı!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}