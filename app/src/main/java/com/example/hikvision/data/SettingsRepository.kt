package com.example.hikvision.data
import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.hikvision.model.CameraSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
private val Context.dataStore by preferencesDataStore(name = "camera_settings")
class SettingsRepository(private val context: Context) {
    companion object {
        private val IP_ADDRESS = stringPreferencesKey("ip_address")
        private val HTTP_PORT = intPreferencesKey("http_port")
        private val RTSP_PORT = intPreferencesKey("rtsp_port")
        private val USERNAME = stringPreferencesKey("username")
        private val PASSWORD = stringPreferencesKey("password")
        private val MAIN_CHANNEL = intPreferencesKey("main_channel")
        private val SUB_CHANNEL = intPreferencesKey("sub_channel")
    }
    val cameraSettingsFlow: Flow<CameraSettings> = context.dataStore.data.map { prefs ->
        CameraSettings(
            ipAddress = prefs[IP_ADDRESS] ?: "192.168.1.64",
            httpPort = prefs[HTTP_PORT] ?: 80,
            rtspPort = prefs[RTSP_PORT] ?: 554,
            username = prefs[USERNAME] ?: "admin",
            password = prefs[PASSWORD] ?: "",
            mainChannel = prefs[MAIN_CHANNEL] ?: 101,
            subChannel = prefs[SUB_CHANNEL] ?: 102
        )
    }
    suspend fun saveSettings(settings: CameraSettings) {
        context.dataStore.edit { prefs ->
            prefs[IP_ADDRESS] = settings.ipAddress
            prefs[HTTP_PORT] = settings.httpPort
            prefs[RTSP_PORT] = settings.rtspPort
            prefs[USERNAME] = settings.username
            prefs[PASSWORD] = settings.password
            prefs[MAIN_CHANNEL] = settings.mainChannel
            prefs[SUB_CHANNEL] = settings.subChannel
        }
    }
}
