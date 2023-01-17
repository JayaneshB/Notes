package com.project.notes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.notes.R
import com.project.notes.database.Note

class Note_adapter(var list: MutableList<Note>,
                   private val onClick: noteClickListener) :
    RecyclerView.Adapter<Note_adapter.viewHolder>() {

    class viewHolder(view: View, onclick: noteClickListener) : RecyclerView.ViewHolder(view) {


        init {
            itemView.setOnClickListener {

                onclick.onItemClick(adapterPosition)
            }
        }



        var title: TextView = view.findViewById(R.id.view_title)
        var desc: TextView = view.findViewById(R.id.view_desc)
        var date: TextView = view.findViewById(R.id.view_date)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.view, parent, false)
        return viewHolder(view, onClick)

    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        val note: Note = list[position]
        holder.title.text = note.title
        holder.desc.text = note.desc
        holder.date.text = note.date
        holder.itemView.setOnLongClickListener {
            onClick.onLongClick(position)
//            notifyItemChanged(position)
            true
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {

        return list[position].id.toLong()

    }

    interface noteClickListener {

        fun onItemClick(position: Int)
        fun onLongClick(position: Int)
    }

    fun clearList() {

        list.clear()
        notifyDataSetChanged()
    }

}