package com.example.nike.features.product.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nike.data.Comment
import com.example.nike.databinding.ItemCommentBinding

class CommentAdapter(val showAll:Boolean=false): RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    var comments=ArrayList<Comment>()
        set(value) {
        field=value
            notifyDataSetChanged()
    }

    class ViewHolder(items:ItemCommentBinding):RecyclerView.ViewHolder(items.root){
       val binding=items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCommentBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.comment=comments[position]


    }

    override fun getItemCount(): Int {
        return if (comments.size>3 && !showAll) 3 else comments.size
    }
}