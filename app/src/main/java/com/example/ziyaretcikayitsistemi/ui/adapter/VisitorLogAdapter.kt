package com.example.ziyaretcikayitsistemi.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ziyaretcikayitsistemi.data.model.VisitorWithDetails
import com.example.ziyaretcikayitsistemi.databinding.ItemVisitorLogBinding

class VisitorLogAdapter : ListAdapter<VisitorWithDetails, VisitorLogAdapter.LogViewHolder>(DiffCallback()) {

    class LogViewHolder(val binding: ItemVisitorLogBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val binding = ItemVisitorLogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.apply {
            val dateFormat = java.text.SimpleDateFormat("dd.MM.yyyy HH:mm", java.util.Locale.getDefault())
            val okunabilirTarih = dateFormat.format(java.util.Date(item.registeredAt))
            tvLogDate.text = okunabilirTarih

            tvLogVisitor.text = "${item.visitorFirstName} ${item.visitorLastName}"
            tvLogHost.text = "${item.hostFirstName} ${item.hostLastName}"

            val guncelDurum = item.status.trim()

            tvLogStatus.text = when(guncelDurum) {
                "Bekliyor", "BEKLIYOR" -> "Bekliyor"
                "İçeride", "ICERIDE", "İÇERİDE" -> "İçeride"
                "Çıkış Yaptı", "CIKIS_YAPTI", "ÇIKIŞ YAPTI" -> "Çıkış Yaptı"
                else -> guncelDurum
            }

            tvLogStatus.setTextColor(
                when(guncelDurum) {
                    "Bekliyor", "BEKLIYOR" -> android.graphics.Color.parseColor("#1565C0") // Mavi
                    "İçeride", "ICERIDE", "İÇERİDE" -> android.graphics.Color.parseColor("#2E7D32")  // Yeşil
                    "Çıkış Yaptı", "CIKIS_YAPTI", "ÇIKIŞ YAPTI" -> android.graphics.Color.parseColor("#D32F2F") // Kırmızı
                    else -> android.graphics.Color.parseColor("#212529") // Koyu Renk
                }
            )
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<VisitorWithDetails>() {
        override fun areItemsTheSame(oldItem: VisitorWithDetails, newItem: VisitorWithDetails): Boolean {
            return oldItem.visitorId == newItem.visitorId
        }

        override fun areContentsTheSame(oldItem: VisitorWithDetails, newItem: VisitorWithDetails): Boolean {
            return oldItem == newItem
        }
    }
}