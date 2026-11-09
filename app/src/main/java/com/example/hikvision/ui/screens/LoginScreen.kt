package com.example.hikvision.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.hikvision.model.CameraSettings
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    initialSettings: CameraSettings,
    onSaveAndConnect: (CameraSettings) -> Unit
) {
    var ipAddress by remember { mutableStateOf(initialSettings.ipAddress) }
    var httpPort by remember { mutableStateOf(initialSettings.httpPort.toString()) }
    var rtspPort by remember { mutableStateOf(initialSettings.rtspPort.toString()) }
    var username by remember { mutableStateOf(initialSettings.username) }
    var password by remember { mutableStateOf(initialSettings.password) }
    var mainChannel by remember { mutableStateOf(initialSettings.mainChannel.toString()) }
    Scaffold(
        topBar = { TopAppBar(title = { Text("Ustawienia Kamery") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = ipAddress,
                onValueChange = { ipAddress = it },
                label = { Text("Adres IP Kamery") },
                modifier = Modifier.fillMaxWidth()
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = httpPort,
                    onValueChange = { httpPort = it },
                    label = { Text("Port HTTP/ONVIF") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = rtspPort,
                    onValueChange = { rtspPort = it },
                    label = { Text("Port RTSP") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Użytkownik") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Hasło") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = mainChannel,
                onValueChange = { mainChannel = it },
                label = { Text("Kanał RTSP") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    val settings = CameraSettings(
                        ipAddress = ipAddress.trim(),
                        httpPort = httpPort.toIntOrNull() ?: 80,
                        rtspPort = rtspPort.toIntOrNull() ?: 554,
                        username = username.trim(),
                        password = password,
                        mainChannel = mainChannel.toIntOrNull() ?: 101
                    )
                    onSaveAndConnect(settings)
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Zapisz i Połącz")
            }
        }
    }
}
