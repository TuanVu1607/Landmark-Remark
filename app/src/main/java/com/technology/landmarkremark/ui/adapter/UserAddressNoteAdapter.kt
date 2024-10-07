package com.technology.landmarkremark.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.technology.landmarkremark.BR
import com.technology.landmarkremark.R
import com.technology.landmarkremark.data.model.UserAddressNote
import com.technology.landmarkremark.databinding.ItemUserAddressNoteBinding
import com.technology.landmarkremark.interfaces.ItemClickListener

class UserAddressNoteAdapter(var itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<UserAddressNoteAdapter.ViewHolder>() {

    val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<UserAddressNote>() {
        override fun areItemsTheSame(oldItem: UserAddressNote, newItem: UserAddressNote): Boolean {
            return oldItem.addressNoteId == newItem.addressNoteId
        }

        override fun areContentsTheSame(
            oldItem: UserAddressNote,
            newItem: UserAddressNote
        ): Boolean {
            return oldItem == newItem
        }
    })

    override fun getItemCount(): Int = differ.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemUserAddressNoteBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_user_address_note,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.onBindView(item)
    }

    inner class ViewHolder(private val binding: ItemUserAddressNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBindView(data: UserAddressNote) {
            binding.setVariable(BR.userAddressNote, data)
            binding.executePendingBindings()

            itemView.setOnClickListener {
                itemClickListener.onClick(data)
            }
        }
    }
}