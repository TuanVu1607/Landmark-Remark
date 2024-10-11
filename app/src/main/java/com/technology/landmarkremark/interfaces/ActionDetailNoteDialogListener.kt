package com.technology.landmarkremark.interfaces

import com.technology.landmarkremark.data.model.UserAddressNote

interface ActionDetailNoteDialogListener {
    fun onCreateNote(userAddressNote: UserAddressNote)
    fun onEditNote(userAddressNote: UserAddressNote)
}