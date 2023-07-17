package com.shadi777.todoapp.ui.screen.CreateTaskScreen

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.shadi777.todoapp.data_sources.models.Priority
import com.shadi777.todoapp.data_sources.models.TodoItem
import com.shadi777.todoapp.data_sources.models.TodoItemViewModel
import com.shadi777.todoapp.data_sources.models.TodoListViewModel
import com.shadi777.todoapp.databinding.FragmentCreateToDoBinding
import com.shadi777.todoapp.notifications.AlarmReceiver
import com.shadi777.todoapp.ui.core.AppTheme
import com.shadi777.todoapp.ui.screen.CreateTaskScreen.components.CreateTodoScreen
import com.shadi777.todoapp.ui.screen.MainActivity
import com.shadi777.todoapp.util.Constants
import kotlinx.coroutines.launch


/**
 * Fragment for creating and editing tasks
 */
class FragmentCreateToDo : Fragment() {

    private var _binding: FragmentCreateToDoBinding? = null
    private val binding get() = _binding!!

    private val itemViewModel: TodoItemViewModel by lazy {
        (requireActivity() as MainActivity).itemViewModel
    }

    private lateinit var lastKnownItem: TodoItem

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateToDoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lastKnownItem = itemViewModel.getSelectedItem().value

        binding.composeView.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
        binding.composeView.setContent {
            AppTheme {
                val item by itemViewModel.getSelectedItem().collectAsState()
                CreateTodoScreen(
                    itemViewModel.isUpdating,
                    item,
                    itemViewModel::onAction,
                    { Navigation.findNavController(view).navigateUp() }
                )
            }
        }
    }

    /*
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val item =
                if (itemViewModel.getSelectedItem().value == null)
                    TodoItem(
                        id = UUID.randomUUID().toString(),
                        text = "",
                        priority = Priority.Default,
                        isDone = false,
                        color = null,
                        createDate = Date().time,
                        changeDate = Date().time
                    )
                else itemViewModel.getSelectedItem().value!!

            initSpinnerPriority(item)
            initDeadlinePicker(item)
            initDeleteButton(item)
            initSaveButton(item)

            binding.imageViewCancel.setOnClickListener {
                Navigation.findNavController(view).navigateUp()
            }

            binding.editText.setText(item.text)
        }

        private fun initSpinnerPriority(item: TodoItem) {
            ArrayAdapter.createFromResource(
                this.requireContext(),
                R.array.priorities,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerPriority.adapter = adapter
            }
            binding.spinnerPriority.setSelection(item.priority.ordinal)
        }

        private fun initDeadlinePicker(item: TodoItem) {
            if (item.deadlineDate != null) {
                binding.switchDate.isChecked = true
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = item.deadlineDate!!
                val zonedDateTime = calendar.time.toInstant().atZone(ZoneId.systemDefault())
                val dateString = "${zonedDateTime.dayOfMonth} ${
                    this.requireContext().resources.getStringArray(R.array.MONTHS)[zonedDateTime.monthValue - 1]
                } ${zonedDateTime.year}"

                binding.textViewDateUntil.text = dateString
            }
            binding.switchDate.setOnCheckedChangeListener { compoundButton, isChecked ->
                if (isChecked) {
                    initDatePickerDialog(item)
                } else {
                    item.deadlineDate = null
                    binding.textViewDateUntil.setText("")
                }
            }
        }

        private fun initDatePickerDialog(item: TodoItem) {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                this.requireContext(),
                DatePickerDialog.OnDateSetListener { v, y, m, d ->
                    val calendar = Calendar.getInstance()
                    calendar.set(y, m, d)
                    item.deadlineDate = calendar.time.time
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
        }

        private fun initDeleteButton(item: TodoItem) {
            if (item.text.isNotEmpty()) {
                binding.buttonDelete.isClickable = true
                binding.buttonDelete.setTextColor(getResources().getColor(R.color.color_red))
                binding.buttonDelete.compoundDrawables[0].setColorFilter(
                    ContextCompat.getColor(this.requireContext(), R.color.color_red),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                binding.buttonDelete.setOnClickListener {
                    itemViewModel.deleteItem(item.id)
                    Navigation.findNavController(it).navigateUp()
                }
            }
        }

        private fun initSaveButton(item: TodoItem) {
            binding.buttonSave.setOnClickListener {
                if (binding.editText.text.isEmpty()) {
                    Navigation.findNavController(it).navigateUp()
                    return@setOnClickListener
                }

                item.text = binding.editText.text.toString()
                item.priority = Priority.values()[binding.spinnerPriority.selectedItemPosition]

                if (itemViewModel.getSelectedItem().value == null) {
                    itemViewModel.addItem(item)
                } else {
                    itemViewModel.updateItem(item)
                }

                Navigation.findNavController(it).navigateUp()
            }
        }
    */
    override fun onDestroyView() {
        addNotification()
        super.onDestroyView()
        _binding = null
    }

    private fun addNotification() {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        lifecycleScope.launch {
            val todoItem = itemViewModel.getSelectedItem().value
            val alarmIntent = createPendingIntent(todoItem)

            val isDeleted = (itemViewModel.getDeletedItem().value != null)
            val isChanged = (todoItem != lastKnownItem) && itemViewModel.isSaving
            
            if (isChanged || isDeleted) {
                alarmManager.cancel(alarmIntent)
            }
            if (isChanged) {
                setNewNotification(alarmManager, alarmIntent, todoItem.deadlineDate)
            }
            itemViewModel.isSaving = false
            itemViewModel.isUpdating = false
        }
    }

    private fun createPendingIntent(todoItem: TodoItem): PendingIntent {
        return Intent(context, AlarmReceiver::class.java).let { intent ->
            intent
                .putExtra(Constants.INTENT_ID_KEY, todoItem.id)
                .putExtra(Constants.INTENT_ID_TITLE_KEY, todoItem.text)
                .putExtra(
                    Constants.INTENT_ID_IMPORTANCE_KEY,
                    todoItem.priority.toString()
                )
                .addFlags(
                    Intent.FLAG_RECEIVER_FOREGROUND or
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_NEW_TASK
                )

            PendingIntent.getBroadcast(
                context,
                todoItem.id.hashCode(),
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }
    }

    private fun setNewNotification(
        alarmManager: AlarmManager,
        alarmIntent: PendingIntent,
        time: Long?
    ) {
        if (time != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    time,
                    alarmIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    time,
                    alarmIntent
                )
            }
        }
    }
}
