package com.mykotlinapplication.project2.views.adapters

import android.content.Context
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mykotlinapplication.project2.R
import com.mykotlinapplication.project2.databinding.GalleryItemBinding
import com.mykotlinapplication.project2.models.ListingsProperty
import com.mykotlinapplication.project2.views.activities.TenantActivity

class ListingsListAdapter (var items: ArrayList<ListingsProperty>): RecyclerView.Adapter<ListingsListAdapter.ListingsViewHolder>(){

    private val TAG = "ListingsListAdapter"
    private lateinit var binding: GalleryItemBinding
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListingsViewHolder {
        context = parent.context
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.gallery_item, parent, false)
        return ListingsViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ListingsViewHolder, position: Int) {
        var fullAddress = capitalizeEachWord(items[position].address) + "\n" + capitalizeEachWord(items[position].city) + ", " + items[position].state.toUpperCase() + " " + items[position].postcode
        holder.address.text = fullAddress
        holder.price.text = "$ " + items[position].price
        Glide.with(context).load(items[position].image).into(holder.image)
    }

    fun setData(data: ArrayList<ListingsProperty>) {
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

    inner class ListingsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val image = binding.imageViewHouse
        val price = binding.textViewPrice
        val address = binding.textViewAddress

        private val item = binding.propertyItem.setOnClickListener(this)
        private val tenantActivity = context as TenantActivity

        override fun onClick(v: View) {
            when (v.id) {
                binding.propertyItem.id -> {
                    tenantActivity.viewModel.setSelectedListings(items[adapterPosition])
                    tenantActivity.goToListingsDetail()
                }

            }
        }

    }
}