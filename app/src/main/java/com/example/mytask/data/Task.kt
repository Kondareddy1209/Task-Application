package com.example.mytask.data

import java.util.Date

data class Task(
    val id: String,
    val title: String,
    val description: String?,
    val dueDate: Date?,
    val priority: Int,
    val tags: List<String>?,
    val isCompleted: Boolean,
    val owner: String,
    val assignees: List<String>?
)
