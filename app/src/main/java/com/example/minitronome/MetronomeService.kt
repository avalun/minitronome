package com.example.minitronome

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log

class MetronomeService: Service() {

    private var bpm = 120
    private var interval = 500L

    private lateinit var timer: CountDownTimer

    private lateinit var tickListener : TickListener

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        setBpm(120)
    }

    private fun toInterval(bpm: Int): Long {
        return (60000 / bpm).toLong()
    }

    private fun toBpm(interval: Long): Int {
        return (60000 / interval).toInt()
    }

    fun setBpm(bpm: Int) {
        this.bpm = bpm
        this.interval = toInterval(bpm)

        // Maybe using a handler with post would be better for subdivisions
        // Would have to make this class runnable and post the run via a handler
        timer = object : CountDownTimer(interval, 1) {
            override fun onTick(millisUntilFinished: Long) {
                // Animation and stuff
            }

            override fun onFinish() {
                tick()
            }
        }

        if (::tickListener.isInitialized)
            tickListener.onBpmChanged(bpm)
    }

    fun tick() {
        Log.d("Metronome", "Tick")
        timer.start()

        if (::tickListener.isInitialized)
            tickListener.onTick()
    }

    fun startTimer() {
        timer.start()

        if (::tickListener.isInitialized)
            tickListener.onStartTicks()
    }

    fun stopTimer() {
        timer.cancel()

        if (::tickListener.isInitialized)
            tickListener.onStopTicks()
    }

    // Object subscribing to listening to ticks
    fun setTickListener(listener: TickListener) {
        this.tickListener = listener
    }

    interface TickListener {
        fun onTick()
        fun onBpmChanged(bpm: Int)
        fun onStartTicks()
        fun onStopTicks()
    }
}