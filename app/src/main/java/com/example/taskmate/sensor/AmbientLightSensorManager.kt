package com.example.taskmate.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.taskmate.ui.theme.ThemeState

/**
 * Pengelola Hardware Ambient Light Sensor (Hardware Sensor - PAPB13).
 * Mendeteksi intensitas cahaya (lux) dan secara otomatis mengubah tema aplikasi
 * ke Dark Mode ketika intensitas cahaya ruangan di bawah ambang batas (gelap).
 */
class AmbientLightSensorManager(context: Context) : SensorEventListener {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val lightSensor: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

    val isSensorAvailable: Boolean
        get() = lightSensor != null

    fun startListening() {
        lightSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || event.sensor.type != Sensor.TYPE_LIGHT) return

        val lux = event.values[0]
        ThemeState.currentLuxValue = lux

        // Jika mode otomatis sensor diaktifkan oleh pengguna
        if (ThemeState.isAutoLightSensorEnabled) {
            if (lux < 15.0f) {
                // Ambang batas ruangan gelap -> Aktifkan Dark Mode
                ThemeState.setDarkMode(true)
            } else {
                // Terang -> Mode Terang
                ThemeState.setDarkMode(false)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not required for light sensor
    }
}
