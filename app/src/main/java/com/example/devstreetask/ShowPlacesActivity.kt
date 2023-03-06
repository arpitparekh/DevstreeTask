package com.example.devstreetask

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.devstreetask.databinding.ActivityShowPlacesBinding

class ShowPlacesActivity : AppCompatActivity(), OnItemClickListener {
    lateinit var binding : ActivityShowPlacesBinding
    var list = ArrayList<MyPlaces>()
    lateinit var dao: PlacesDao
    lateinit var adapter : PlacesAdapter

    override fun onResume() {
        super.onResume()

        if(list.isEmpty()){
            binding.frame.visibility = View.VISIBLE
            binding.tvRoutes.visibility = View.GONE
        }else{
            binding.frame.visibility = View.GONE
            binding.tvRoutes.visibility = View.VISIBLE
        }

        if(intent!=null){

            val oobj = intent.getSerializableExtra("obj") as MyPlaces?

            if(oobj!=null){

                dao.updatePlace(oobj)
                adapter = PlacesAdapter(list,this)
                binding.rvPlaces.adapter = adapter
            }

        }

        adapter = PlacesAdapter(list,this)
        binding.rvPlaces.adapter = adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowPlacesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.rvPlaces.layoutManager = LinearLayoutManager(this)

        dao = PlacesDatabase.getInstance(this)?.getPlacesDao()!!


        if(intent!=null){

            val oobj = intent.getSerializableExtra("obj") as MyPlaces?

            if(oobj!=null){

                dao.updatePlace(oobj)
                adapter = PlacesAdapter(list,this)
                binding.rvPlaces.adapter = adapter
            }

        }

        list = dao.showAllPlaces() as ArrayList<MyPlaces>

        adapter = PlacesAdapter(list,this)
        binding.rvPlaces.adapter = adapter

        if(list.isEmpty()){
            binding.frame.visibility = View.VISIBLE
            binding.tvRoutes.visibility = View.GONE
        }

        binding.tvRoutes.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("list",list)
            startActivity(intent)
        }

        binding.btnAddPlace.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

    }

    override fun onUpdateItemClick(position: Int) {
        val intent = Intent(this,MainActivity::class.java)
        intent.putExtra("data",list[position])
        startActivity(intent)
    }

    override fun onDeleteItemClick(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Do you Want to Delete this?")
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                dao.deletePlace(list[position])
                list = dao.showAllPlaces() as ArrayList<MyPlaces>
                adapter = PlacesAdapter(list,this)
                binding.rvPlaces.adapter = adapter

            }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->

            }).create().show()
    }

}