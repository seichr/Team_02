package com.backend.todo_tasker.popup_window

import android.content.res.Configuration
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.backend.todo_tasker.R
import com.backend.todo_tasker.button_functions.DateTimePickerFunctions
import com.backend.todo_tasker.button_functions.ModifyFunctions
import com.backend.todo_tasker.button_functions.MoreOptionsFunctions
import kotlinx.android.synthetic.main.recyclerview_item.view.*

enum class WINDOWTYPE {
    ADD, MODIFY, MOREOPTIONS, MENU
}

class PopUpWindowInflater {

    companion object {
        private var instance: PopUpWindowInflater? = null
    }

    private var backgroundDimmerWindow: PopupWindow? = null
    private var addTaskView: View? = null
    private var addTaskWindow: PopupWindow? = null
    private var modifyTaskView: View? = null
    private var modifyTaskWindow: PopupWindow? = null
    private var moreOptionsTaskView: View? = null
    private var moreOptionsTaskWindow: PopupWindow? = null
    private var menuTaskView: View? = null
    private var menuTaskWindow: PopupWindow? = null

    private var currentUID: Int? = null

    fun getInstance(): PopUpWindowInflater {
        if (instance == null) {
            instance = PopUpWindowInflater()
        }
        return instance as PopUpWindowInflater
    }

    fun getAddTaskView(): View? {
        return addTaskView
    }

    fun getModifyTaskView(): View? {
        return modifyTaskView
    }

    fun dismissAddTaskWindow() {
        addTaskWindow!!.dismiss()
    }

    fun dismissModifyTaskWindow() {
        modifyTaskWindow!!.dismiss()
    }

    fun dismissMenuWindow() {
        menuTaskWindow!!.dismiss()
    }

    fun inflateWindow(
        view: View,
        type: WINDOWTYPE,
        it: View? = null,
        adapterPosition: Int? = null,
        width: Int = LinearLayout.LayoutParams.WRAP_CONTENT,
        height: Int = LinearLayout.LayoutParams.WRAP_CONTENT,
        backgroundDimmed: Boolean = true
    ) {

        val inflater =
            view.context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        if (backgroundDimmed) {
            val backgroundView: View = inflater.inflate(R.layout.dimming_background, null)
            val widthBackgroundWindow = LinearLayout.LayoutParams.MATCH_PARENT
            val heightBackgroundWindow = LinearLayout.LayoutParams.MATCH_PARENT
            backgroundDimmerWindow =
                PopupWindow(backgroundView, widthBackgroundWindow, heightBackgroundWindow, false)
            backgroundDimmerWindow!!.showAtLocation(view, Gravity.CENTER, 0, 0)
        }

        when (type) {
            WINDOWTYPE.ADD -> {
                inflateAddWindow(view, width, height)
            }
            WINDOWTYPE.MODIFY -> {
                inflateModifyWindow(view, width, height, adapterPosition = adapterPosition)
            }
            WINDOWTYPE.MOREOPTIONS -> {
                if (it != null) {
                    inflateMoreOptionsWindow(view, width, height, it, adapterPosition)
                }
            }
            WINDOWTYPE.MENU -> {
                inflateMenuWindow(view, width, height)
            }
        }
    }

