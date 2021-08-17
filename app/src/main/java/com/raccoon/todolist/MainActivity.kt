package com.raccoon.todolist

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var taskAdapter: TaskAdapter
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = FirebaseDatabase.getInstance().getReference("Tasks")

        // Pass an empty list to the adapter
        taskAdapter = TaskAdapter(mutableListOf())
        taskAdapter.setDataBase(database)

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children
                children.forEach {
                    val map = it.getValue() as HashMap<String, Any>
                    val nome: String = map["name"] as String
                    val checked: Boolean = map["checked"] as Boolean
                    val task: Task = Task(nome, checked)

                    taskAdapter.addTask(task)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        rvTaskList.adapter = taskAdapter
        rvTaskList.layoutManager = LinearLayoutManager(this)

        bAddTask.setOnClickListener {
            val taskName = etTaskName.text.toString()
            if(taskName.isNotEmpty()){
                val task = Task(taskName)
                database.child(taskName).setValue(task)
                taskAdapter.addTask(task)
                etTaskName.text.clear()
            }
        }

        bDelTask.setOnClickListener {
            taskAdapter.delCheckedTasks()
        }
    }
}