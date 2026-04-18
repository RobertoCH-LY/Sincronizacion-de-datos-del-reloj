# Sincronización de datos del reloj

Módulo Android en Kotlin que sincroniza datos de actividad física desde Health Connect y los guarda localmente con Room.

---

## Ficheros

| Fichero | Descripción |
|---|---|
| `MedicionReloj.kt` | Entidad que representa una medición individual con timestamp, valor y unidad |
| `AcumuladoReloj.kt` | Entidad que representa el resumen diario: pasos, ritmo, distancia y calorías |
| `AppDatabase.kt` | Instancia singleton de la base de datos Room (`saludconecta.db`) |
| `RelojDao.kt` | Interfaz con las operaciones de base de datos: insertar y consultar mediciones |
| `RelojRepository.kt` | Lee de Health Connect y guarda en Room. Incluye `insertarDatosDePrueba()` |
| `SincronizacionWorker.kt` | Worker que se ejecuta cada 30 minutos y llama al repositorio |
| `MainActivity.kt` | Registra el Worker al abrir la app. Incluye botón de prueba |

## Tecnologías

- **Room** `2.6.1` — persistencia local SQLite
- **WorkManager** — sincronización en segundo plano
- **Health Connect** — lectura de datos del reloj
- **KSP** `2.0.21` — procesador de anotaciones para Room
- **Kotlin Coroutines** — operaciones asíncronas

## Permisos necesarios
`READ_STEPS` · `READ_HEART_RATE` · `READ_DISTANCE` · `READ_TOTAL_CALORIES_BURNED`

---

## Sin reloj

1. Pulsar el botón **"Insertar datos de prueba"** en la app
2. Ir a Android Studio → **App Inspection** → **Database Inspector**
3. Abrir `saludconecta.db`
4. Verificar que las tablas `mediciones_reloj` y `acumulados_reloj` tienen datos

## Con reloj

1. Instalar la app **Health Connect** en el dispositivo
2. Conceder los permisos de pasos, frecuencia cardíaca, distancia y calorías
3. Asegurarse de que el reloj está sincronizado con Health Connect
4. Al abrir la app, el Worker se registra y sincroniza automáticamente cada 30 minutos
5. Verificar igual que en el caso sin reloj, activando **Live updates** en el Database Inspector

---

## Licencia

Este proyecto utiliza la licencia MIT. El código es libre y se puede usar, modificar o distribuir sin restricciones, siempre que se mantenga el aviso de copyright original.

