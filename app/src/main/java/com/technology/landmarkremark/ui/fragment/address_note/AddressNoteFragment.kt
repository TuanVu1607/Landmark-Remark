package com.technology.landmarkremark.ui.fragment.address_note

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import com.technology.landmarkremark.R
import com.technology.landmarkremark.databinding.FragmentAddressNoteBinding
import com.technology.landmarkremark.ui.base.BaseFragment
import com.technology.landmarkremark.viewmodel.address_note.AddressNoteViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddressNoteFragment : BaseFragment<FragmentAddressNoteBinding, AddressNoteViewModel>() {
    private val TAG = AddressNoteFragment::class.java.simpleName

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated()")
        binding.addressNoteViewModel = viewModel
        activity?.let { changeLayoutAppBar(it) }
    }

    private fun changeLayoutAppBar(fragmentActivity: FragmentActivity) {
        val menuHost: MenuHost = fragmentActivity
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.findItem(R.id.action_add_note)?.isVisible = false
                menu.findItem(R.id.action_note_history)?.isVisible = false
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}