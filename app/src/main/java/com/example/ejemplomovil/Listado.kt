package com.example.ejemplomovil

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import com.casella.turecuador.db.Backend
import com.casella.turecuador.db.DatosEnviar
import com.casella.turecuador.db.TelefnoEnviar
import com.example.ejemplomovil.DB.DBIn
import com.example.ejemplomovil.backend.DatosUsuario
import com.example.ejemplomovil.databinding.ActivityListadoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL

class Listado : AppCompatActivity() {

    private lateinit var views: ActivityListadoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        views = ActivityListadoBinding.inflate(layoutInflater)
        setContentView(views.root)
        //setSupportActionBar(views.toolbar)
//        cargarCedulaEnTitle()
//        createNotificationChannel()
        crearCanal()
        cargarListenerButtom()

        startService(Intent(this, ServicioAbierto::class.java))
    }

    private fun alHacerClick(){

    }

    fun crearCanal() {
        val name = getString(R.string.app_name)
        val channelId = getString(R.string.user)
        val descriptionText = getString(R.string.dashboard)
        val importance = NotificationManager.IMPORTANCE_MAX

        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }

        val nm: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.createNotificationChannel(channel)
    }

    fun cargarListenerButtom() {



        //val channelId = getString(R.string.user)
//        val notification2 = NotificationCompat.Builder(this, channelId)
//            .setSmallIcon(R.drawable.dashboard)
//            .setContentTitle("Titulo de la notificación 2")
//            .setContentText("Mensaje que se puede poner cuando se muestra la notificación 2")
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT).setStyle(
//                NotificationCompat.BigTextStyle()
//                    .bigText("fasdfladsf aslfj alsdf sldfj asdflkasd flasdfasdfjasd fsadf")
//            )
//            .build()

        views.doNotification.setOnClickListener {
            Backend.api()
                ?.logearse(DatosEnviar.Builder().user("pruebados").clave("pruebaclave").build())
                ?.enqueue(object : Callback<DatosUsuario> {
                    override fun onResponse(
                        call: Call<DatosUsuario>,
                        response: Response<DatosUsuario>
                    ) {
                        if (response.isSuccessful) {
                            val datosUsuario = response.body()
                            datosUsuario?.let { datos ->
                                createNotificationChannel(
                                    datos!!.cedula,
                                    datos!!.email,
                                    datos!!.nombres,
                                    datos!!.urlFoto
                                )
                            }

                        } else {
                            //Toast.makeText(this@Listado, response.message(),Toast.LENGTH_SHORT).show()
                            createNotificationChannel(
                                "fallo con cedula",
                                "fallo con los nombres",
                                "fallo con el email",
                                null
                            )
                        }
                    }

                    override fun onFailure(call: Call<DatosUsuario>, t: Throwable) {
                        Toast.makeText(this@Listado, t.message,Toast.LENGTH_SHORT).show()
                    }

                })


//            with(NotificationManagerCompat.from(this)) {
//                notify(2, notification2)
//            }
        }
        views.doNotificationTwo.setOnClickListener {


//            with(NotificationManagerCompat.from(this)) {
//                notify(2, notification2)
//            }
        }
    }

    val baseDeDatos: DBIn = DBIn(this)

    private fun cargarCedulaEnTitle() {
        val viene = intent.getStringExtra("idpush")
        if (viene == null || viene == "") {
            if (getPreferences(Context.MODE_PRIVATE).contains("token")) {
                val cedula = getSharedPreferences("globales", Context.MODE_PRIVATE).getString(
                    "cedula",
                    resources.getText(R.string.app_name).toString().trim()
                )
                val nombres = getSharedPreferences("globales", Context.MODE_PRIVATE).getString(
                    "nombres",
                    "alguno"
                )
                val email = getSharedPreferences("globales", Context.MODE_PRIVATE).getString(
                    "email",
                    "vacio"
                )
                views.toolbar.title = cedula

                val persona = baseDeDatos.buscarPersona(cedula!!)
                if (persona == null) {
                    baseDeDatos.addPersona(cedula!!, nombres!!, email!!)
                }
            } else {
                views.toolbar.setTitle(R.string.app_name)
            }
        } else {
            views.toolbar.title = viene
            val nm: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.cancelAll()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_listado, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.user -> {
                if (!getPreferences(Context.MODE_PRIVATE).contains("cedula")) {
                    hacerLoginYRegreso()
                } else {
                    getPreferences(Context.MODE_PRIVATE).edit().clear()
                    Toast.makeText(this, R.string.user, Toast.LENGTH_SHORT).show()
                }
                return true
            }
            R.id.settings -> {
                if (!getPreferences(Context.MODE_PRIVATE).contains("cedula")) {
                    val datosTelefono = TelefnoEnviar.Builder().telefono("0983514333").cedula(
                        getPreferences(Context.MODE_PRIVATE).getString("cedula", null)!!
                    ).build()
                    Backend.api()?.agregarTelefono(
                        getPreferences(Context.MODE_PRIVATE).getString(
                            "token",
                            null
                        )!!, datosTelefono
                    )
                } else {
                    Toast.makeText(this, "No está logeado", Toast.LENGTH_LONG).show()
                }
                return true
            }
            R.id.logout -> {
                getPreferences(Context.MODE_PRIVATE).edit().clear().apply()
                getSharedPreferences("globales", Context.MODE_PRIVATE).edit().clear().apply()
                baseDeDatos.borrarBase()
                cargarCedulaEnTitle()
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    val iniciarYVolver =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val token = result.data?.getStringExtra("token")
                getPreferences(Context.MODE_PRIVATE).edit().putString("token", token).apply()
                cargarCedulaEnTitle()
            }
        }

    private fun hacerLoginYRegreso() {
        var intent = Intent(this, Login::class.java)
        intent.putExtra("dato", "información pasada")
        iniciarYVolver.launch(intent)
    }

    private fun generateNotification() {
        val channelId = getString(R.string.user)
        val largeIcon = BitmapFactory.decodeResource(
            resources,
            R.mipmap.ic_launcher
        )
        val conejo = BitmapFactory.decodeResource(
            resources,
            R.mipmap.ic_launcher
        )

        val intent = Intent(this, Listado::class.java)
        intent.putExtra("idpush", "hola")
        val pIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this, channelId)
            .setLargeIcon(largeIcon)
            .setSmallIcon(R.drawable.iconapp)
            .setContentTitle("Titulo de la notificación")
            .setContentText("Mensaje que se puede poner cuando se muestra la notificación")
            .setSubText("uide.edu.ec")
            .setStyle(
                NotificationCompat.BigPictureStyle().bigPicture(largeIcon).bigLargeIcon(conejo)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(R.drawable.dashboard, "Abrir la app", pIntent)
            .addAction(R.drawable.dashboard, "Segundo", pIntent)
            .addAction(R.drawable.dashboard, "Segundo1", pIntent)
            .build()

        with(NotificationManagerCompat.from(this)) {
            notify(1, notification)
        }
    }

    private fun createNotificationChannel(cedula: String?, nombres: String?, email: String?, urlFoto: String?) {
        val ford = BitmapFactory.decodeResource(
            resources,
            R.mipmap.ford
        )
        val ford_raptor = BitmapFactory.decodeResource(
            resources,
            R.mipmap.ford_raptor
        )

        val intent = Intent(this, Login::class.java)
        intent.putExtra("cedula", cedula)
        intent.putExtra("nombres", nombres)
        intent.putExtra("email", email)


        val channelId = getString(R.string.user)
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.dashboard)
            .setContentTitle(cedula)
            .setContentText(nombres)
            .setSubText(email)
            .setStyle(
                NotificationCompat.BigPictureStyle().bigPicture(ford).bigLargeIcon(ford_raptor)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        with(NotificationManagerCompat.from(this)) {
            notify(1, notification)
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {   //(1)
//            val name = getString(R.string.app_name)   // (2)
//            val channelId = getString(R.string.user) // (3)
//            val descriptionText = getString(R.string.dashboard) // (4)
//            val importance = NotificationManager.IMPORTANCE_MAX // (5)
//
//            val channel = NotificationChannel(channelId, name, importance).apply { // (6)
//                description = descriptionText
//            }
//
//            val nm: NotificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager // (7)
//            nm.createNotificationChannel(channel) // (8)
//        }
    }
}