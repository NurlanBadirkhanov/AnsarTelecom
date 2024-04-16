package com.muslimapk.Dailydua.az.ui.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muslimapk.Dailydua.az.databinding.ItemListBinding

class HomeAdapter(val context: Context) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root)

    val myList: MutableList<Any> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return myList.size
    }
     fun addData(data:List<Any>){
        myList.clear()
        myList.add(data)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        myList[position].let {
            holder.binding.textView.text = it.toString()
        }

    }


}