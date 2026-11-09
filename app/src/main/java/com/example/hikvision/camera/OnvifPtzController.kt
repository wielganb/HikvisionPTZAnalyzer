package com.example.hikvision.camera
import com.example.hikvision.model.CameraSettings
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
class OnvifPtzController(private val settings: CameraSettings) {
    private val client = OkHttpClient.Builder()
        .authenticator { _, response ->
            val credential = Credentials.basic(settings.username, settings.password)
            response.request.newBuilder().header("Authorization", credential).build()
        }
        .build()
    fun move(pan: Float, tilt: Float, zoom: Float) {
        val xml = """
            <?xml version="1.0" encoding="utf-8"?>
            <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" 
                           xmlns:tptz="http://www.onvif.org/ver20/ptz/wsdl" 
                           xmlns:tt="http://www.onvif.org/ver10/schema">
              <soap:Body>
                <tptz:ContinuousMove>
                  <tptz:ProfileToken>Profile_1</tptz:ProfileToken>
                  <tptz:Velocity>
                    <tt:PanTilt x="$pan" y="$tilt"/>
                    <tt:Zoom x="$zoom"/>
                  </tptz:Velocity>
                </tptz:ContinuousMove>
              </soap:Body>
            </soap:Envelope>
        """.trimIndent()
        sendSoapRequest(xml)
    }
    fun stop() {
        val xml = """
            <?xml version="1.0" encoding="utf-8"?>
            <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" 
                           xmlns:tptz="http://www.onvif.org/ver20/ptz/wsdl">
              <soap:Body>
                <tptz:Stop>
                  <tptz:ProfileToken>Profile_1</tptz:ProfileToken>
                </tptz:Stop>
              </soap:Body>
            </soap:Envelope>
        """.trimIndent()
        sendSoapRequest(xml)
    }
    private fun sendSoapRequest(xml: String) {
        val url = "http://${settings.ipAddress}:${settings.httpPort}/onvif/ptz_service"
        val request = Request.Builder()
            .url(url)
            .post(xml.toRequestBody("application/soap+xml; charset=utf-8".toMediaType()))
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) { e.printStackTrace() }
            override fun onResponse(call: Call, response: Response) { response.close() }
        })
    }
}
