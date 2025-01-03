package com.bignerdranch.android.wannago.restaurants

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.WannaGo2.databinding.ListItemCoordinatesBinding
import com.bignerdranch.android.wannago.CoordinatesData

class CoordinatesAdapter(val coordinatesDataList: List<CoordinatesData>): RecyclerView.Adapter<CoordinatesAdapter.CoordinateViewHolder>(){
override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoordinateViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ListItemCoordinatesBinding.inflate(inflater,parent,false)
            return CoordinateViewHolder(binding)
        }

        override fun onBindViewHolder(holder: CoordinateViewHolder, position: Int) {
            holder.bind(coordinatesDataList[position])
        }


        override fun getItemCount(): Int = coordinatesDataList.size

        inner class CoordinateViewHolder(val binding: ListItemCoordinatesBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(coordinatesData: CoordinatesData) {
                binding.coordinateTextView.text = "${coordinatesData.address}\nLatitude:${coordinatesData.latitude}, Longitude:${coordinatesData.longitude}"
            }
        }
}