package com.shadi777.todoapp.screen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shadi777.todoapp.App
import com.shadi777.todoapp.screen.FragmentListToDoDirections
import com.shadi777.todoapp.R

import com.shadi777.todoapp.databinding.FragmentListToDoBinding
import com.shadi777.todoapp.recyclerview.TodoAdapter
import com.shadi777.todoapp.recyclerview.data.Action
import com.shadi777.todoapp.recyclerview.data.Priority
import com.shadi777.todoapp.recyclerview.data.SharedTodoItem
import com.shadi777.todoapp.recyclerview.data.TodoItem
import com.shadi777.todoapp.recyclerview.data.TodoItemsRepository
import kotlinx.coroutines.CoroutineDispatcher
// import com.shadi777.todoapp.recyclerview.data.TodoItemsRepository.Companion.idIterator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Date


class FragmentListToDo : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val todoAdapter = TodoAdapter()

    private val todoItemsRepository by lazy { App.get(requireContext()).todoItemsRepository }
    private var isDoneVisible = true

    private var _binding: FragmentListToDoBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SharedTodoItem

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

        viewModel = ViewModelProvider(requireActivity()).get(SharedTodoItem::class.java)
        viewModel.action.observe(viewLifecycleOwner, Observer<Action> { receivedAction ->
            when (receivedAction) {
                Action.SAVE_NEW -> {
                    //CoroutineScope(Dispatchers.IO).launch {
                    lifecycleScope.launch(Dispatchers.IO) {
                        todoItemsRepository.addTodoItem(viewModel.todoItem.value!!)
                        todoAdapter.submitList(todoItemsRepository.getTasks(isDoneVisible))
                    }
                }

                Action.DELETE -> {
                    //CoroutineScope(Dispatchers.IO).launch {
                    lifecycleScope.launch(Dispatchers.IO) {
                        todoItemsRepository.removeTodoItem(viewModel.todoItem.value!!)
                        todoAdapter.submitList(todoItemsRepository.getTasks(isDoneVisible))
                    }
                }

                Action.SAVE_CHANGE -> {
                    //CoroutineScope(Dispatchers.IO).launch {
                    lifecycleScope.launch(Dispatchers.IO) {
                        todoItemsRepository.updateTodoItem(viewModel.todoItem.value!!)
                        todoAdapter.submitList(todoItemsRepository.getTasks(isDoneVisible))
                    }
                }

                else -> {}
            }
        })

        binding.buttonAddTask.setOnClickListener {
            val todoItem =
                TodoItem(
                    todoItemsRepository.idIterator.toString(),
                    "",
                    Priority.Default,
                    false,
                    Date().time,
                    Date().time
                )
            val action =
                FragmentListToDoDirections.actionFragmentListToDoToFragmentCreateToDo(todoItem)
            Navigation.findNavController(view).navigate(action)
        }



        binding.textViewDone.text = String.format(
            getResources().getString(R.string.done_sublabel),
            todoItemsRepository.countDone()
        )

        setVisibleTasksAndIcon(isDoneVisible)

        binding.imageViewVisible.setOnClickListener {
            isDoneVisible = !isDoneVisible
            setVisibleTasksAndIcon(isDoneVisible)
        }
    }

    private fun initRecyclerView() {
        recyclerView = binding.recyclerView
        recyclerView.adapter = todoAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        todoAdapter.setOnChangeItemListener {
            binding.textViewDone.text = String.format(
                getResources().getString(R.string.done_sublabel),
                todoItemsRepository.countDone()
            )

            todoAdapter.submitList(todoItemsRepository.getTasks(isDoneVisible))
        }

        binding.pullToRefresh.setOnRefreshListener {
            lifecycleScope.launch(Dispatchers.IO) {
                todoItemsRepository.loadFromServer()
                binding.pullToRefresh.isRefreshing = false
            }
        }
    }

    private fun setVisibleTasksAndIcon(isDoneVisible: Boolean) {
        when (isDoneVisible) {
            true -> binding.imageViewVisible.setImageResource(R.drawable.ic_visibility)
            else -> binding.imageViewVisible.setImageResource(R.drawable.ic_visibility_off)
        }
        todoAdapter.submitList(todoItemsRepository.getTasks(isDoneVisible))
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