    private fun inflateAddWindow(view: View, width: Int, height: Int) {
        val inflater =
            view.context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        addTaskView = inflater.inflate(R.layout.create_task_window, null)
        addTaskWindow = PopupWindow(addTaskView, width, height, true)

        addTaskWindow!!.showAtLocation(view, Gravity.CENTER, 0, 0)
        addTaskWindow!!.setOnDismissListener {
            backgroundDimmerWindow!!.dismiss()
        }

        val editText = addTaskView?.findViewById<EditText>(R.id.edittext_name)
        editText?.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable) {
                    val saveButton = addTaskView?.findViewById<Button>(R.id.button_add_to_db)
                    saveButton?.isEnabled = editText.text.toString() != ""
                }
            })
        if (editText != null && editText.text.toString() == "") {
            val saveButton = addTaskView?.findViewById<Button>(R.id.button_add_to_db)
            saveButton?.isEnabled = false
        }
    }

    private fun inflateModifyWindow(view: View, width: Int, height: Int, adapterPosition: Int?) {
        val inflater =
            view.context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        modifyTaskView = inflater.inflate(R.layout.modify_task_window, null)

        modifyTaskWindow = PopupWindow(modifyTaskView, width, height, true)
        modifyTaskWindow!!.showAtLocation(view, Gravity.CENTER, 0, 0)
        modifyTaskWindow!!.setOnDismissListener {
            backgroundDimmerWindow!!.dismiss()
        }

        val editTextName = modifyTaskView?.findViewById<EditText>(R.id.edittext_modify_name)
        editTextName?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                val saveButton = modifyTaskView?.findViewById<Button>(R.id.button_modify_to_db)
                saveButton?.isEnabled = editTextName.text.toString() != ""
            }
        })
        if (editTextName != null && editTextName.text.toString() == "") {
            val saveButton = modifyTaskView?.findViewById<Button>(R.id.button_modify_to_db)
            saveButton?.isEnabled = false
        }

        // Fill EditTexts with the proper entries
        val taskName = Editable.Factory.getInstance().newEditable(view.item_title.text)
        val taskDate = Editable.Factory.getInstance().newEditable(view.item_date.text)

        editTextName?.text = taskName

        // Configure Buttons and EditText
        val cancelButton = modifyTaskView?.findViewById<Button>(R.id.button_modify_cancel)
        cancelButton?.setOnClickListener {
            ModifyFunctions().cancelModifyActivity()
        }
        val saveButton = modifyTaskView?.findViewById<Button>(R.id.button_modify_to_db)
        saveButton?.setOnClickListener {
            if (adapterPosition != null) {
                ModifyFunctions().updateModifyActivity(editTextName, view.item_uid, adapterPosition)
            }
        }

        val completeButton = modifyTaskView?.findViewById<Button>(R.id.button_complete_task)
        completeButton?.setOnClickListener {
            if (adapterPosition != null) {
                ModifyFunctions().deleteCompletedTask(view.item_uid, adapterPosition)
            }
        }


        val editTextDateTime = modifyTaskView?.findViewById<EditText>(R.id.edittext_modify_datetime)
        editTextDateTime?.setOnClickListener {
            DateTimePickerFunctions().clickOnDateTimeField(it, WINDOWTYPE.MODIFY)
        }
        editTextDateTime?.text = taskDate
        val moreOptionsButton = modifyTaskView?.findViewById<ImageButton>(R.id.button_more_options)
        moreOptionsButton?.setOnClickListener {
            if (adapterPosition != null) {
                ModifyFunctions().openMoreOptionsWindows(it, view, adapterPosition)
            }
        }
    }

    private fun inflateMoreOptionsWindow(
        view: View,
        width: Int,
        height: Int,
        it: View,
        adapterPosition: Int?
    ) {
        val inflater =
            view.context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        moreOptionsTaskView = inflater.inflate(R.layout.more_options_window, null)
        currentUID = view.item_uid.text.toString().toInt()

        moreOptionsTaskWindow = PopupWindow(moreOptionsTaskView, width, height, true)

        val windowLoc = IntArray(2)
        it.getLocationOnScreen(windowLoc)

        val button = it.findViewById<ImageButton>(R.id.button_more_options)
        val margin = 30 // Gap between Button and Window

        val moreOptionsWindowYPos = -windowLoc[1] / 2 + button.height + margin
        val moreOptionsWindowXPos = 120

        moreOptionsTaskWindow!!.showAtLocation(
            view,
            Gravity.CENTER,
            moreOptionsWindowXPos,
            moreOptionsWindowYPos
        )

        val duplicateButton = moreOptionsTaskView?.findViewById<Button>(R.id.button_duplicate_task)
        duplicateButton?.setOnClickListener {
            MoreOptionsFunctions().duplicateFunction(currentUID, adapterPosition)
            moreOptionsTaskWindow!!.dismiss()
        }
        val deleteButton = moreOptionsTaskView?.findViewById<Button>(R.id.button_delete_task)
        deleteButton?.setOnClickListener {
            MoreOptionsFunctions().deleteFunction(currentUID, adapterPosition)
            moreOptionsTaskWindow!!.dismiss()
        }

        moreOptionsTaskWindow!!.setOnDismissListener {
            modifyTaskWindow!!.dismiss()
        }
    }

    private fun inflateMenuWindow(view: View, width: Int, height: Int) {
        val inflater =
            view.context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        menuTaskView = inflater.inflate(R.layout.burgermenu_window, null)

        menuTaskWindow = PopupWindow(menuTaskView, width, height, true)
        menuTaskView?.animation = AnimationUtils.loadAnimation(view.context, R.anim.slide_in)

        menuTaskWindow!!.setOnDismissListener {
            backgroundDimmerWindow!!.dismiss()
        }

        menuTaskWindow!!.showAtLocation(view, Gravity.START, 0, 0)

        val currentNightMode =
            view.context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)

        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_NO -> {
                val lightmode_btn = view.findViewById<Button>(R.id.lightmode_btn)
                lightmode_btn?.isEnabled = false
                val darkmode_btn = view.findViewById<Button>(R.id.darkmode_btn)
                darkmode_btn?.isEnabled = true
            } // Light mode is not active, we're using the light theme

            Configuration.UI_MODE_NIGHT_YES -> {
                val lightmode_btn = view.findViewById<Button>(R.id.lightmode_btn)
                lightmode_btn?.isEnabled = true
                val darkmode_btn = view.findViewById<Button>(R.id.darkmode_btn)
                darkmode_btn?.isEnabled = false
            } // Night mode is active, we're using dark theme
        }
    }
}