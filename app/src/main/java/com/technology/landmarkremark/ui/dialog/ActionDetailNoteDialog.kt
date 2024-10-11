package com.technology.landmarkremark.ui.dialog

import android.app.Activity
import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.technology.landmarkremark.R
import com.technology.landmarkremark.common.enums.ActionDetailEnum
import com.technology.landmarkremark.common.extensions.setOnSingleClickListener
import com.technology.landmarkremark.data.model.UserAddressNote
import com.technology.landmarkremark.databinding.CreateNoteDialogBinding
import com.technology.landmarkremark.interfaces.ActionDetailNoteDialogListener

class ActionDetailNoteDialog(var activity: Activity) {
    private val builder = AlertDialog.Builder(activity)
    private var alertDialog: AlertDialog? = null
    private var listener: ActionDetailNoteDialogListener? = null
    private var userAddressNote: UserAddressNote? = null
    private var action = ActionDetailEnum.DETAIL

    fun createDialog(
        listener: ActionDetailNoteDialogListener,
        userLocation: UserAddressNote,
        action: ActionDetailEnum
    ) {
        this.listener = listener
        this.userAddressNote = userLocation
        this.action = action
        val binding = CreateNoteDialogBinding.inflate(LayoutInflater.from(activity))
        builder.setView(binding.root)
        alertDialog = builder.create()
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(0))

        bindDataToView(binding, userLocation)
        handleEvent(binding)

        showDialog()
    }

    private fun bindDataToView(binding: CreateNoteDialogBinding, userLocation: UserAddressNote) {
        binding.tvHeader.text = when (action) {
            ActionDetailEnum.CREATE -> activity.getString(R.string.create_note)
            ActionDetailEnum.EDIT -> activity.getString(R.string.edit_note)
            else -> activity.getString(R.string.detail_note)
        }
        binding.tvUsername.text = userLocation.userName
        binding.tvAddress.text = userLocation.address
        binding.edtNote.setText(userLocation.addressNoteContent)
        binding.edtNote.isVisible = action == ActionDetailEnum.EDIT || action == ActionDetailEnum.CREATE
        binding.btnCancel.isVisible = action == ActionDetailEnum.EDIT || action == ActionDetailEnum.CREATE
        binding.btnSubmit.isVisible = action == ActionDetailEnum.EDIT || action == ActionDetailEnum.CREATE
        binding.tvNote.isVisible = action == ActionDetailEnum.DETAIL
        binding.btnClose.isVisible = action == ActionDetailEnum.DETAIL
    }

    private fun handleEvent(binding: CreateNoteDialogBinding) {
        binding.btnCancel.setOnSingleClickListener { cancelDialog() }
        binding.btnClose.setOnSingleClickListener { cancelDialog() }
        binding.btnSubmit.setOnSingleClickListener {
            if (userAddressNote != null && binding.edtNote.text.toString().isNotEmpty()) {
                handleActionSubmit()
            } else {
                binding.edtNote.error = activity.getString(R.string.note_input_empty)
            }
        }
        binding.edtNote.doAfterTextChanged {
            userAddressNote?.addressNoteContent = it.toString()
        }
    }

    private fun handleActionSubmit() {
        userAddressNote?.let {
            when (action) {
                ActionDetailEnum.CREATE -> {
                    listener?.onCreateNote(it)
                }

                ActionDetailEnum.EDIT -> {
                    listener?.onEditNote(it)
                }

                else -> {}
            }
        }
        cancelDialog()
    }

    private fun showDialog() {
        if (alertDialog == null) return
        if (!alertDialog!!.isShowing) alertDialog!!.show()
    }

    private fun cancelDialog(delayClose: Boolean = false) {
        if (delayClose) {
            Handler(Looper.getMainLooper()).postDelayed({ alertDialog?.cancel() }, 600)
        } else {
            alertDialog?.cancel()
        }
    }
}