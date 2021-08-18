package com.raccoon.todolist

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.task_item.view.*

class TaskAdapter (
    private val taskList: MutableList<Task>
    ) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    private var database: DatabaseReference? = null

    class TaskViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            // Parse layout to a Kotlin class
            LayoutInflater.from(parent.context).inflate(
                R.layout.task_item,
                parent,
                false
            )
        )
    }

    fun addTask(task: Task){
        taskList.add(task)
        notifyItemInserted(taskList.size - 1)
    }

    fun delCheckedTasks(){
        for (task in taskList){
            if(task.isChecked){
                database?.child(task.name)?.removeValue()
            }
        }
        taskList.removeAll { task ->
            task.isChecked
        }
        notifyDataSetChanged()
    }

    fun setDataBase(db: DatabaseReference){
        database = db
    }

    fun getTaskList(): MutableList<Task>{
        return taskList
    }

    private fun toggleStrikeThrough(tvTaskItem: TextView, isChecked: Boolean){
        if(isChecked){
            tvTaskItem.paintFlags = tvTaskItem.paintFlags or STRIKE_THRU_TEXT_FLAG
        }
        else{
            tvTaskItem.paintFlags = tvTaskItem.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val curTask = taskList[position]
        holder.itemView.apply {
            tvTaskItem.text = curTask.name
            cbTaskItem.isChecked = curTask.isChecked
            
            // If an item is (not) checked, the text will be (un)strikedthrough
            toggleStrikeThrough(tvTaskItem, curTask.isChecked)

            cbTaskItem.setOnClickListener {
                database?.child(curTask.name)?.child("checked")?.setValue(!curTask.isChecked)
                curTask.isChecked = !curTask.isChecked
                toggleStrikeThrough(tvTaskItem, curTask.isChecked)
            }
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}