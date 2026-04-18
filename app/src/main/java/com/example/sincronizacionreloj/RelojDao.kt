package com.example.sincronizacionreloj

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RelojDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarMedicion(medicion: MedicionReloj)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarAcumulado(acumulado: AcumuladoReloj)

    @Query("SELECT * FROM mediciones_reloj WHERE timestamp >= :desde AND timestamp <= :hasta AND tipoMedicion = :tipo")
    suspend fun getMedicionesPorFechaTipo(desde: Long, hasta: Long, tipo: String): List<MedicionReloj>

    @Query("SELECT * FROM acumulados_reloj WHERE fecha = :fecha")
    suspend fun getAcumuladoPorFecha(fecha: String): AcumuladoReloj?

    @Query("SELECT AVG(valor) FROM mediciones_reloj WHERE tipoMedicion = 'frecuencia_cardiaca' AND timestamp >= :desde")
    suspend fun getRitmoPromedio(desde: Long): Double

    @Query("SELECT SUM(valor) FROM mediciones_reloj WHERE tipoMedicion = 'pasos' AND timestamp >= :desde")
    suspend fun getPasosTotales(desde: Long): Double
}