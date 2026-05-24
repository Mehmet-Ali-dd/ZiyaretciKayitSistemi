package com.example.ziyaretcikayitsistemi.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ziyaretcikayitsistemi.data.model.Department
import com.example.ziyaretcikayitsistemi.databinding.ItemDepartmentBinding

class DepartmentAdapter(
    private val onDeleteClick: (Department) -> Unit
) : ListAdapter<Department, DepartmentAdapter.DepartmentViewHolder>(DepartmentDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepartmentViewHolder {
        val binding = ItemDepartmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DepartmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DepartmentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DepartmentViewHolder(private val binding: ItemDepartmentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(department: Department) {

            binding.tvDepartmentName.text = department.departmentName

            binding.btnDeleteDepartment.setOnClickListener {
                onDeleteClick(department)
            }
        }
    }

    class DepartmentDiffCallBack : DiffUtil.ItemCallback<Department>() {
        override fun areItemsTheSame(oldItem: Department, newItem: Department) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Department, newItem: Department) = oldItem == newItem
    }
}