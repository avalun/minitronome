package com.example.minitronome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        button?.setOnClickListener()
        {
            fun setStop() = if (button.text == "Start") {
                button.setText("Stop")
            }else{
                button.setText("Start")
            }

            setStop()
            Toast.makeText(this@MainActivity,"Button 1 clicked", Toast.LENGTH_LONG).show()
        }

    }
}
