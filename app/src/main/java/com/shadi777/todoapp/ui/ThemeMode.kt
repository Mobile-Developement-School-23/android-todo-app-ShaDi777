package com.shadi777.todoapp.ui

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.shadi777.todoapp.util.Constants

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
                Constants.SETTINGS_KEY,
                Context.MODE_PRIVATE
            )
            val defaultMode = SYSTEM
            val mode = sharedPref.getInt(
                Constants.THEME_MODE_KEY,
                defaultMode.ordinal
            )
            return values()[mode]
        }

        fun setCurrentMode(context: Context, mode: ThemeMode) {
            val sharedPref = context.getSharedPreferences(
                Constants.SETTINGS_KEY,
                Context.MODE_PRIVATE
            )
            with(sharedPref.edit()) {
                putInt(Constants.THEME_MODE_KEY, mode.ordinal)
                apply()
            }
            applyTheme(mode)
        }

    }
}
