package com.example.my_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

enum class TaskState {FREE, PRE_WORKING, CONNECTING, POST_WORKING, COMPLETED}


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val timer = Timer()
        var taskState = TaskState.FREE
        var taskProgress = 0
        val button = findViewById<Button>(R.id.button)
        val textView = findViewById<TextView>(R.id.textView)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)


        button.setOnClickListener {
            timer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    when (taskState) {
                        TaskState.FREE -> {
                            taskState = TaskState.PRE_WORKING
                            ++taskProgress
                            runOnUiThread{
                                textView.text = "Working..."
                            }
                            progressBar.progress = taskProgress
                        }

                        TaskState.PRE_WORKING -> {
                            if (taskProgress < 50) {
                                ++taskProgress

                                progressBar.progress = taskProgress
                            } else {
                                taskState = TaskState.CONNECTING
                                runOnUiThread {
                                    textView.text = "Connecting to server..."
                                    progressBar.isIndeterminate = true
                                }

                                this.cancel()
                            }
                        }
                        TaskState.CONNECTING -> {
                            taskState = TaskState.POST_WORKING
                            ++taskProgress
                            runOnUiThread {
                                textView.text = "Working..."
                                progressBar.isIndeterminate = false
                            }
                            progressBar.progress = taskProgress
                        }

                        TaskState.POST_WORKING -> {
                            if (taskProgress < 100) {
                                ++taskProgress

                                progressBar.progress = taskProgress
                            } else {
                                runOnUiThread {
                                    textView.text = "Completed!"
                                }
                                taskState = TaskState.COMPLETED
                            }
                        }

                        TaskState.COMPLETED -> {
                            timer.cancel()
                        }
                    }
                }
            }, 1000, 250)
        }
    }
}
