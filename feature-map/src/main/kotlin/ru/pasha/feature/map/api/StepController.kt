package ru.pasha.feature.map.api

import android.Manifest
import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StepController(private val context: Context) : SensorEventListener {

    val stepsFlow: StateFlow<Int> = _stepsFlow.asStateFlow()

    private val sensorManager by lazy {
        context.getSystemService(SENSOR_SERVICE) as SensorManager
    }

    private val stepSensor by lazy {
        sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
    }

    fun startCounting() {
        if (hasPermissions() && isStepCounterAvailable()) {
            stepSensor?.let {
                sensorManager.registerListener(
                    this,
                    it,
                    SensorManager.SENSOR_DELAY_FASTEST
                )
            }
        }
    }

    fun stopCounting() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_STEP_DETECTOR) {
            _stepsFlow.update { it + 1 }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) = Unit

    private fun isStepCounterAvailable(): Boolean {
        val sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ->
                sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null
            else ->
                sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null
        }
    }

    private fun hasPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    companion object {
        private val _stepsFlow = MutableStateFlow(0)
    }
}
