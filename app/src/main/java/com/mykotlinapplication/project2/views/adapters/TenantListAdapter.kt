package com.mykotlinapplication.project2.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mykotlinapplication.project2.R
import com.mykotlinapplication.project2.databinding.TenantItemBinding
import com.mykotlinapplication.project2.models.Tenant
import com.mykotlinapplication.project2.views.activities.LandlordActivity

class TenantListAdapter (var items: ArrayList<Tenant>): RecyclerView.Adapter<TenantListAdapter.TenantViewHolder>() {

    private val TAG = "TenantListAdapter"
    private lateinit var binding: TenantItemBinding
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TenantViewHolder {
        context = parent.context
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.tenant_item, parent, false)

        return TenantViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TenantViewHolder, position: Int) {
        holder.name.text = items[position].name
        holder.address.text = items[position].address
    }

    fun setData(data: ArrayList<Tenant>) {
        items = data
        notifyDataSetChanged()
    }


    inner class TenantViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val name = binding.textViewName
        val address = binding.textViewAddress

        private val item = binding.tenantItem.setOnClickListener(this)
        private val landlordActivity = context as LandlordActivity

        override fun onClick(v: View) {
            when (v.id) {
                binding.tenantItem.id -> landlordActivity.goToTenantDetails(items[adapterPosition])
            }
        }

    }
}