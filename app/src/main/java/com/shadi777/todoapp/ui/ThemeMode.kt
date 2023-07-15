package com.shadi777.todoapp.ui

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat.getString
import com.shadi777.todoapp.R

enum class ThemeMode {
    DARK, LIGHT, SYSTEM;

    companion object {
        private fun applyTheme(mode: ThemeMode) {
            when (mode) {
                LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }

        fun getCurrentMode(context: Context): ThemeMode {
            val sharedPref = context.getSharedPreferences(
                getString(context, R.string.settings_key),
                Context.MODE_PRIVATE
            )
            val defaultMode = SYSTEM
            val mode = sharedPref.getInt(
                getString(context, R.string.theme_mode_key),
                defaultMode.ordinal
            )
            return values()[mode]
        }

        fun setCurrentMode(context: Context, mode: ThemeMode) {
            val sharedPref = context.getSharedPreferences(
                getString(context, R.string.settings_key),
                Context.MODE_PRIVATE
            )
            with(sharedPref.edit()) {
                putInt(getString(context, R.string.theme_mode_key), mode.ordinal)
                apply()
            }
            applyTheme(mode)
        }

    }
}
