package com.technology.landmarkremark.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.technology.landmarkremark.R
import com.technology.landmarkremark.data.entity.AddressNoteEntity
import com.technology.landmarkremark.databinding.ItemAddressNoteBinding
import com.technology.landmarkremark.interfaces.ItemClickListener
import com.technology.landmarkremark.BR

class NoteAdapter(var itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    private val TAG = NoteAdapter::class.java.simpleName

    val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<AddressNoteEntity>() {
        override fun areItemsTheSame(
            oldItem: AddressNoteEntity,
            newItem: AddressNoteEntity
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: AddressNoteEntity,
            newItem: AddressNoteEntity
        ): Boolean {
            return oldItem == newItem
        }
    })

    override fun getItemCount(): Int = differ.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemAddressNoteBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_address_note,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.onBindView(item)
    }

    inner class ViewHolder(private val binding: ItemAddressNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBindView(data: AddressNoteEntity) {
            binding.setVariable(BR.addressNote, data)
            binding.executePendingBindings()

            itemView.setOnClickListener {
                itemClickListener.onClick(data)
            }
        }
    }
}