package com.example.hikvision
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.hikvision.analytics.modules.AnprModule
import com.example.hikvision.analytics.modules.SpeedEstimationModule
import com.example.hikvision.camera.OnvifPtzController
import com.example.hikvision.data.SettingsRepository
import com.example.hikvision.model.CameraSettings
import com.example.hikvision.ui.screens.LoginScreen
import com.example.hikvision.ui.screens.MainScreen
import kotlinx.coroutines.launch
class MainActivity : ComponentActivity() {
    private lateinit var settingsRepository: SettingsRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsRepository = SettingsRepository(applicationContext)
        val modules = listOf(
            AnprModule(),
            SpeedEstimationModule()
        )
        setContent {
            val scope = rememberCoroutineScope()
            val cameraSettingsState by settingsRepository.cameraSettingsFlow.collectAsState(initial = CameraSettings())
            var isConfigured by remember { mutableStateOf(false) }
            if (!isConfigured) {
                LoginScreen(
                    initialSettings = cameraSettingsState,
                    onSaveAndConnect = { newSettings ->
                        scope.launch {
                            settingsRepository.saveSettings(newSettings)
                            isConfigured = true
                        }
                    }
                )
            } else {
                val ptzController = remember(cameraSettingsState) { OnvifPtzController(cameraSettingsState) }
                MainScreen(
                    settings = cameraSettingsState,
                    ptzController = ptzController,
                    modules = modules,
                    onOpenSettings = { isConfigured = false }
                )
            }
        }
    }
}
