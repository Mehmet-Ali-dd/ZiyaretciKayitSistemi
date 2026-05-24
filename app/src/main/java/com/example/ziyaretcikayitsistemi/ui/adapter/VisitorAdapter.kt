package com.example.ziyaretcikayitsistemi.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ziyaretcikayitsistemi.data.model.VisitorWithDetails
import com.example.ziyaretcikayitsistemi.databinding.ItemVisitorBinding

class VisitorAdapter(
    private val onItemClick: (VisitorWithDetails) -> Unit
) : ListAdapter<VisitorWithDetails, VisitorAdapter.VisitorViewHolder>(VisitorDiffCallback()) {

    private var originalList = listOf<VisitorWithDetails>()

    inner class VisitorViewHolder(private val binding: ItemVisitorBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(visitor: VisitorWithDetails) {
            binding.tvVisitorName.text = "${visitor.visitorFirstName} ${visitor.visitorLastName}"
            binding.tvHostName.text = "Ziyaret Edilen: ${visitor.hostFirstName} ${visitor.hostLastName}"
            binding.tvVisitReason.text = "Ziyaret Sebebi: ${visitor.reasonName}"

            val safeStatus = visitor.status.uppercase()

            // Metni Türkçeleştirme
            binding.tvStatus.text = when(safeStatus) {
                "BEKLIYOR" -> "Bekliyor"
                "ICERIDE" -> "İçeride"
                "CIKIS_YAPTI" -> "Çıkış Yaptı"
                else -> visitor.status
            }


            binding.tvStatus.setTextColor(Color.parseColor("#212529"))

            binding.root.setOnClickListener {
                onItemClick(visitor)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisitorViewHolder {
        val binding = ItemVisitorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VisitorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VisitorViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    fun submitData(list: List<VisitorWithDetails>) {
        originalList = list
        submitList(list)
    }

    fun filter(query: String) {
        val filteredList = if (query.isEmpty()) {
            originalList
        } else {
            originalList.filter {
                val tamZiyaretciAdi = "${it.visitorFirstName} ${it.visitorLastName}"
                val tamCalisanAdi = "${it.hostFirstName} ${it.hostLastName}"

                tamZiyaretciAdi.contains(query, ignoreCase = true) ||
                        tamCalisanAdi.contains(query, ignoreCase = true)
            }
        }
        submitList(filteredList)
    }

    class VisitorDiffCallback: DiffUtil.ItemCallback<VisitorWithDetails>(){
        override fun areItemsTheSame(
            oldItem: VisitorWithDetails,
            newItem: VisitorWithDetails
        ): Boolean {
            return oldItem.visitorId == newItem.visitorId
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: VisitorWithDetails,
            newItem: VisitorWithDetails
        ): Boolean {
            return oldItem == newItem
        }
    }
}