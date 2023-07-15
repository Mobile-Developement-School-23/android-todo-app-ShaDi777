package com.shadi777.todoapp.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shadi777.todoapp.databinding.FragmentSettingsBinding
import com.shadi777.todoapp.ui.ThemeMode

class FragmentSettings : BottomSheetDialogFragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initModeSelection()
    }


    private fun initModeSelection() {
        val currentMode = ThemeMode.getCurrentMode(requireContext())

        binding.modeSelection.check(
            when(currentMode) {
                ThemeMode.LIGHT -> binding.lightMode.id
                ThemeMode.DARK -> binding.darkMode.id
                ThemeMode.SYSTEM -> binding.systemMode.id
            }
        )

        binding.modeSelection.addOnButtonCheckedListener { group, id, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener
            when(id) {
                binding.lightMode.id -> ThemeMode.setCurrentMode(requireContext(), ThemeMode.LIGHT)
                binding.darkMode.id -> ThemeMode.setCurrentMode(requireContext(), ThemeMode.DARK)
                binding.systemMode.id -> ThemeMode.setCurrentMode(requireContext(), ThemeMode.SYSTEM)
            }
        }
    }
}