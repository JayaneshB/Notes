package com.project.notes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.notes.R
import com.project.notes.database.Note

class Note_adapter (var list: List<Note>) : RecyclerView.Adapter<Note_adapter.viewHolder>() {

    class viewHolder(view:View) : RecyclerView.ViewHolder(view)
    {
        var title : TextView = view.findViewById(R.id.view_title)
        var desc : TextView = view.findViewById(R.id.view_desc)
        var date : TextView = view.findViewById(R.id.view_date)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.view,parent,false)
        return viewHolder(view)

    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        val note:Note = list[position]
        holder.title.text = note.title
        holder.desc.text = note.desc
        holder.date.text = note.date
    }

    override fun getItemCount(): Int {
        return list.size
    }
}