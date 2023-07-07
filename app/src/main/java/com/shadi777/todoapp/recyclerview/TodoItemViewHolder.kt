package com.shadi777.todoapp.recyclerview

import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.shadi777.todoapp.R
import com.shadi777.todoapp.data_sources.models.Priority
import com.shadi777.todoapp.data_sources.models.TodoItem
import com.shadi777.todoapp.databinding.ItemTodoBinding
import java.time.ZoneId
import java.util.Calendar

/**
 * Class for view holder in recycler view that is used in FragmentListToDo
 */
class TodoItemViewHolder(
    val binding: ItemTodoBinding,
    val itemInteractListener: TodoAdapter.onItemInteractListener
) : RecyclerView.ViewHolder(binding.root) {

    private fun setIcon(imageView: ImageView, icon: Int, iconColor: Int) {
        imageView.setImageResource(icon)
        imageView.setColorFilter(
            ContextCompat.getColor(this.itemView.context, iconColor),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
    }

    private fun setState(item: TodoItem) {
        if (item.isDone) {
            setIcon(binding.checkBox, R.drawable.ic_checkbox_done, R.color.color_green)

            binding.textViewText.setTextColor(
                ContextCompat.getColor(
                    this.itemView.context,
                    R.color.label_tertiary
                )
            )
            binding.textViewText.setPaintFlags(binding.textViewText.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)

        } else {
            if (item.priority == Priority.High) setIcon(
                binding.checkBox,
                R.drawable.ic_checkbox,
                R.color.color_red
            )
            else setIcon(binding.checkBox, R.drawable.ic_checkbox, R.color.color_gray_light)

            binding.textViewText.setTextColor(
                ContextCompat.getColor(
                    this.itemView.context,
                    R.color.label_primary
                )
            )
            binding.textViewText.setPaintFlags(
                binding.textViewText.getPaintFlags() and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            )
        }
    }

    fun onBind(item: TodoItem) {
        binding.textViewText.text = item.text

        if (item.deadlineDate != null) {
            binding.textViewDate.visibility = View.VISIBLE

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = item.deadlineDate!!
            val zonedDateTime = calendar.time.toInstant().atZone(ZoneId.systemDefault())
            val dateString = "${zonedDateTime.dayOfMonth} ${
                itemView.getResources().getStringArray(R.array.MONTHS)[zonedDateTime.monthValue - 1]
            } ${zonedDateTime.year}"

            binding.textViewDate.text = dateString
        } else {
            binding.textViewDate.visibility = View.GONE
        }

        when (item.priority) {
            Priority.High -> {
                setIcon(binding.imageViewPriority, R.drawable.ic_priority_high, R.color.color_red)
            }

            Priority.Low -> {
                setIcon(binding.imageViewPriority, R.drawable.ic_priority_low, R.color.color_gray)
            }

            else -> {
                setIcon(binding.imageViewPriority, 0, R.color.color_gray)
            }
        }

        setState(item)

        binding.root.setOnClickListener {
            itemInteractListener.onItemClickListener(item)
        }

        binding.checkBox.setOnClickListener {
            itemInteractListener.onCheckboxClickListener(item)
            setState(item)
        }
    }
}
