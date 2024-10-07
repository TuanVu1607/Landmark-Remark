package com.technology.landmarkremark.ui.fragment.address_note

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.technology.landmarkremark.R
import com.technology.landmarkremark.databinding.FragmentAddressNoteBinding
import com.technology.landmarkremark.interfaces.ItemClickListener
import com.technology.landmarkremark.ui.adapter.NoteAdapter
import com.technology.landmarkremark.ui.base.BaseFragment
import com.technology.landmarkremark.viewmodel.address_note.AddressNoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressNoteFragment : BaseFragment<FragmentAddressNoteBinding, AddressNoteViewModel>(),
    ItemClickListener {
    private val TAG = AddressNoteFragment::class.java.simpleName

    private lateinit var adapter: NoteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated()")
        binding.addressNoteViewModel = viewModel
        activity?.let { changeLayoutAppBar(it) }
        initAdapter()
        eventHandle()
        observeAddressNoteData()
    }

    private fun changeLayoutAppBar(fragmentActivity: FragmentActivity) {
        val menuHost: MenuHost = fragmentActivity
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.findItem(R.id.action_logout)?.isVisible = false
                menu.findItem(R.id.action_add_note)?.isVisible = false
                menu.findItem(R.id.action_note_history)?.isVisible = false
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun initAdapter() {
        context?.let {
            adapter = NoteAdapter(this)
            binding.rcvAddressNote.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            binding.rcvAddressNote.addItemDecoration(
                DividerItemDecoration(
                    binding.rcvAddressNote.context,
                    RecyclerView.VERTICAL
                )
            )
            binding.rcvAddressNote.adapter = adapter
        }
    }

    private fun eventHandle() {
        binding.svSearchAddressNote.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { viewModel.setTextSearch(it) }
                return false
            }
        })
    }

    private fun observeAddressNoteData() {
        viewModel.addressNoteData.observe(viewLifecycleOwner) { result ->
            if (result.isNotEmpty()) {
                result.let { dataList ->
                    adapter.differ.submitList(dataList)
                }
            }else{
                adapter.differ.submitList(listOf())
            }
        }
    }

    override fun onClick(objects: Any?) {
        //
    }
}