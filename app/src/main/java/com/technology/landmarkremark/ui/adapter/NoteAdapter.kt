package com.technology.landmarkremark.ui.adapter

import com.technology.landmarkremark.data.entity.AddressNoteEntity
import com.technology.landmarkremark.databinding.ItemAddressNoteBinding
import com.technology.landmarkremark.interfaces.ItemClickListener
import com.technology.landmarkremark.ui.base.BaseAdapter

class NoteAdapter(data: List<AddressNoteEntity>, itemClickListener: ItemClickListener) :
    BaseAdapter<ItemAddressNoteBinding, AddressNoteEntity>(
        ItemAddressNoteBinding::inflate,
        data,
        itemClickListener
    ) {
    private val TAG = NoteAdapter::class.java.simpleName

    override fun onBindViewHolder(holder: BaseViewHolder<ItemAddressNoteBinding>, position: Int) {
        super.onBindViewHolder(holder, position)

    }
}