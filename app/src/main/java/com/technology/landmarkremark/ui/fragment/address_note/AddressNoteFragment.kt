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
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.technology.landmarkremark.R
import com.technology.landmarkremark.common.enums.ActionDetailEnum
import com.technology.landmarkremark.common.extensions.toast
import com.technology.landmarkremark.common.swipe.SwipeActionDeleteEditListener
import com.technology.landmarkremark.data.entity.AddressNoteEntity
import com.technology.landmarkremark.data.model.Resource
import com.technology.landmarkremark.data.model.UserAddressNote
import com.technology.landmarkremark.databinding.FragmentAddressNoteBinding
import com.technology.landmarkremark.interfaces.ActionDetailNoteDialogListener
import com.technology.landmarkremark.interfaces.ConfirmDialogListener
import com.technology.landmarkremark.interfaces.ItemClickListener
import com.technology.landmarkremark.ui.adapter.NoteAdapter
import com.technology.landmarkremark.ui.base.BaseFragment
import com.technology.landmarkremark.ui.dialog.ActionDetailNoteDialog
import com.technology.landmarkremark.ui.dialog.ConfirmDialog
import com.technology.landmarkremark.ui.dialog.ProgressDialog
import com.technology.landmarkremark.viewmodel.address_note.AddressNoteViewModel
import com.technology.landmarkremark.viewmodel.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressNoteFragment : BaseFragment<FragmentAddressNoteBinding, AddressNoteViewModel>(),
    ItemClickListener, SwipeActionDeleteEditListener, ActionDetailNoteDialogListener {
    private val TAG = AddressNoteFragment::class.java.simpleName

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var adapter: NoteAdapter
    private var loadingDialog: ProgressDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated()")
        binding.addressNoteViewModel = viewModel
        activity?.let {
            changeLayoutAppBar(it)
            loadingDialog = ProgressDialog(it)
        }
        initAdapter()
        eventHandle()
        observeAddressNoteData()
        observeEditNoteData()
        observeDeleteNoteData()
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
            viewModel.applySwipeGestureForList(it, binding.rcvAddressNote, this)
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
            } else {
                adapter.differ.submitList(listOf())
            }
        }
    }

    private fun observeEditNoteData() {
        viewModel.editNoteData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> loadingDialog?.createProgressDialog()

                is Resource.Error -> {
                    loadingDialog?.cancelDialog()
                    result.message?.let { errorMess -> context?.toast(errorMess) }
                }

                is Resource.Success -> {
                    loadingDialog?.cancelDialog()
                    context?.toast("Chỉnh sửa note thành công")
                }
            }
        }
    }

    private fun observeDeleteNoteData() {
        viewModel.deleteNoteData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> loadingDialog?.createProgressDialog()

                is Resource.Error -> {
                    loadingDialog?.cancelDialog()
                    result.message?.let { errorMess -> context?.toast(errorMess) }
                }

                is Resource.Success -> {
                    loadingDialog?.cancelDialog()
                    context?.toast("Xóa note thành công")
                }
            }
        }
    }

    override fun onClick(objects: Any?) {
        val item = objects as AddressNoteEntity
        activity?.let {
            ActionDetailNoteDialog(it).createDialog(
                this, UserAddressNote(
                    mainViewModel.userData!!.id,
                    mainViewModel.userData!!.name,
                    item.id,
                    item.content,
                    item.address,
                    item.lat,
                    item.lng
                ), ActionDetailEnum.DETAIL
            )
        }
    }

    override fun onDelete(position: Int) {
        val item = viewModel.addressNoteData.value?.get(position)
        if (item != null && activity != null)
            ConfirmDialog(requireActivity()).createDialog(
                requireActivity().getString(R.string.delete_note),
                requireActivity().getString(R.string.delete_confirm_message),
                object : ConfirmDialogListener {
                    override fun onAction(isAgree: Boolean) {
                        if (isAgree)
                            viewModel.deleteAddressNote(item)
                    }
                }
            )
    }

    override fun onEdit(position: Int) {
        val item = viewModel.addressNoteData.value?.get(position)
        if (item != null && activity != null) {
            ActionDetailNoteDialog(requireActivity()).createDialog(
                this, UserAddressNote(
                    mainViewModel.userData!!.id,
                    mainViewModel.userData!!.name,
                    item.id,
                    item.content,
                    item.address,
                    item.lat,
                    item.lng
                ), ActionDetailEnum.EDIT
            )
        }
    }

    override fun onCreateNote(userAddressNote: UserAddressNote) {
        //
    }

    override fun onEditNote(userAddressNote: UserAddressNote) {
        viewModel.editAddressNote(
            AddressNoteEntity(
                userAddressNote.addressNoteId,
                userAddressNote.address,
                userAddressNote.lat,
                userAddressNote.lng,
                userAddressNote.addressNoteContent,
                userAddressNote.userId
            )
        )
    }
}