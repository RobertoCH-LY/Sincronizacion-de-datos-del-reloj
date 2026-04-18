package com.example.sincronizacionreloj

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "acumulados_reloj")
data class AcumuladoReloj(
    @PrimaryKey
    val fecha: String, // formato "2026-03-16"
    val pasosTotales: Int,
    val ritmoPromedio: Double,
    val distanciaTotal: Double,
    val caloriasTotal: Double,
    val horasSueno: Double
)