package com.backend.todo_tasker.button_functions

import android.app.Activity
import android.view.View
import android.widget.ImageButton
import com.backend.todo_tasker.MainActivity
import com.backend.todo_tasker.R
import petrov.kristiyan.colorpicker.ColorPicker

class ColorMenuButtonFunctions {

    fun saveProjectCreation()
    {

    }

    fun cancleProjectCreation()
    {

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