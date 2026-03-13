package com.example.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var spinnerMenu: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        spinnerMenu = findViewById(R.id.spinnerMenu)

        val options = arrayOf("Selecciona acción", "Conexión", "Registrar", "Login")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMenu.adapter = adapter

        spinnerMenu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
                    1 -> startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                    2 -> {  }
                    3 -> startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
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
                } else {
                }
            } catch (e: Exception) {
            }
        }
    }
}