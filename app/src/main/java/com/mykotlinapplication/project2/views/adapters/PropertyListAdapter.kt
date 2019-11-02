package com.mykotlinapplication.project2.views.adapters

import android.content.Context
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mykotlinapplication.project2.R
import com.mykotlinapplication.project2.databinding.GalleryItemBinding
import com.mykotlinapplication.project2.models.LandlordProperty
import com.mykotlinapplication.project2.views.activities.LandlordActivity
import java.lang.StringBuilder
import kotlin.random.Random

class PropertyListAdapter(var items: ArrayList<LandlordProperty>): RecyclerView.Adapter<PropertyListAdapter.PropertyViewHolder>(){

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
        Glide.with(context).load(items[position].image).into(holder.image)
        holder.price.text = "$ ${items[position].price}"
        holder.address.text = "${capitalizeEachWord(items[position].address)}\n${capitalizeEachWord(items[position].city)}, ${items[position].state.capitalize()} ${items[position].country}"
    }

    fun setData(data: ArrayList<LandlordProperty>) {
        items = data
        notifyDataSetChanged()
    }

    private fun capitalizeEachWord(string: String): String {
        var inputList = string.split(" ")
        var outputString = ""

        for (e in inputList) {
            outputString += e.capitalize() + " "
        }

        return outputString.trim()
    }


    inner class PropertyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val image = binding.imageViewHouse
        val price = binding.textViewPrice
        val address = binding.textViewAddress

        private val item = binding.propertyItem.setOnClickListener(this)
//        private val itemContextMenu = binding.propertyItem.setOnCreateContextMenuListener(this)
        private val landlordActivity = context as LandlordActivity

        override fun onClick(v: View) {
            when (v.id) {
                binding.propertyItem.id -> {
                    landlordActivity.viewModel.setSelectedProperty(items[adapterPosition])
                    landlordActivity.goToPropertyDetails()
                }

            }
        }
//
//        override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
//            val delete = menu?.add(Menu.NONE, 1, 1, "Delete property")
//            delete?.setOnMenuItemClickListener(this)
//        }
//
//        override fun onMenuItemClick(item: MenuItem): Boolean {
//            when (item.itemId) {
//                1 -> {
//                    landlordActivity.viewModel.setSelectedProperty(items[adapterPosition])
//                    landlordActivity.deleteProperty()
//                }
//            }
//            return true
//        }

    }
}