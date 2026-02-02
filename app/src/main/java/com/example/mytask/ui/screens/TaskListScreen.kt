package com.example.mytask.ui.screens

import android.app.Activity
import android.view.WindowManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mytask.ui.navigation.Screen
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

// Mock data for tasks
data class Task(val id: Int, val title: String, val location: LatLng?, val status: String)

val mockTasks = listOf(
    Task(1, "Submit design proposal", LatLng(34.0522, -118.2437), "Pending"),
    Task(2, "Pick up groceries", null, "Completed"),
    Task(3, "Finalize project report", LatLng(40.7128, -74.0060), "Pending"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(navController: NavController) {
    var showMap by remember { mutableStateOf(false) }
    var tasks by remember { mutableStateOf<List<Task>?>(null) }
    var selectedFilter by remember { mutableStateOf("Today") }

    // Prevent screenshots for this screen
    DisableScreenshots()

    LaunchedEffect(Unit) {
        // Simulate loading tasks
        kotlinx.coroutines.delay(1000)
        tasks = mockTasks
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("My Tasks", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "MAPTASK Logo",
                            modifier = Modifier.padding(start = 12.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    actions = {
                        IconButton(onClick = { showMap = !showMap }) {
                            Icon(
                                imageVector = if (showMap) Icons.Outlined.Map else Icons.Filled.Map,
                                contentDescription = if (showMap) "List View" else "Map View"
                            )
                        }
                        IconButton(onClick = { navController.navigate(Screen.Profile.route) }) {
                            Icon(imageVector = Icons.Default.Person, contentDescription = "Profile")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                )
                TaskFilter(selectedFilter = selectedFilter, onFilterSelected = { selectedFilter = it })
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddTask.route) },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task", tint = Color.White)
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (tasks == null) {
                // Loading indicator
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                val filteredTasks = tasks!!.filter {
                    when (selectedFilter) {
                        "Nearby" -> it.location != null
                        "Completed" -> it.status == "Completed"
                        else -> true
                    }
                }
                AnimatedVisibility(visible = !showMap, enter = fadeIn(), exit = fadeOut()) {
                    TaskList(tasks = filteredTasks, navController = navController, onTaskSwiped = { task, direction ->
                        val currentTasks = tasks?.toMutableList() ?: return@TaskList
                        if (direction == "right") { // Mark as complete
                            val index = currentTasks.indexOfFirst { it.id == task.id }
                            if (index != -1) {
                                currentTasks[index] = task.copy(status = "Completed")
                                tasks = currentTasks
                            }
                        } else if (direction == "left") { // Delete
                            tasks = tasks?.filterNot { it.id == task.id }
                        }
                    })
                }
                AnimatedVisibility(visible = showMap, enter = fadeIn(), exit = fadeOut()) {
                    TaskMapView(tasks = filteredTasks, navController = navController)
                }
            }
        }
    }
}

@Composable
fun TaskFilter(selectedFilter: String, onFilterSelected: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        FilterChip(selected = selectedFilter == "Today", onClick = { onFilterSelected("Today") }, label = { Text("Today") })
        FilterChip(selected = selectedFilter == "Nearby", onClick = { onFilterSelected("Nearby") }, label = { Text("Nearby") })
        FilterChip(selected = selectedFilter == "Completed", onClick = { onFilterSelected("Completed") }, label = { Text("Completed") })
    }
}

@Composable
fun TaskList(tasks: List<Task>, navController: NavController, onTaskSwiped: (Task, String) -> Unit) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(tasks, key = { it.id }) { task ->
            var offsetX by remember { mutableStateOf(0f) }
            AnimatedVisibility(
                visible = true, // Simplified for this example
                enter = slideInVertically(initialOffsetY = { it / 2 }, animationSpec = tween(500)) + fadeIn()
            ) {
                Box(modifier = Modifier.pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { change, dragAmount ->
                            offsetX += dragAmount
                            change.consume()
                        },
                        onDragEnd = {
                            if (offsetX > 300f) onTaskSwiped(task, "right")
                            else if (offsetX < -300f) onTaskSwiped(task, "left")
                            offsetX = 0f
                        }
                    )
                }) {
                    TaskCard(task = task, onClick = { /* Navigate to task details */ })
                    if (offsetX > 0) {
                        Row(modifier = Modifier.align(Alignment.CenterStart).padding(start = 16.dp)) {
                            Icon(Icons.Default.Done, "Done", tint = Color.Green)
                        }
                    }
                    if (offsetX < 0) {
                        Row(modifier = Modifier.align(Alignment.CenterEnd).padding(end = 16.dp)) {
                            Icon(Icons.Default.Delete, "Delete", tint = Color.Red)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TaskCard(task: Task, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (task.location != null) {
                Icon(Icons.Default.LocationOn, contentDescription = "Location", tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(16.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = task.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                if (task.location != null) {
                    Text(text = "Location-based task", style = MaterialTheme.typography.bodySmall)
                }
            }
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (task.status == "Completed") Color.Green.copy(alpha = 0.7f) else Color.Yellow.copy(alpha = 0.7f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(task.status, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
fun TaskMapView(tasks: List<Task>, navController: NavController) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(34.0522, -118.2437), 10f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        tasks.filter { it.location != null }.forEach { task ->
            Marker(
                state = MarkerState(position = task.location!!),
                title = task.title,
                snippet = task.status,
                onClick = {
                    // Navigate to task details or show a bottom sheet
                    false
                }
            )
        }
    }
}

@Composable
fun DisableScreenshots() {
    val activity = LocalContext.current as? Activity
    DisposableEffect(Unit) {
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        onDispose {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }
}
