package com.technology.landmarkremark.interfaces

import com.technology.landmarkremark.data.model.UserAddressNote

interface CreateNoteDialogListener {
    fun onCreateNote(userAddressNote: UserAddressNote)
}