package com.example.minitronome

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log

class MetronomeService: Service() {

    private var bpm = 120
    private var interval = 500L

    private lateinit var timer: CountDownTimer
    private lateinit var soundPool: SoundPool
    private lateinit var tickListener : TickListener

    var soundId = 0

    private val binder: IBinder = LocalBinder()

    override fun onCreate() {
        // Create soundPool to playback audio
        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build())
            .build();

        soundId = soundPool.load(this, R.raw.wood_csharp, 1)
        setBpm(120)
        super.onCreate()
    }

    override fun onDestroy() {
        soundPool.release()
        super.onDestroy()
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
            var tickCounter = 0
            override fun onTick(millisUntilFinished: Long) {
                if(tickCounter > 9) {
                    // Animation and stuff
                    val progress = (1f - (millisUntilFinished / interval)) * 100f
                    tickListener.onProgress(progress)
                    tickCounter = 0
                } else {
                    tickCounter++
                }
            }

            override fun onFinish() {
                tick()
            }
        }

        if (::tickListener.isInitialized)
            tickListener.onBpmChanged(bpm)
    }

    fun tick() {
        timer.start()
        Log.d("Metronome", "Tick")
        soundPool.play(soundId, 1f, 1f, 0, 0, 1f)

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

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    interface TickListener {
        fun onTick()
        fun onBpmChanged(bpm: Int)
        fun onProgress(progress: Float)
        fun onStartTicks()
        fun onStopTicks()
    }

    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): MetronomeService = this@MetronomeService
    }
}