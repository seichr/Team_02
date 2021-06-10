package com.backend.todo_tasker.tasklist_view

import android.text.format.DateFormat
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.backend.todo_tasker.*
import com.backend.todo_tasker.database.Category
import com.backend.todo_tasker.database.Todo
import com.backend.todo_tasker.popup_window.PopUpWindowInflater
import com.backend.todo_tasker.popup_window.WINDOWTYPE
import kotlinx.android.synthetic.main.recyclerview_item_category.view.*
import java.util.*


class RecyclerAdapterCategory: RecyclerView.Adapter<RecyclerAdapterCategory.CategoryHolder>()  {

    companion object {
        private var instance: RecyclerAdapterCategory? = null
        private var categories: List<Category> = emptyList()
    }

    fun setDataCategory(data: List<Category>){
        categories = data
    }

    fun getInstance(): RecyclerAdapterCategory {
        if(instance == null) {
            instance = RecyclerAdapterCategory()
        }
        return instance as RecyclerAdapterCategory
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val inflatedView = parent.inflate(R.layout.recyclerview_item_category, false)
        return CategoryHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        if (position %2 == 1) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.tasks1))
        }
        else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.tasks2))
        }
        val itemCategory = categories[position]
        holder.bindCategory(itemCategory)
    }

    class CategoryHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        private var category: Category? = null

        fun bindCategory(cat: Category) {

            this.category = cat

            view.item_name.text = cat.name
            view.category_color.setColorFilter(cat.color!!)
            view.item_uid.text = cat.uid.toString()
            view.deleteButton.contentDescription = cat.uid.toString()
            //view.item_name.color = cat.color
        }
    }
}
