package com.project.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.notes.adapter.Note_adapter
import com.project.notes.database.Mydatabase
import com.project.notes.database.Note
import com.project.notes.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


val REQUEST_CODE=1
val EDIT_REQUEST_CODE=2
val NOTE_ID : String = "NOTE_ID"
val TITLE = "TITLE"
val DESC = "DESC"
val DATE = "DATE"

class MainActivity : AppCompatActivity(),Note_adapter.noteClickListener {

    private lateinit var binding : ActivityMainBinding

    private lateinit var recyleview : RecyclerView

    private lateinit var adapter : Note_adapter

    private lateinit var list : List<Note>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

        setData()


        binding.btnAdd.setOnClickListener {

            val intent = Intent(this@MainActivity,AddNew::class.java)
            startActivityForResult(intent, REQUEST_CODE)

        }

    }

    private fun setData() {

        val database = Mydatabase.getInstance(this@MainActivity)
        val data = database!!.noteDao()


        CoroutineScope(Dispatchers.IO).launch {

            val list:List<Note> = data.getAllNote()
            this@MainActivity.list=list
            withContext(Dispatchers.Main){
                adapter = Note_adapter(list,this@MainActivity )
                binding.recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()

            }
        }
    }

    override fun onItemClick(position: Int) {

        val note : Note = list[position]
        val intent = Intent(this@MainActivity,AddNew::class.java)
        intent.putExtra(NOTE_ID,note.id)
        intent.putExtra(TITLE,note.title)
        intent.putExtra(DESC,note.desc)
        intent.putExtra(DATE,note.date)
        startActivityForResult(intent, EDIT_REQUEST_CODE)


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            Toast.makeText(this,"Note Updated",Toast.LENGTH_SHORT).show()
            setData()
        } else if( requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Toast.makeText(this,"Note Saved",Toast.LENGTH_SHORT).show()
            setData()
        }


    }

}

