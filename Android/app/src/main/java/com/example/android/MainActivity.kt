package com.example.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class MainActivity : AppCompatActivity() {

    private lateinit var textViewMessage: TextView
    private lateinit var spinnerMenu: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewMessage = findViewById(R.id.textViewMessage)
        spinnerMenu = findViewById(R.id.spinnerMenu)

        val options = arrayOf("Selecciona acción", "Conexión", "Registrar", "Login")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMenu.adapter = adapter

        spinnerMenu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                when (position) {
                    1 -> {  }
                    2 -> startActivity(Intent(this@MainActivity, RegisterActivity::class.java))
                    3 -> startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                }
                spinnerMenu.setSelection(0)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.checkApi()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        val displayText = "Service: ${body.service}\n" +
                                "Status: ${body.status}\n" +
                                "Version: ${body.version}"
                        textViewMessage.text = displayText
                    } else {
                        textViewMessage.text = "Respuesta vacía"
                    }
                } else {
                    textViewMessage.text = "Error del servidor: ${response.code()}"
                }
            } catch (e: Exception) {
                val friendlyMessage = when (e) {
                    is ConnectException -> "No se pudo conectar al servidor. Verifica que el servicio esté corriendo."
                    is SocketTimeoutException -> "Tiempo de espera agotado. El servidor tarda mucho en responder."
                    is UnknownHostException -> "No se encontró la dirección del servidor. Revisa tu red."
                    else -> "Error de conexión: ${e.localizedMessage}"
                }
                textViewMessage.text = friendlyMessage
            }
        }
    }
}
