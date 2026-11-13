package edu.virginia.cs4720.StepActivityLogger

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import kotlin.math.sqrt
@Entity(tableName = "logs")
data class LogEntity(
    @PrimaryKey val day: Long,
    val stepCount: Int = 0
)

@RequiresApi(Build.VERSION_CODES.O)
fun Log.toEntity() = LogEntity(
    day = date.toEpochDay(), stepCount = stepCount
)

@RequiresApi(Build.VERSION_CODES.O)
fun LogEntity.toLog() = Log(
    date = LocalDate.ofEpochDay(day), stepCount = stepCount
)


data class SensorUiState (
    val accelerometerX: Float = 0f,
    val accelerometerY: Float = 0f,
    val accelerometerZ: Float = 0f,
    val sessionSteps: Int = 0,
    val steps: Int = 0,
    val hardwareCounter : Boolean = false,
    val stepsPerm : Boolean = false
)

class StepCounter(context: Context) : SensorEventListener {
    private val sm = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accel = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val counter = sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    private val _state = MutableStateFlow(SensorUiState(hardwareCounter = counter != null))

    val state: StateFlow<SensorUiState> = _state

    private var baseline: Float? = null

    private val recentMagnitudes = ArrayDeque<Float>()
    private var lastSmoothed = 0f
    private var secondLastSmoothed = 0f
    private var lastStepsMs = 0L
    private var sessionSteps = 0
    private var lastAccelEmitMs = 0L

    fun start(stepsAllowed: Boolean) {
        _state.update { it.copy(stepsPerm = stepsAllowed) }
        recentMagnitudes.clear()
        lastSmoothed = 0f
        secondLastSmoothed = 0f
        val manualCounting = stepsAllowed && counter == null
        val delay =
            if (manualCounting) SensorManager.SENSOR_DELAY_GAME else SensorManager.SENSOR_DELAY_UI
        accel?.let { sm.registerListener(this, it, delay) }
        if (stepsAllowed) counter?.let {
            sm.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    fun stop() = sm.unregisterListener(this)

    override fun onSensorChanged(sensorData: SensorEvent) {
        when (sensorData.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                if (counter == null && _state.value.stepsPerm) detectStep(sensorData)
                val now = System.currentTimeMillis()
                if (now - lastAccelEmitMs >= 150) {
                    lastAccelEmitMs = now
                    _state.update {
                        it.copy(
                            accelerometerX = sensorData.values[0],
                            accelerometerY = sensorData.values[1],
                            accelerometerZ = sensorData.values[2],
                            sessionSteps = sessionSteps
                        )
                    }
                }
            }

            Sensor.TYPE_STEP_COUNTER -> {
                val total = sensorData.values[0]
                if (baseline == null || total < baseline!!) baseline = total
                sessionSteps = (total - baseline!!).toInt()
                _state.update { it.copy(sessionSteps = sessionSteps) }
            }
        }
    }

    private fun detectStep(sensorData: SensorEvent) {
        val accelerometerX = sensorData.values[0]
        val accelerometerY = sensorData.values[1]
        val accelerometerZ = sensorData.values[2]

        val magnitude =
            sqrt(accelerometerX * accelerometerX + accelerometerY * accelerometerY + accelerometerZ * accelerometerZ)

        recentMagnitudes.addLast(magnitude)
        if (recentMagnitudes.size > WINDOW_SIZE) recentMagnitudes.removeFirst()
        val smoothed = recentMagnitudes.average().toFloat()

        val nowMs = sensorData.timestamp / 1_000_000
        val isPeak = lastSmoothed > secondLastSmoothed &&
                lastSmoothed >= smoothed &&
                lastSmoothed > THRESHOLD
        if (isPeak && nowMs - lastStepsMs >= MIN_GAP_MS) {
            sessionSteps++
            lastStepsMs = nowMs
            _state.update { it.copy(sessionSteps = sessionSteps) }
        }
        secondLastSmoothed = lastSmoothed
        lastSmoothed = smoothed
    }

    override fun onAccuracyChanged(sensorData: Sensor?, accuracy: Int) {}

    companion object {
        const val WINDOW_SIZE = 5
        const val THRESHOLD = 11f
        const val MIN_GAP_MS = 300L
    }
}

