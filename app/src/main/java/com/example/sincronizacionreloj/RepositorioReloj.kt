package com.example.sincronizacionreloj

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class RepositorioReloj(private val context: Context) {

    private val client = HealthConnectClient.getOrCreate(context)
    private val dao = AppDatabase.getInstance(context).relojDao()
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    suspend fun sincronizarDiaActual() {
        val hoy = LocalDate.now()
        val inicio = hoy.atStartOfDay(ZoneId.systemDefault()).toInstant()
        val fin = Instant.now()
        val fechaStr = hoy.format(formatter)

        leerFrecuenciaCardiaca(inicio, fin)
        leerPasos(inicio, fin)
        leerSueno(inicio, fin)
        calcularYGuardarAcumulado(fechaStr, inicio)
    }

    private suspend fun leerFrecuenciaCardiaca(inicio: Instant, fin: Instant) {
        val respuesta = client.readRecords(
            ReadRecordsRequest(
                recordType = HeartRateRecord::class,
                timeRangeFilter = TimeRangeFilter.between(inicio, fin)
            )
        )
        respuesta.records.forEach { record ->
            record.samples.forEach { sample ->
                dao.insertarMedicion(
                    MedicionReloj(
                        timestamp = sample.time.toEpochMilli(),
                        tipoMedicion = "frecuencia_cardiaca",
                        valor = sample.beatsPerMinute.toDouble(),
                        unidad = "bpm"
                    )
                )
            }
        }
    }

    private suspend fun leerPasos(inicio: Instant, fin: Instant) {
        val respuesta = client.readRecords(
            ReadRecordsRequest(
                recordType = StepsRecord::class,
                timeRangeFilter = TimeRangeFilter.between(inicio, fin)
            )
        )
        respuesta.records.forEach { record ->
            dao.insertarMedicion(
                MedicionReloj(
                    timestamp = record.startTime.toEpochMilli(),
                    tipoMedicion = "pasos",
                    valor = record.count.toDouble(),
                    unidad = "pasos"
                )
            )
        }
    }

    private suspend fun leerSueno(inicio: Instant, fin: Instant) {
        val respuesta = client.readRecords(
            ReadRecordsRequest(
                recordType = SleepSessionRecord::class,
                timeRangeFilter = TimeRangeFilter.between(inicio, fin)
            )
        )
        respuesta.records.forEach { record ->
            val horas = (record.endTime.epochSecond - record.startTime.epochSecond) / 3600.0
            dao.insertarMedicion(
                MedicionReloj(
                    timestamp = record.startTime.toEpochMilli(),
                    tipoMedicion = "sueno",
                    valor = horas,
                    unidad = "horas"
                )
            )
        }
    }

    private suspend fun calcularYGuardarAcumulado(fecha: String, desde: Instant) {
        val pasos = dao.getPasosTotales(desde.toEpochMilli())
        val ritmo = dao.getRitmoPromedio(desde.toEpochMilli())

        dao.insertarAcumulado(
            AcumuladoReloj(
                fecha = fecha,
                pasosTotales = pasos.toInt(),
                ritmoPromedio = ritmo,
                distanciaTotal = 0.0,
                caloriasTotal = 0.0
            )
        )
    }
}