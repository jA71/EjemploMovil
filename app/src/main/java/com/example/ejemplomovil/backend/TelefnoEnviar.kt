package com.casella.turecuador.db

class TelefnoEnviar private constructor(
    val cedula: String? = null,
    val telefono: String? = null,
) {
    data class Builder(
        var cedula: String? = null,
        var telefono: String? = null,
    ) {
        fun cedula(cedula: String) = apply { this.cedula = cedula }
        fun telefono(telefono: String) = apply { this.telefono = telefono }
        fun build() = TelefnoEnviar(cedula, telefono)
    }
}