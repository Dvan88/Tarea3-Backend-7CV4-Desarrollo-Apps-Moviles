package com.example.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RegisterFragment : Fragment() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        etUsername = view.findViewById(R.id.etUsername)
        etPassword = view.findViewById(R.id.etPassword)
        btnRegister = view.findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Llena todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                btnRegister.isEnabled = false

                lifecycleScope.launch {
                    try {
                        val response = RetrofitInstance.api.registerUser(RegisterRequest(username, password))
                        if (response.isSuccessful) {
                            Toast.makeText(requireContext(), response.body()?.message ?: "Usuario registrado", Toast.LENGTH_SHORT).show()
                            etUsername.text.clear()
                            etPassword.text.clear()
                        } else {
                            Toast.makeText(requireContext(), "Usuario ya existe o error: ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        val friendlyMessage = when (e) {
                            is ConnectException -> "No se pudo conectar al servidor. Verifica que el servicio esté corriendo."
                            is SocketTimeoutException -> "Tiempo de espera agotado. El servidor tarda mucho en responder."
                            is UnknownHostException -> "No se encontró la dirección del servidor. Revisa tu red."
                            else -> "Error de conexión: ${e.localizedMessage}"
                        }
                        Toast.makeText(requireContext(), friendlyMessage, Toast.LENGTH_LONG).show()
                    } finally {
                        btnRegister.isEnabled = true
                    }
                }
            }
        }

        return view
    }
}