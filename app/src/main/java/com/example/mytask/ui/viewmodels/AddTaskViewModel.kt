package com.example.mytask.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytask.data.Task
import com.example.mytask.repositories.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class AddTaskViewModel : ViewModel() {

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()

    fun onTitleChange(newTitle: String) {
        _title.value = newTitle
    }

    fun onDescriptionChange(newDescription: String) {
        _description.value = newDescription
    }

    fun addTask() {
        viewModelScope.launch {
            val task = Task(
                id = System.currentTimeMillis().toString(),
                title = _title.value,
                description = _description.value,
                dueDate = Date(),
                priority = 1,
                tags = null,
                isCompleted = false,
                owner = "user1",
                assignees = null
            )
            TaskRepository.addTask(task)
        }
    }
}
