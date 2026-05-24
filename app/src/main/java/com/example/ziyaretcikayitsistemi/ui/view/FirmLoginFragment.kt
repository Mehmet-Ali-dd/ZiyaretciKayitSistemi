package com.example.ziyaretcikayitsistemi.ui.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ziyaretcikayitsistemi.R
import com.example.ziyaretcikayitsistemi.databinding.FragmentFirmLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirmLoginFragment : Fragment() {

    private var _binding: FragmentFirmLoginBinding? = null
    private val binding get() = _binding!!

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirmLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Geri dönüş butonu
        binding.toolbarFirmLogin.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.btnFirmLogin.setOnClickListener {
            val email = binding.etFirmLoginEmail.text.toString().trim().lowercase()
            val password = binding.etFirmLoginPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Lütfen e-posta ve şifrenizi girin.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 1. Firebase Auth ile Giriş
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {

                db.collection("firmalar")
                    .whereEqualTo("firmEmail", email)
                    .get()
                    .addOnSuccessListener { querySnapshot ->

                        if (!querySnapshot.isEmpty) {
                            val firmId = querySnapshot.documents[0].id

                           // Cihaz yetkilendirme
                            val sharedPref = requireActivity().getSharedPreferences("FirmPrefs", Context.MODE_PRIVATE)
                            with(sharedPref.edit()) {
                                putString("FIRM_ID", firmId)
                                apply()
                            }

                            Toast.makeText(requireContext(), "Cihaz başarıyla yetkilendirildi!", Toast.LENGTH_SHORT).show()

                            // Ana firmanın Firebase oturumunu kapatma işlemi
                            auth.signOut()

                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, LoginFragment())
                                .commit()

                        } else {
                            Toast.makeText(requireContext(), "Hata: Bu hesap bir ana firma hesabı değil!", Toast.LENGTH_LONG).show()
                            auth.signOut()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(requireContext(), "Sunucu bağlantı hatası.", Toast.LENGTH_SHORT).show()
                        auth.signOut()
                    }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Giriş başarısız: E-posta veya şifre hatalı.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}