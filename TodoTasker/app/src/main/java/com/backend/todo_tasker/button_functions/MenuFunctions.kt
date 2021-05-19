package com.backend.todo_tasker.button_functions

import androidx.appcompat.app.AppCompatDelegate

class MenuFunctions {
    fun darkModeFunction() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    fun lightModeFunction() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}