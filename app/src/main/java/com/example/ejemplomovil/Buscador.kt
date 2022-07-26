package com.example.ejemplomovil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.casella.turecuador.db.Backend
import com.casella.turecuador.db.DatosEnviar
import com.example.ejemplomovil.backend.DatosUsuario
import com.example.ejemplomovil.buscador.AdaptadorPersonas
import com.example.ejemplomovil.databinding.ActivityBuscadorBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Buscador : AppCompatActivity() {

    private lateinit var views: ActivityBuscadorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        views = ActivityBuscadorBinding.inflate(layoutInflater)
        setContentView(views.root)
        setSupportActionBar(views.toolbar)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_listado, menu)
        habilitarBuscador(menu.findItem(R.id.search))
        return true
    }


    private fun habilitarBuscador(item: MenuItem) {
        val finder = item.actionView as SearchView
        finder.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                buscando(query.toString().trim())
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                buscando(newText.toString().trim())
                return false
            }
        })
    }

    private fun buscando(texto: String) {
        texto?.let { textoReal ->
            if (textoReal.length >= 3) {
                // var datos = DatosEnviar()
                // datos.usuario = textoReal
                Backend.api()?.buscarPersona(DatosEnviar.Builder().user(textoReal).build())
                    ?.enqueue(object : Callback<List<DatosUsuario>> {
                        override fun onResponse(
                            call: Call<List<DatosUsuario>>,
                            response: Response<List<DatosUsuario>>
                        ) {
                            if (response.isSuccessful) {
                                response.body()?.let { listado ->
                                    llenarfragmentos(listado)
                                }
                            } else {
                                views.listador.adapter = null
                            }
                        }

                        override fun onFailure(call: Call<List<DatosUsuario>>, t: Throwable) {

                        }
                    })
            } else {
                views.listador.adapter = null
            }
        }
    }

    private fun llenarfragmentos(lista: List<DatosUsuario>) {
        views.listador.layoutManager = LinearLayoutManager(this)
        views.listador.adapter = AdaptadorPersonas(lista)
    }
}