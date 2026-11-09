package com.example.hikvision.analytics.modules
import android.graphics.Bitmap
import android.graphics.RectF
import com.example.hikvision.analytics.DetectionResult
import com.example.hikvision.analytics.VisionModule
class AnprModule : VisionModule {
    override val id: String = "anpr_module"
    override val name: String = "Rozpoznawanie Tablic (ANPR)"
    override var isEnabled: Boolean = true
    override fun processFrame(bitmap: Bitmap, timestampMs: Long): List<DetectionResult> {
        if (!isEnabled) return emptyList()
        val sampleBoundingBox = RectF(0.35f, 0.60f, 0.55f, 0.68f)
        val recognizedPlate = "WI 7820X"
        return listOf(
            DetectionResult(
                id = "anpr_${timestampMs / 1000}",
                label = "Tablica: $recognizedPlate",
                confidence = 0.94f,
                boundingBox = sampleBoundingBox,
                color = 0xFF00E5FF.toInt(),
                extraData = mapOf("plate_number" to recognizedPlate)
            )
        )
    }
}
