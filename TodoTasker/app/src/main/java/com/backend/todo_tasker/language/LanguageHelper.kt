package com.backend.todo_tasker.language

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.core.content.ContextCompat.startActivity
import com.backend.todo_tasker.MainActivity
import java.util.*

class LanguageHelper {
    private var currentLanguage = "en"
    // Changes language to Russian or English depending on the current state
    fun toggleLanguage(resources: Resources, context: Context) {
        val locale: Locale
        val oldLanguage: String
        if(currentLanguage == "en") {
            oldLanguage = "en"
            currentLanguage = "ru"
            locale = Locale("ru")

        } else {
            oldLanguage = "ru"
            currentLanguage = "en"
            locale = Locale("en")
        }
        changeLanguage(resources, context, locale, oldLanguage)
    }

    private fun changeLanguage(resources: Resources, context: Context, locale: Locale, oldLanguage: String) {
        val dm = resources.displayMetrics
        val conf = resources.configuration
        conf.locale = locale
        resources.updateConfiguration(conf, dm)
        val refresh = Intent(
                context,
                MainActivity::class.java
        )
        refresh.putExtra(oldLanguage, currentLanguage)
        startActivity(context, refresh, null)
    }
}