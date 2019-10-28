package com.mykotlinapplication.project2.views.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mykotlinapplication.project2.R
import com.mykotlinapplication.project2.databinding.GalleryItemBinding
import com.mykotlinapplication.project2.models.Property
import com.mykotlinapplication.project2.views.activities.LandlordActivity

class PropertyListAdapter(var items: ArrayList<Property>): RecyclerView.Adapter<PropertyListAdapter.PropertyViewHolder>(){

    private val TAG = "PropertyListAdapter"
    private lateinit var binding: GalleryItemBinding
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.gallery_item, parent, false)
        context = parent.context

        return PropertyViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {

        holder.price.text = "$ ${items[position].price}"

        holder.address.text = "${items[position].address}\n${items[position].city}, ${items[position].state} ${items[position].country}"


    }

    fun setData(data: ArrayList<Property>) {
        items = data
        notifyDataSetChanged()
    }


    inner class PropertyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val image = binding.imageViewHouse
        val price = binding.textViewPrice
        val address = binding.textViewAddress

        private val item = binding.propertyItem.setOnClickListener(this)
        private val landlordActivity = context as LandlordActivity

        override fun onClick(v: View) {
            when (v.id) {
                binding.propertyItem.id -> landlordActivity.goToPropertyDetails(items[adapterPosition])
            }
        }

    }
}