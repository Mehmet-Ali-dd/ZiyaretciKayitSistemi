package com.example.ziyaretcikayitsistemi.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.ziyaretcikayitsistemi.R
import com.example.ziyaretcikayitsistemi.databinding.FragmentVisitorDetailBinding
import com.example.ziyaretcikayitsistemi.ui.viewmodel.VisitorDetailViewModel
import com.example.ziyaretcikayitsistemi.ui.viewmodel.VisitorListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VisitorDetailFragment : Fragment() {

    private var _binding: FragmentVisitorDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VisitorDetailViewModel by viewModels()

    private val listViewModel: VisitorListViewModel by viewModels()

    private var visitorId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVisitorDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        visitorId = arguments?.getString("VISITOR_ID", "") ?: ""

        binding.toolbarDetail.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        setupClickListeners()
        observeVisitorDetails()
    }

    private fun setupClickListeners() {
        binding.btnUpdateStatus.setOnClickListener {
            val statusOptions = arrayOf("Bekliyor", "İçeride", "Çıkış Yaptı")

            val currentStatus = binding.tvDetailStatus.text.toString()
            val checkedItemIndex = statusOptions.indexOf(currentStatus).takeIf { it >= 0 } ?: 0

            var selectedStatus = statusOptions[checkedItemIndex]

            MaterialAlertDialogBuilder(requireContext(), R.style.BlueDialogTheme)
                .setTitle("Ziyaretçi Durumunu Güncelle")
                .setSingleChoiceItems(statusOptions, checkedItemIndex) { _, which ->
                    selectedStatus = statusOptions[which]
                }
                .setPositiveButton("Kaydet") { dialog, _ ->
                    if (selectedStatus == "Çıkış Yaptı" && currentStatus != "Çıkış Yaptı") {
                        MaterialAlertDialogBuilder(requireContext(), R.style.BlueDialogTheme)
                            .setTitle("Uyarı!")
                            .setMessage("Ziyaretçi durumu 'Çıkış Yaptı' olarak kaydederseniz bu işlemi geri alamazsınız. Onaylıyor musunuz?")
                            .setPositiveButton("Onayla") { confirmDialog, _ ->
                                viewModel.updateVisitorStatus(visitorId, selectedStatus)
                                confirmDialog.dismiss()
                            }
                            .setNegativeButton("Vazgeç") { confirmDialog, _ ->
                                confirmDialog.dismiss()
                            }
                            .show()
                    } else {
                        viewModel.updateVisitorStatus(visitorId, selectedStatus)
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("İptal") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun observeVisitorDetails() {
        if (visitorId.isNotEmpty()) {
            listViewModel.visitorsWithDetails.observe(viewLifecycleOwner) { logListesi ->
                val visitor = logListesi.find { it.visitorId == visitorId }

                visitor?.let {
                    binding.tvDetailVisitorName.text = "${it.visitorFirstName} ${it.visitorLastName}"
                    binding.tvDetailHostName.text = "${it.hostFirstName} ${it.hostLastName}"
                    binding.tvDetailReason.text = it.reasonName
                    val safeStatus = it.status.uppercase()
                    binding.tvDetailStatus.text = when(safeStatus) {
                        "BEKLIYOR" -> "Bekliyor"
                        "ICERIDE" -> "İçeride"
                        "CIKIS_YAPTI" -> "Çıkış Yaptı"
                        else -> it.status
                    }

                    if (it.status == "Çıkış Yaptı") {
                        binding.btnUpdateStatus.isEnabled = false
                        binding.btnUpdateStatus.text = "Ziyaret Sonlandı"
                    } else {
                        binding.btnUpdateStatus.isEnabled = true
                        binding.btnUpdateStatus.text = "Durumu Güncelle"
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}