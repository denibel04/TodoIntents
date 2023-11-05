package com.alanturing.cpifp.todo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.alanturing.cpifp.todo.adapter.TasksAdapter
import com.alanturing.cpifp.todo.data.TaskLocalRepository
import com.alanturing.cpifp.todo.databinding.ActivityMainBinding
import com.alanturing.cpifp.todo.model.Task
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val repository = TaskLocalRepository.getInstance()
    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        when (it.resultCode) {
            Activity.RESULT_OK -> {
                val taskAdapter = TasksAdapter(repository.tasks, ::onShareItem, ::onEdit)
                binding.tasks.adapter = taskAdapter
            }
            Activity.RESULT_CANCELED -> {
                Snackbar.make(this, binding.root, "Cancelado", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val taskAdapter = TasksAdapter(repository.tasks, ::onShareItem, ::onEdit)
        binding.tasks.adapter = taskAdapter

        binding.floatingButton.setOnClickListener {
            val intent = Intent(this, CreateToDoActivity::class.java)
            getResult.launch(intent)
        }

    }
    fun onShareItem(task: Task, view: View) {
        val statusText = if(task.isCompleted) "completada"
                         else "pendiente"
        val shareText = getString(R.string.share_text, task.title, task.description,statusText)
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(intent, null)
        startActivity(shareIntent)

    }

    fun onEdit(task: Task, view: View) {
        val intent = Intent(this, EditActivity::class.java)
        intent.putExtra("TASK", task)
        getResult.launch(intent)
    }
}