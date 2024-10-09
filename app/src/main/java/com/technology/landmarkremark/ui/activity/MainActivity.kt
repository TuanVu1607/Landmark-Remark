package com.technology.landmarkremark.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.technology.landmarkremark.R
import com.technology.landmarkremark.common.extensions.toast
import com.technology.landmarkremark.data.model.Resource
import com.technology.landmarkremark.data.model.UserAddressNote
import com.technology.landmarkremark.databinding.ActivityMainBinding
import com.technology.landmarkremark.interfaces.CreateNoteDialogListener
import com.technology.landmarkremark.ui.base.BaseActivity
import com.technology.landmarkremark.ui.dialog.ActionDetailNoteDialog
import com.technology.landmarkremark.ui.dialog.ProgressDialog
import com.technology.landmarkremark.viewmodel.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate),
    CreateNoteDialogListener {
    private val TAG = MainActivity::class.java.simpleName
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val navController by lazy { findNavController(R.id.nav_host_fragment_content_main) }
    private val mainViewModel by viewModels<MainViewModel>()
    private var actionDetailNoteDialog: ActionDetailNoteDialog? = null
    private var loadingDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate()")
        setupAppBarAndNavigation()
        actionDetailNoteDialog = ActionDetailNoteDialog(this)
        loadingDialog = ProgressDialog(this)
        mainViewModel.getUser()
        observeCreateNoteData()
    }

    private fun observeCreateNoteData() {
        mainViewModel.createNoteData.observe(this) { result ->
            when (result) {
                is Resource.Loading -> loadingDialog?.createProgressDialog()

                is Resource.Error -> {
                    loadingDialog?.cancelDialog()
                    result.message?.let { errorMess -> toast(errorMess) }
                }

                is Resource.Success -> {
                    loadingDialog?.cancelDialog()
                    toast("Tạo note thành công")
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_note -> {
                Log.d(
                    "action_add_note",
                    "${mainViewModel.currentLocation} - ${mainViewModel.userData}"
                )
                if (mainViewModel.currentLocation != null && mainViewModel.userData != null)
                    actionDetailNoteDialog?.createDialog(
                        this, UserAddressNote(
                            mainViewModel.userData!!.id,
                            mainViewModel.userData!!.name,
                            0,
                            "",
                            mainViewModel.currentLocation!!.address,
                            mainViewModel.currentLocation!!.lat,
                            mainViewModel.currentLocation!!.lng
                        )
                    )
            }

            R.id.action_note_history -> navController.navigate(R.id.addressNoteFragment)
            R.id.action_logout -> mainViewModel.logout(this)
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupAppBarAndNavigation() {
        setSupportActionBar(binding.toolbar)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onCreateNote(userAddressNote: UserAddressNote) {
        mainViewModel.createNote(userAddressNote)
    }
}