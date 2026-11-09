package com.example.hikvision.analytics.modules
import android.graphics.Bitmap
import android.graphics.PointF
import android.graphics.RectF
import com.example.hikvision.analytics.DetectionResult
import com.example.hikvision.analytics.VisionModule
import kotlin.math.hypot
class SpeedEstimationModule(
    private val metersPerPixel: Float = 0.05f
) : VisionModule {
    override val id: String = "speed_module"
    override val name: String = "Pomiar Prędkości"
    override var isEnabled: Boolean = true
    private var lastCenter: PointF? = null
    private var lastTimestampMs: Long = 0L
    override fun processFrame(bitmap: Bitmap, timestampMs: Long): List<DetectionResult> {
        if (!isEnabled) return emptyList()
        val currentBox = RectF(0.20f, 0.40f, 0.45f, 0.70f)
        val currentCenter = PointF(currentBox.centerX() * bitmap.width, currentBox.centerY() * bitmap.height)
        var speedKmh = 0.0
        if (lastCenter != null && lastTimestampMs > 0 && timestampMs > lastTimestampMs) {
            val deltaX = currentCenter.x - lastCenter!!.x
            val deltaY = currentCenter.y - lastCenter!!.y
            val distancePixels = hypot(deltaX.toDouble(), deltaY.toDouble()).toFloat()
            val distanceMeters = distancePixels * metersPerPixel
            val timeSeconds = (timestampMs - lastTimestampMs) / 1000.0
            if (timeSeconds > 0) {
                speedKmh = (distanceMeters / timeSeconds) * 3.6
            }
        }
        lastCenter = currentCenter
        lastTimestampMs = timestampMs
        val color = if (speedKmh > 50) 0xFFFF0000.toInt() else 0xFF00FF00.toInt()
        return listOf(
            DetectionResult(
                id = "speed_car_1",
                label = "Pojazd: %.1f km/h".format(speedKmh),
                confidence = 0.89f,
                boundingBox = currentBox,
                color = color,
                extraData = mapOf("speed_kmh" to speedKmh)
            )
        )
    }
}
