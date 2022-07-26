package com.example.ejemplomovil.buscador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ejemplomovil.R
import com.example.ejemplomovil.backend.DatosUsuario
import com.example.ejemplomovil.databinding.FragmentoBuscadorBinding
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

class AdaptadorPersonas(private var listado: List<DatosUsuario>) :
    RecyclerView.Adapter<AdaptadorPersonas.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var viewsHolder: FragmentoBuscadorBinding

        init {
            viewsHolder = FragmentoBuscadorBinding.bind(itemView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragmento_buscador, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.viewsHolder.nombres.text = listado.get(position).nombres
        holder.viewsHolder.email.text = listado.get(position).cedula
        Picasso.get().load(listado.get(position).urlFoto).fit().
        transform(CropCircleTransformation()).into(holder.viewsHolder.imagen)
    }

    override fun getItemCount(): Int {
        return listado.size
    }
}