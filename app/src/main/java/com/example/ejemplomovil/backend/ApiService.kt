package com.casella.turecuador.db

import com.example.ejemplomovil.backend.DatosUsuario
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST(value = "autentication/login")
    fun logearse(
        @Body datos: DatosEnviar,
    ): Call<DatosUsuario>

    @Headers("Content-Type: application/json")
    @POST(value = "telefono/agregar")
    fun agregarTelefono(
        @Header("authorization") token: String,
        @Body datos: TelefnoEnviar,
    ): Call<String>

    @Headers("Content-Type: application/json")
    @POST(value = "personas/buscar")
    fun buscarPersona(
        @Body datos: DatosEnviar,
    ): Call<List<DatosUsuario>>
}