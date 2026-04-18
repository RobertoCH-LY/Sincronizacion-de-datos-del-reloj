package com.example.sincronizacionreloj

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mediciones_reloj")
data class MedicionReloj(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long,
    val tipoMedicion: String,
    val valor: Double,
    val unidad: String
)