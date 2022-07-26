package com.example.ejemplomovil

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.casella.turecuador.db.Backend
import com.casella.turecuador.db.DatosEnviar
import com.example.ejemplomovil.backend.DatosUsuario
import com.example.ejemplomovil.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    private lateinit var views: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        views = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(views.root)
        //logearse()
        cargarCedula()
    }

    private fun cargarCedula(){
        val cedula = intent.getStringExtra("cedula")
        cedula?.let {
            views.user.setText(it.toString().trim())
        }
    }

    private fun logearse() {
        views.loggin.setOnClickListener {
            val datos = DatosEnviar.Builder().user(views.user.text.toString().trim())
                .clave(views.password.text.toString().trim()).build()
            Backend.api()?.logearse(datos)?.enqueue(object : Callback<DatosUsuario> {
                override fun onResponse(
                    call: Call<DatosUsuario>,
                    response: Response<DatosUsuario>
                ) {
                    if (response.isSuccessful) {
                        agregarPreferencas(response.body())
                        finalizarRegresar(response.headers().get("authorization").toString().trim())
                    } else {
                        Toast.makeText(
                            this@Login,
                            response.message().toString().trim(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<DatosUsuario>, t: Throwable) {
                    Toast.makeText(this@Login, t.message.toString().trim(), Toast.LENGTH_SHORT)
                        .show()
                }

            })
        }
    }

    private fun finalizarRegresar(token: String) {
        setResult(Activity.RESULT_OK, intent.putExtra("token", token))
        finish()
    }

    private fun agregarPreferencas(datosUsuario: DatosUsuario?) {
        getSharedPreferences("globales", Context.MODE_PRIVATE).edit()
            .putString("nombres", datosUsuario?.nombres)
            .putString("email", datosUsuario?.email)
            .putString("cedula", datosUsuario?.cedula)
            .putString("foto", datosUsuario?.urlFoto).apply()
    }
}