package com.example.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var textViewMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewMessage = findViewById(R.id.textViewMessage)

        // Llamada al backend
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.checkApi()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        // Construimos el mensaje a mostrar
                        val displayText = "Service: ${body.service}\n" +
                                "Status: ${body.status}\n" +
                                "Version: ${body.version}"
                        textViewMessage.text = displayText
                    } else {
                        textViewMessage.text = "Respuesta vacía"
                    }
                } else {
                    textViewMessage.text = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                textViewMessage.text = "Error de red: ${e.message}"
            }
        }
    }
}