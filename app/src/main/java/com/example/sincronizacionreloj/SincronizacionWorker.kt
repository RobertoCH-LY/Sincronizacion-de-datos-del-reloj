package com.example.sincronizacionreloj

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class SincronizacionWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            RepositorioReloj(applicationContext).sincronizarDiaActual()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        // Llama a esto desde MainActivity para programar la sincronización
        fun programar(context: Context) {
            val peticion = PeriodicWorkRequestBuilder<SincronizacionWorker>(
                30, TimeUnit.MINUTES
            ).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "sync_reloj",
                ExistingPeriodicWorkPolicy.KEEP,
                peticion
            )
        }
    }
}