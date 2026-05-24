package com.example.ziyaretcikayitsistemi.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ziyaretcikayitsistemi.data.model.VisitReason
import com.example.ziyaretcikayitsistemi.databinding.ItemReasonBinding

class ReasonAdapter(
    private val onDeleteClick: (VisitReason) -> Unit
) : ListAdapter<VisitReason, ReasonAdapter.ReasonViewHolder>(ReasonDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReasonViewHolder {
        val binding = ItemReasonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReasonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReasonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ReasonViewHolder(private val binding: ItemReasonBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(reason: VisitReason) {
            binding.tvReasonName.text = reason.reasonName
            binding.btnDeleteReason.setOnClickListener {
                onDeleteClick(reason)
            }
        }
    }

    class ReasonDiffCallBack : DiffUtil.ItemCallback<VisitReason>() {
        override fun areItemsTheSame(oldItem: VisitReason, newItem: VisitReason) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: VisitReason, newItem: VisitReason) = oldItem == newItem
    }
}