package com.example.ziyaretcikayitsistemi.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.ziyaretcikayitsistemi.data.model.VisitReason
import com.example.ziyaretcikayitsistemi.databinding.FragmentAddReasonBinding
import com.example.ziyaretcikayitsistemi.ui.viewmodel.AddReasonViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddReasonFragment : Fragment() {

    private var _binding: FragmentAddReasonBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddReasonViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddReasonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarAddReason.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.btnSaveReason.setOnClickListener {
            val reasonName = binding.etReasonName.text.toString().trim()

            if (reasonName.isEmpty()) {
                Toast.makeText(requireContext(), "Sebep adı boş bırakılmaz!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newReason = VisitReason(reasonName = reasonName)

            viewModel.insertReason(newReason)

            Toast.makeText(requireContext(), "Ziyaret Sebebi başarıyla eklendi!", Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}