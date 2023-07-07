package com.shadi777.todoapp.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.shadi777.todoapp.data_sources.models.TodoItem
import com.shadi777.todoapp.databinding.ItemTodoBinding

/**
 * Class for recycler view adapter that is used in FragmentListToDo
 */
class TodoAdapter(
    private val itemInteractListener: onItemInteractListener
) : ListAdapter<TodoItem, TodoItemViewHolder>(DiffCallback) {

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

    interface onItemInteractListener {
        fun onItemClickListener(item: TodoItem)
        fun onCheckboxClickListener(item: TodoItem)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoItemViewHolder(binding, itemInteractListener)
    }

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}
