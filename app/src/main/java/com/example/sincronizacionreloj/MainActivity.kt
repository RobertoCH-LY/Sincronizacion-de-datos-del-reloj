package com.example.sincronizacionreloj

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.sincronizacionreloj.ui.theme.SincronizacionRelojTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        SincronizacionWorker.programar(this)
        setContent {
            SincronizacionRelojTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PantallaPrueba(
                        modifier = Modifier.padding(innerPadding),
                        onInsertar = {
                            lifecycleScope.launch {
                                insertarDatosDePrueba()
                            }
                        }
                    )
                }
            }
        }
    }

    private suspend fun insertarDatosDePrueba() {
        val dao = AppDatabase.getInstance(this).relojDao()
        dao.insertarMedicion(MedicionReloj(
            timestamp = System.currentTimeMillis(),
            tipoMedicion = "frecuencia_cardiaca",
            valor = 72.0,
            unidad = "bpm"
        ))
        dao.insertarMedicion(MedicionReloj(
            timestamp = System.currentTimeMillis(),
            tipoMedicion = "pasos",
            valor = 8543.0,
            unidad = "pasos"
        ))
        dao.insertarAcumulado(AcumuladoReloj(
            fecha = "2026-04-18",
            pasosTotales = 8543,
            ritmoPromedio = 72.0,
            distanciaTotal = 5.2,
            caloriasTotal = 320.0
        ))
    }
}

@Composable
fun PantallaPrueba(modifier: Modifier = Modifier, onInsertar: () -> Unit) {
    Column(modifier = modifier) {
        Text(text = "Sincronización Reloj - Prueba")
        Button(onClick = onInsertar) {
            Text("Insertar datos de prueba")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaPruebaPreview() {
    SincronizacionRelojTheme {
        PantallaPrueba(onInsertar = {})
    }
}