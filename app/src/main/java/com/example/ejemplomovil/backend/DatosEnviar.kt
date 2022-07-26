package com.casella.turecuador.db

class DatosEnviar private constructor(
    val usuario: String? = null,
    val clave: String? = null,
) {
    data class Builder(
        var usuario: String? = null,
        var clave: String? = null,
    ) {
        fun user(usuario: String) = apply { this.usuario = usuario }
        fun clave(clave: String) = apply { this.clave = clave }
        fun build() = DatosEnviar(usuario, clave)
    }
}