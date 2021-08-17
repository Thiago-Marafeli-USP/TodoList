package com.raccoon.todolist

data class Task (
    val name: String,
    var isChecked: Boolean = false
)