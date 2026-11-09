package com.example.hikvision.analytics
import android.graphics.Bitmap
import android.graphics.RectF
data class DetectionResult(
    val id: String,
    val label: String,
    val confidence: Float,
    val boundingBox: RectF,
    val color: Int = 0xFF00FF00.toInt(),
    val extraData: Map<String, Any> = emptyMap()
)
interface VisionModule {
    val id: String
    val name: String
    var isEnabled: Boolean
    fun processFrame(bitmap: Bitmap, timestampMs: Long): List<DetectionResult>
}
