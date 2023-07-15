package com.shadi777.todoapp.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.shadi777.todoapp.R
import com.shadi777.todoapp.data_sources.models.TodoItem
import com.shadi777.todoapp.data_sources.models.TodoItemViewModel
import com.shadi777.todoapp.data_sources.models.TodoListViewModel
import com.shadi777.todoapp.databinding.FragmentListToDoBinding
import com.shadi777.todoapp.recyclerview.TodoAdapter
import com.shadi777.todoapp.recyclerview.TodoAdapter.onItemInteractListener
import com.shadi777.todoapp.util.Constants
import kotlinx.coroutines.launch

/**
 * Fragment for list of all tasks. Used as a main fragment
 */
class FragmentListToDo : Fragment() {

    private val todoAdapter = TodoAdapter(
        itemInteractListener = object : onItemInteractListener {
            override fun onItemClickListener(item: TodoItem) {
                editItem(item)
            }

            override fun onCheckboxClickListener(item: TodoItem) {
                listViewModel.updateItem(item.copy(isDone = !item.isDone))
            }

        }
    )

    private var _binding: FragmentListToDoBinding? = null
    private val binding get() = _binding!!

    private val itemViewModel: TodoItemViewModel by lazy {
        (requireActivity() as MainActivity).itemViewModel
    }
    private val listViewModel: TodoListViewModel by lazy {
        (requireActivity() as MainActivity).listViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListToDoBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initDataObserver()
        initVisibilityChanger()
        initConnectionSnackbar()
        initDeleteSnackbar()

        binding.imageSettings.setOnClickListener {
            val settings = FragmentSettings()
            settings.show(requireFragmentManager(), settings.getTag());
        }

        binding.buttonAddTask.setOnClickListener {
            itemViewModel.selectItem(null)

            Navigation.findNavController(view).navigate(
                R.id.action_fragmentListToDo_to_fragmentCreateToDo
            )
        }

        binding.imageViewVisible.setOnClickListener {
            listViewModel.changeStateVisibility()
        }
    }

    private fun initRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = todoAdapter
        val layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, true)
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager


        binding.pullToRefresh.setOnRefreshListener {
            viewLifecycleOwner.lifecycleScope.launch {
                listViewModel.refreshData()
            }
            binding.pullToRefresh.isRefreshing = false
        }
    }

    private fun initDataObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            listViewModel.items.collect { items ->
                todoAdapter.submitList(items)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            listViewModel.doneItemsSize.collect {
                binding.textViewDone.text = String.format(
                    getResources().getString(R.string.done_sublabel),
                    it
                )
            }
        }
    }

    private fun initVisibilityChanger() {
        viewLifecycleOwner.lifecycleScope.launch {
            listViewModel.visibility.collect { isVisible ->
                binding.imageViewVisible.setImageResource(
                    if (isVisible) R.drawable.ic_visibility
                    else R.drawable.ic_visibility_off
                )
            }
        }
    }

    private fun initConnectionSnackbar() {
        viewLifecycleOwner.lifecycleScope.launch {
            listViewModel.getStatusCode().collect { statusCode ->
                if (statusCode == Constants.OK_STATUS_CODE) return@collect
                Snackbar.make(
                    binding.root.rootView,
                    when (statusCode) {
                        Constants.SYNC_ERROR_STATUS_CODE -> getString(R.string.sync_error)
                        Constants.AUTH_ERROR_STATUS_CODE -> getString(R.string.auth_error)
                        Constants.NO_ELEMENT_STATUS_CODE -> getString(R.string.no_element_error)
                        Constants.SERVER_ERROR_STATUS_CODE -> getString(R.string.server_error)
                        else -> getString(R.string.internet_not_found)
                    },
                    Snackbar.LENGTH_LONG
                ).setAction(getString(R.string.retry_snackbar)) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        listViewModel.refreshData()
                    }
                }.show()
            }
        }
    }

    private fun initDeleteSnackbar() {
        viewLifecycleOwner.lifecycleScope.launch {
            itemViewModel.getDeletedItem().collect { item ->
                if (item == null) return@collect
                val snackbar = Snackbar.make(
                    binding.root.rootView,
                    getString(R.string.snackbar_delete_text) + " " + item.text,
                    Snackbar.LENGTH_LONG
                ).setAction(getString(R.string.restore_button)) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        itemViewModel.addItem(item)
                    }
                }

                snackbar.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    override fun onShown(transientBottomBar: Snackbar?) {
                        itemViewModel.clearDeletedItem()
                        super.onShown(transientBottomBar)
                    }

                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        itemViewModel.clearDeletedItem()
                        super.onDismissed(transientBottomBar, event)
                    }
                })

                snackbar.show()
            }
        }
    }

    private fun editItem(item: TodoItem) {
        itemViewModel.selectItem(item.id)
        findNavController().navigate(R.id.action_fragmentListToDo_to_fragmentCreateToDo)
    }


    override fun onPause() {
        super.onPause()
        binding.pullToRefresh.isRefreshing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
