package com.technology.landmarkremark.ui.dialog

import android.app.Activity
import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import com.technology.landmarkremark.R
import com.technology.landmarkremark.common.extensions.setOnSingleClickListener
import com.technology.landmarkremark.databinding.ConfirmDialogBinding
import com.technology.landmarkremark.interfaces.ConfirmDialogListener

class ConfirmDialog(var activity: Activity) {
    private val builder = AlertDialog.Builder(activity)
    private var alertDialog: AlertDialog? = null
    private var listener: ConfirmDialogListener? = null

    fun createDialog(
        title: String,
        message: String,
        listener: ConfirmDialogListener,
        textBtnPositive: String = activity.getString(R.string.confirm),
        textBtnNegative: String = activity.getString(R.string.cancel)
    ) {
        this.listener = listener
        val binding = ConfirmDialogBinding.inflate(LayoutInflater.from(activity))
        builder.setView(binding.root)
        alertDialog = builder.create()
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(0))

        bindDataToView(binding, title, message, textBtnPositive, textBtnNegative)
        handleEvent(binding)

        showDialog()
    }

    private fun bindDataToView(
        binding: ConfirmDialogBinding,
        title: String,
        message: String,
        textBtnPositive: String,
        textBtnNegative: String
    ) {
        binding.tvHeader.text = title
        binding.tvMessage.text = message
        binding.btnSubmit.text = textBtnPositive
        binding.btnCancel.text = textBtnNegative
    }

    private fun handleEvent(binding: ConfirmDialogBinding) {
        binding.btnCancel.setOnSingleClickListener {
            listener?.onAction(false)
            cancelDialog()
        }

        binding.btnSubmit.setOnSingleClickListener {
            listener?.onAction(true)
            cancelDialog()
        }
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