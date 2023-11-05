package com.alanturing.cpifp.todo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alanturing.cpifp.todo.R
import com.alanturing.cpifp.todo.data.TaskLocalRepository
import com.alanturing.cpifp.todo.databinding.ActivityEditBinding
import com.alanturing.cpifp.todo.databinding.ActivityMainBinding
import com.alanturing.cpifp.todo.model.Task

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    private val repository = TaskLocalRepository.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = getIntent()
        val task: Task? = intent?.extras?.getParcelable("TASK")
        var taskTitle = ""
        var taskDescription = ""
        var taskId = 0
        var isCompleted = false

        if (task!=null) {
            taskTitle = task.title.toString()
            taskDescription = task.description.toString()
            taskId = task.id
            isCompleted = task.isCompleted
        }

        binding.titleInput.setText(taskTitle)
        binding.descriptionInput.setText(taskDescription)

        binding.isCompleted.isChecked = isCompleted

        binding.buttonConfirm.setOnClickListener {
            val task = Task(taskId, binding.titleInput.text.toString(), binding.descriptionInput.text.toString(), binding.isCompleted.isChecked)
            repository.update(task)
            setResult(Activity.RESULT_OK)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.cancelButton.setOnClickListener{
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

    }
}