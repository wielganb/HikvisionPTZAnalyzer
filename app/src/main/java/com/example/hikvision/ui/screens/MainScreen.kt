package com.example.hikvision.ui.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.hikvision.analytics.DetectionResult
import com.example.hikvision.analytics.VisionModule
import com.example.hikvision.camera.OnvifPtzController
import com.example.hikvision.model.CameraSettings
import com.example.hikvision.ui.components.CameraOverlay
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    settings: CameraSettings,
    ptzController: OnvifPtzController,
    modules: List<VisionModule>,
    onOpenSettings: () -> Unit
) {
    var activeDetections by remember { mutableStateOf<List<DetectionResult>>(emptyList()) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hikvision PTZ Analyzer") },
                actions = {
                    IconButton(onClick = onOpenSettings) { Text("⚙️") }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.Black)
        ) {
            Text(
                text = "Połączono: ${settings.getRtspUrl()}",
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
            CameraOverlay(
                detections = activeDetections,
                modifier = Modifier.fillMaxSize()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .align(Alignment.TopStart),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                modules.forEach { module ->
                    var checked by remember { mutableStateOf(module.isEnabled) }
                    FilterChip(
                        selected = checked,
                        onClick = {
                            checked = !checked
                            module.isEnabled = checked
                        },
                        label = { Text(module.name) }
                    )
                }
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = { ptzController.move(0f, 0.4f, 0f) }) { Text("▲") }
                Row {
                    Button(onClick = { ptzController.move(-0.4f, 0f, 0f) }) { Text("◄") }
                    Spacer(modifier = Modifier.width(4.dp))
                    Button(onClick = { ptzController.stop() }) { Text("■") }
                    Spacer(modifier = Modifier.width(4.dp))
                    Button(onClick = { ptzController.move(0.4f, 0f, 0f) }) { Text("►") }
                }
                Button(onClick = { ptzController.move(0f, -0.4f, 0f) }) { Text("▼") }
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    Button(onClick = { ptzController.move(0f, 0f, 0.5f) }) { Text("Zoom +") }
                    Spacer(modifier = Modifier.width(4.dp))
                    Button(onClick = { ptzController.move(0f, 0f, -0.5f) }) { Text("Zoom -") }
                }
            }
        }
    }
}
