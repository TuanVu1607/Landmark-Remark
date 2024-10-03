package com.technology.landmarkremark.ui.dialog

import android.app.Activity
import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.core.widget.doAfterTextChanged
import com.technology.landmarkremark.common.extensions.setOnSingleClickListener
import com.technology.landmarkremark.data.model.UserAddressNote
import com.technology.landmarkremark.databinding.CreateNoteDialogBinding
import com.technology.landmarkremark.interfaces.CreateNoteDialogListener

class CreateNoteDialog(var activity: Activity) {
    private val builder = AlertDialog.Builder(activity)
    private var alertDialog: AlertDialog? = null
    private var listener: CreateNoteDialogListener? = null
    private var userAddressNote: UserAddressNote? = null

    fun createDialog(listener: CreateNoteDialogListener, userLocation: UserAddressNote) {
        this.listener = listener
        this.userAddressNote = userLocation
        val binding = CreateNoteDialogBinding.inflate(LayoutInflater.from(activity))
        builder.setView(binding.root)
        alertDialog = builder.create()
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(0))

        bindDataToView(binding, userLocation)
        handleEvent(binding)

        showDialog()
    }

    private fun handleEvent(binding: CreateNoteDialogBinding) {
        binding.btnCancel.setOnSingleClickListener { cancelDialog() }
        binding.btnSubmit.setOnSingleClickListener {
            if (userAddressNote != null && binding.edtNote.text.toString().isNotEmpty()) {
                listener?.onCreateNote(userAddressNote!!)
                cancelDialog()
            }else{
                binding.edtNote.error = "Please enter note"
            }
        }
        binding.edtNote.doAfterTextChanged {
            userAddressNote?.addressNoteContent = it.toString()
        }
    }

    private fun bindDataToView(binding: CreateNoteDialogBinding, userLocation: UserAddressNote) {
        binding.tvUsername.text = userLocation.userName
        binding.tvAddress.text = userLocation.address
    }

    private fun showDialog() {
        if (alertDialog == null) return
        if (!alertDialog!!.isShowing) alertDialog!!.show()
    }

    private fun cancelDialog(delayClose: Boolean = false) {
        if (delayClose) {
            Handler(Looper.getMainLooper()).postDelayed({alertDialog?.cancel()}, 600)
        } else {
            alertDialog?.cancel()
        }
    }
}