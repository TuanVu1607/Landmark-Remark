package com.technology.landmarkremark.ui.dialog

import android.app.Activity
import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import com.technology.landmarkremark.databinding.ProgressDialogBinding

class ProgressDialog (private val activity: Activity) {
    private val builder = AlertDialog.Builder(activity)
    private var alertDialog: AlertDialog? = null
    private var process = false

    fun createProgressDialog() {
        process = false
        val binding = ProgressDialogBinding.inflate(LayoutInflater.from(activity))
        builder.setView(binding.root)
        alertDialog = builder.create()
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(0))

        val rotate = RotateAnimation(
            0f,
            180f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotate.duration = 1000
        rotate.repeatCount = Animation.INFINITE
        rotate.repeatMode = Animation.RESTART
        rotate.interpolator = LinearInterpolator()
        binding.progressImage.startAnimation(rotate)
        showDialog()
    }

    private fun showDialog() {
        if (!process) {
            process = true
            alertDialog?.show()
        }
    }

    fun cancelDialog(delayClose: Boolean = false) {
        if (alertDialog?.isShowing == true)
            if (delayClose) {
                Handler(Looper.getMainLooper()).postDelayed({
                    alertDialog?.cancel()
                    process = false
                }, 600)
            } else {
                alertDialog?.cancel()
                process = false
            }
    }
}