package com.example.hikvision.model
data class CameraSettings(
    val ipAddress: String = "192.168.1.64",
    val httpPort: Int = 80,
    val rtspPort: Int = 554,
    val username: String = "admin",
    val password: String = "",
    val mainChannel: Int = 101,
    val subChannel: Int = 102
) {
    fun getRtspUrl(useSubStream: Boolean = false): String {
        val channel = if (useSubStream) subChannel else mainChannel
        return "rtsp://$username:$password@$ipAddress:$rtspPort/Streaming/Channels/$channel"
    }
}
