package com.example.devstreetask

import android.view.View

interface OnItemClickListener {

    fun onUpdateItemClick(position: Int)
    fun onDeleteItemClick(position: Int)

}