package org.utl.dovasystemcompose

data class RegistroCaptacion(val fechaHora: String, val litros: Int)

fun getHistorialMock(): List<RegistroCaptacion> {
    return listOf(
        RegistroCaptacion("14/07/2025 10:00", 120),
        RegistroCaptacion("14/07/2025 09:00", 98),
        RegistroCaptacion("14/07/2025 08:00", 115),
        RegistroCaptacion("13/07/2025 22:00", 140)
    )
}