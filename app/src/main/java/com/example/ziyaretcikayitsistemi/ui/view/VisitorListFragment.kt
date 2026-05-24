package com.example.ziyaretcikayitsistemi.ui.view

import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ziyaretcikayitsistemi.R
import com.example.ziyaretcikayitsistemi.databinding.FragmentVisitorListBinding
import com.example.ziyaretcikayitsistemi.ui.adapter.VisitorAdapter
import com.example.ziyaretcikayitsistemi.ui.viewmodel.VisitorListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VisitorListFragment : Fragment() {

    private var _binding: FragmentVisitorListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VisitorListViewModel by viewModels()
    private lateinit var visitorAdapter: VisitorAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVisitorListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAddVisitor.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AddVisitorFragment())
                .addToBackStack(null)
                .commit()
        }

        // Geri Tuşu Kilidi
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : androidx.activity.OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Toast.makeText(requireContext(), "Lütfen çıkış yapmak için sağ üstteki çıkış butonunu kullanın.", Toast.LENGTH_SHORT).show()
            }
        })

        // Çıkış Yap Butonu İşlemleri
        binding.btnLogout.setOnClickListener {
            val baslik = HtmlCompat.fromHtml("<font color='#000000'><b>Oturumu Kapat</b></font>", HtmlCompat.FROM_HTML_MODE_COMPACT)
            val uyariMesaji = HtmlCompat.fromHtml("<font color='#212529'>Çıkış yaptığınız takdirde oturumunuz kapatılacaktır. Onaylıyor musunuz?</font>", HtmlCompat.FROM_HTML_MODE_COMPACT)

            MaterialAlertDialogBuilder(requireContext(), R.style.BlueDialogTheme)
                .setTitle(baslik)
                .setMessage(uyariMesaji)
                .setPositiveButton("Onayla") { dialog, which ->
                    FirebaseAuth.getInstance().signOut()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, WelcomeFragment())
                        .commit()
                }
                .setNegativeButton("Vazgeç") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }

        setupRecyclerView()
        setupSearch()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        visitorAdapter = VisitorAdapter { selectedVisitor ->
            val fragment = VisitorDetailFragment()
            val bundle = Bundle()

            bundle.putString("VISITOR_ID", selectedVisitor.visitorId)
            fragment.arguments = bundle

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        binding.recyclerViewVisitors.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = visitorAdapter
        }
    }

    private fun setupSearch() {
        binding.etSearchVisitor.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: android.text.Editable?) {
                val arananKelime = s.toString().trim()
                visitorAdapter.filter(arananKelime)
            }
        })
    }

    private fun observeViewModel() {
        viewModel.visitorsWithDetails.observe(viewLifecycleOwner) { list ->
            visitorAdapter.submitData(list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}