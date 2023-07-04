package com.shadi777.todoapp.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

import com.shadi777.todoapp.databinding.ItemTodoBinding
import com.shadi777.todoapp.recyclerview.data.TodoItem
//import com.shadi777.todoapp.recyclerview.data.TodoItemsRepository.Companion.itemlist


class TodoAdapter : ListAdapter<TodoItem, TodoItemViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<TodoItem>() {
            override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var onItemChangeListener: (() -> Unit)? = null

    fun setOnChangeItemListener(listener: () -> Unit) {
        onItemChangeListener = listener
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoItemViewHolder(binding, onItemChangeListener)
    }

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}
