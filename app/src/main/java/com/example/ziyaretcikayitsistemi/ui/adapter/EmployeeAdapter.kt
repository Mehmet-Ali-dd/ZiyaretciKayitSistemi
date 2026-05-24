package com.example.ziyaretcikayitsistemi.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ziyaretcikayitsistemi.data.model.Employee
import com.example.ziyaretcikayitsistemi.databinding.ItemEmployeeBinding

class EmployeeAdapter(
    private val onDeleteClick: (Employee) -> Unit
) : ListAdapter<Employee, EmployeeAdapter.EmployeeViewHolder>(EmployeeDiffCallBack()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EmployeeAdapter.EmployeeViewHolder {
        val binding = ItemEmployeeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EmployeeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val currentEmployee = getItem(position)
        holder.bind(currentEmployee)
    }

    inner class EmployeeViewHolder(private val binding: ItemEmployeeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(employee: Employee) {
            binding.tvEmployeeName.text = "${employee.firstName} ${employee.lastName}"
            binding.tvEmployeeDepartment.text = employee.department

            binding.btnDeleteEmployee.setOnClickListener {
                onDeleteClick(employee)
            }
        }
    }

    class EmployeeDiffCallBack : DiffUtil.ItemCallback<Employee>() {
        override fun areItemsTheSame(oldItem: Employee, newItem: Employee): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Employee, newItem: Employee): Boolean {
            return oldItem == newItem
        }
    }
}