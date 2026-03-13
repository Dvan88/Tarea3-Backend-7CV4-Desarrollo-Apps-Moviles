package com.example.android

import android.content.Intent
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

class LoginFragment : Fragment() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        etUsername = view.findViewById(R.id.etUsername)
        etPassword = view.findViewById(R.id.etPassword)
        btnLogin = view.findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Llena todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                btnLogin.isEnabled = false

                lifecycleScope.launch {
                    try {
                        val response = RetrofitInstance.api.loginUser(LoginRequest(username, password))
                        if (response.isSuccessful) {
                            val intent = Intent(requireContext(), BienvenidaActivity::class.java)
                            intent.putExtra("username", username)
                            startActivity(intent)
                        } else {
                            Toast.makeText(requireContext(), "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
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
                        btnLogin.isEnabled = true
                    }
                }
            }
        }

        return view
    }
}