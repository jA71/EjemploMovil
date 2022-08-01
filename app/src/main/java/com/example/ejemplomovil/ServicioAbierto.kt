package com.example.ejemplomovil

import android.app.Service
import android.content.Intent
import android.os.IBinder

class ServicioAbierto : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        System.out.println("Servicio Creado")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        System.out.println("Servicio Inicia")
        while (true){
            println("Servicio Corriendo...")
            Thread.sleep(5000)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        System.out.println("Servicio Destruido")
        super.onDestroy()
    }
}