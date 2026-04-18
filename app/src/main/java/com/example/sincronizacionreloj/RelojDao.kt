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

    @Query("SELECT COALESCE(SUM(valor), 0.0) FROM mediciones_reloj WHERE tipoMedicion = 'pasos' AND timestamp >= :desde")
    suspend fun getPasosTotales(desde: Long): Double

    @Query("SELECT COALESCE(AVG(valor), 0.0) FROM mediciones_reloj WHERE tipoMedicion = 'frecuencia_cardiaca' AND timestamp >= :desde")
    suspend fun getRitmoPromedio(desde: Long): Double

    @Query("SELECT COALESCE(SUM(valor), 0.0) FROM mediciones_reloj WHERE tipoMedicion = 'sueno' AND timestamp >= :desde")
    suspend fun getHorasSueno(desde: Long): Double

    @Query("SELECT * FROM acumulados_reloj ORDER BY fecha DESC")
    suspend fun getAcumulados(): List<AcumuladoReloj>
}