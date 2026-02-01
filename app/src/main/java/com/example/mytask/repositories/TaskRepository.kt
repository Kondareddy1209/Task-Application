package com.example.mytask.repositories

import com.example.mytask.data.Task
import java.util.Date

object TaskRepository {
    private val tasks = mutableListOf(
        Task("1", "Buy groceries", "Milk, Bread, Eggs", Date(), 1, listOf("personal"), false, "user1", null),
        Task("2", "Finish report", "Q3 sales report", Date(), 2, listOf("work"), false, "user1", listOf("user2")),
        Task("3", "Call mom", null, Date(), 1, listOf("personal"), true, "user1", null)
    )

    fun getTasks(): List<Task> = tasks

    fun addTask(task: Task) {
        tasks.add(task)
    }
}