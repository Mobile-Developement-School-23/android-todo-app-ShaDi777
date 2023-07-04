package com.shadi777.todoapp.screen

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.shadi777.todoapp.screen.FragmentCreateToDoArgs
import com.shadi777.todoapp.R

import com.shadi777.todoapp.databinding.FragmentCreateToDoBinding
import com.shadi777.todoapp.recyclerview.data.Action
import com.shadi777.todoapp.recyclerview.data.Priority
import com.shadi777.todoapp.recyclerview.data.SharedTodoItem
import com.shadi777.todoapp.recyclerview.data.TodoItem
import java.time.ZoneId
import java.util.Calendar
import java.util.Date


class FragmentCreateToDo : Fragment() {

    private var _binding: FragmentCreateToDoBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SharedTodoItem

    private val args: FragmentCreateToDoArgs by navArgs<FragmentCreateToDoArgs>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateToDoBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val item: TodoItem = args.todoItem
        viewModel = ViewModelProvider(requireActivity()).get(SharedTodoItem::class.java)
        viewModel.updateState(item, Action.NO_ACTION)

        // Set spinner to choose priority of task
        ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.priorities,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerPriority.adapter = adapter
        }
        binding.spinnerPriority.setSelection(item.priority.ordinal)


        // Set switch to open calendar date picker
        // var deadlineDate: Date? = item.deadline_date
        var deadlineDate: Long? = item.deadline_date
        if (deadlineDate != null) {
            binding.switchDate.isChecked = true
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = deadlineDate
            val zonedDateTime = calendar.time.toInstant().atZone(ZoneId.systemDefault())
            val dateString = "${zonedDateTime.dayOfMonth} ${
                this.requireContext().resources.getStringArray(R.array.MONTHS)[zonedDateTime.monthValue - 1]
            } ${zonedDateTime.year}"

            binding.textViewDateUntil.text = dateString
        }
        binding.switchDate.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val dpd = DatePickerDialog(
                    this.requireContext(),
                    DatePickerDialog.OnDateSetListener { v, y, m, d ->
                        val calendar = Calendar.getInstance()
                        calendar.set(y, m, d)
                        deadlineDate = calendar.time.time
                        val m_string =
                            this.requireContext().resources.getStringArray(R.array.MONTHS)[m]
                        binding.textViewDateUntil.setText("$d $m_string $y")
                    }, year, month, day
                )
                dpd.datePicker.minDate = System.currentTimeMillis()
                dpd.setOnCancelListener {
                    binding.switchDate.isChecked = false
                }
                dpd.show()
                dpd.getButton(DatePickerDialog.BUTTON_POSITIVE)
                    .setTextColor(getResources().getColor(R.color.color_blue))
                dpd.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                    .setTextColor(getResources().getColor(R.color.color_blue))

            } else {
                deadlineDate = null
                binding.textViewDateUntil.setText("")
            }
        }
        binding.imageViewCancel.setOnClickListener {
            Navigation.findNavController(view).navigateUp()
        }

        // Enable button DELETE if editing the task
        if (item.text.isNotEmpty()) {
            binding.buttonDelete.isClickable = true
            binding.buttonDelete.setTextColor(getResources().getColor(R.color.color_red))
            binding.buttonDelete.compoundDrawables[0].setColorFilter(
                ContextCompat.getColor(this.requireContext(), R.color.color_red),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            binding.buttonDelete.setOnClickListener {
                viewModel.updateState(item, Action.DELETE)
                Navigation.findNavController(it).navigateUp()
            }
        }

        // Enable button SAVE
        binding.buttonSave.setOnClickListener {
            if (binding.editText.text.isEmpty()) {
                Navigation.findNavController(it).navigateUp()
                return@setOnClickListener
            }

            val action: Action
            if (item.text.isNotEmpty()) {
                item.change_date = Date().time
                action = Action.SAVE_CHANGE
            } else {
                action = Action.SAVE_NEW
            }
            item.text = binding.editText.text.toString()
            item.priority = Priority.values()[binding.spinnerPriority.selectedItemPosition]
            item.deadline_date = deadlineDate

            viewModel.updateState(item, action)
            Navigation.findNavController(it).navigateUp()
        }

        binding.editText.setText(item.text)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}