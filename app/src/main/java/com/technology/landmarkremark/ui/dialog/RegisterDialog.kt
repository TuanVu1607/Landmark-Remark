package com.technology.landmarkremark.ui.dialog

import android.app.Activity
import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import com.technology.landmarkremark.common.extensions.setOnSingleClickListener
import com.technology.landmarkremark.databinding.RegisterDialogBinding
import com.technology.landmarkremark.interfaces.RegisterDialogListener

class RegisterDialog(var activity: Activity) {
    private val builder = AlertDialog.Builder(activity)
    private var alertDialog: AlertDialog? = null
    private var listener: RegisterDialogListener? = null

    fun createDialog(listener: RegisterDialogListener) {
        this.listener = listener
        val binding = RegisterDialogBinding.inflate(LayoutInflater.from(activity))
        builder.setView(binding.root)
        alertDialog = builder.create()
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(0))

        binding.btnCancel.setOnSingleClickListener {
            listener.onAction(false)
            cancelDialog()
        }

        binding.btnSubmit.setOnSingleClickListener {
            listener.onAction(true)
            cancelDialog()
        }

        showDialog()
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