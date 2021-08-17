package com.raccoon.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Pass an empty list to the adapter
        taskAdapter = TaskAdapter(mutableListOf())

        rvTaskList.adapter = taskAdapter
        rvTaskList.layoutManager = LinearLayoutManager(this)

        bAddTask.setOnClickListener {
            val taskName = etTaskName.text.toString()
            if(taskName.isNotEmpty()){
                val task = Task(taskName)
                taskAdapter.addTask(task)
                etTaskName.text.clear()
            }
        }

        bDelTask.setOnClickListener {
            taskAdapter.delCheckedTasks()
        }
    }
}