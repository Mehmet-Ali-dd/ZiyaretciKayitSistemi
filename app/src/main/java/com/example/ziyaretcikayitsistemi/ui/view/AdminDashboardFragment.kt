package com.example.ziyaretcikayitsistemi.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.example.ziyaretcikayitsistemi.R
import com.example.ziyaretcikayitsistemi.databinding.FragmentAdminDashboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminDashboardFragment : Fragment() {

    private var _binding: FragmentAdminDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminDashboardBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardEmployeeManagement.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, EmployeeListFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.cardReasonManagement.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ReasonListFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.cardDepartmentManagement.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, DepartmentListFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.cardVisitorLogs.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AdminVisitorLogFragment())
                .addToBackStack(null)
                .commit()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : androidx.activity.OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Burası boş bırakıldığı için geri tuşuna basıldığında hiçbir şey olmayacak.
                // Kullanıcı sadece "Çıkış Yap" butonuna basarak sayfadan ayrılabilir.
                android.widget.Toast.makeText(requireContext(), "Lütfen çıkış yapmak için aşağıdaki butonu kullanın.", android.widget.Toast.LENGTH_SHORT).show()
            }
        })

        binding.btnLogout.setOnClickListener {

            val baslik = HtmlCompat.fromHtml("<font color='#000000'><b>Oturumu Kapat</b></font>", HtmlCompat.FROM_HTML_MODE_COMPACT)
            val uyariMesaji = HtmlCompat.fromHtml("<font color='#212529'>Çıkış yaptığınız takdirde oturumunuz kapatılacaktır. Onaylıyor musunuz?</font>", HtmlCompat.FROM_HTML_MODE_COMPACT)

            com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext(), R.style.BlueDialogTheme)
                .setTitle(baslik)
                .setMessage(uyariMesaji)
                .setPositiveButton("Onayla") { dialog, which ->

                    com.google.firebase.auth.FirebaseAuth.getInstance().signOut()

                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, WelcomeFragment()) // veya LoginFragment
                        .commit()
                }
                .setNegativeButton("Vazgeç") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}