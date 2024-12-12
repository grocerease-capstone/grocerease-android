package com.exal.grocerease.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.exal.grocerease.databinding.ItemNotificationBinding
import com.exal.grocerease.model.network.response.RequestDetailItem
import com.exal.grocerease.viewmodel.NotificationViewModel

class NotificationAdapter(private val viewModel: NotificationViewModel) :
    ListAdapter<RequestDetailItem, NotificationAdapter.ItemViewHolder>(DIFF_CALLBACK) {

    inner class ItemViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RequestDetailItem, viewModel: NotificationViewModel) {
            binding.contentText.text = buildString {
                append(item.username)
                append(" membagikan daftar pengeluran : ")
                append(item.title)
            }
            binding.acceptBtn.setOnClickListener {
                viewModel.acceptNotification(item.id!!)
            }
            binding.declineBtn.setOnClickListener {
                viewModel.declineNotification(item.id!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, viewModel)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RequestDetailItem>() {
            override fun areItemsTheSame(
                oldItem: RequestDetailItem,
                newItem: RequestDetailItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: RequestDetailItem,
                newItem: RequestDetailItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}