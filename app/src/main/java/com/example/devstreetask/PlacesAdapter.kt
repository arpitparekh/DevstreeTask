package com.example.devstreetask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.devstreetask.databinding.ListPlacesBinding
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil

class PlacesAdapter(val list : ArrayList<MyPlaces>,val listener : OnItemClickListener) : RecyclerView.Adapter<PlacesAdapter.PlacesViewHolder>() {

    class PlacesViewHolder(val binding: ListPlacesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesViewHolder {
        val binding = ListPlacesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PlacesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlacesViewHolder, position: Int) {

        val current = list[position]
        holder.binding.obj = current

        val startLatLng = LatLng(current.lat, current.long)
        val endLatLng = LatLng(Extra.myPlaces.lat, Extra.myPlaces.long)
        holder.binding.tvDistance.text = "Distance : "+String.format("%.0f", SphericalUtil.computeDistanceBetween(endLatLng, startLatLng)/1000)+" km"

        Extra.myPlaces = current

        if(position==0){
            holder.binding.btn.visibility = View.VISIBLE
            holder.binding.tvDistance.visibility = View.GONE
        }else{
            holder.binding.btn.visibility = View.GONE
            holder.binding.tvDistance.visibility = View.VISIBLE
        }

        holder.binding.btnEdit.setOnClickListener {
            listener.onUpdateItemClick(position)
        }
        holder.binding.btnDelete.setOnClickListener {
            listener.onDeleteItemClick(position)
        }

    }
    override fun getItemCount() = list.size

}