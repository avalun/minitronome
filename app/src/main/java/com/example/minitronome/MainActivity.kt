package com.example.minitronome

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), MetronomeService.TickListener {

    var isBound = false
    private lateinit var mService: MetronomeService

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as MetronomeService.LocalBinder
            mService = binder.getService()

            mService.setTickListener(this@MainActivity)

            isBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
        }
    }

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

    override fun onStart() {
        super.onStart()

        // Bind to LocalService
        Intent(this, MetronomeService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()

        if (isBound) {
            unbindService(connection)
            isBound = false
        }
    }

    override fun onTick() {
        // TODO("Not yet implemented")
    }

    override fun onBpmChanged(bpm: Int) {
        // TODO("Not yet implemented")
    }

    override fun onStartTicks() {
        // TODO("Not yet implemented")
    }

    override fun onStopTicks() {
        // TODO("Not yet implemented")
    }
}
