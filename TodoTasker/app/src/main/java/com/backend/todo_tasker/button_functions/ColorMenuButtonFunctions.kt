package com.backend.todo_tasker.button_functions

import android.app.Activity
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import com.backend.todo_tasker.MainActivity
import com.backend.todo_tasker.R
import com.backend.todo_tasker.popup_window.PopUpWindowInflater
import petrov.kristiyan.colorpicker.ColorPicker

class ColorMenuButtonFunctions {

    fun saveProjectCreation(view: View)
    {
        var name = view.findViewById<EditText>(R.id.edittext_name)
        var color = view.findViewById<ImageButton>(R.id.image_btn_color).colorFilter



        PopUpWindowInflater().dismissProjectWindow()
    }

    fun cancelProjectCreation()
    {
        PopUpWindowInflater().dismissProjectWindow()
    }

    fun pickColorFunction(act: Activity, view: View)
    {
        val color_button = view.findViewById<ImageButton>(R.id.image_btn_color)

        color_button.setOnClickListener {
            val colorPicker = ColorPicker(act)
            colorPicker.setRoundColorButton(true)
            colorPicker.show()
            colorPicker.setOnChooseColorListener(object : ColorPicker.OnChooseColorListener {
                override fun onChooseColor(position: Int, color: Int)
                {
                    color_button.setColorFilter(color)
                }

                override fun onCancel() {

                }
            })
        }
    }


}