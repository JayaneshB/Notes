package com.project.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private lateinit var recyclerView : RecyclerView

    private lateinit var adapter : Note_adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

        setData()


        binding.btnAdd.setOnClickListener {

            val intent = Intent(this,AddNew::class.java)
            startActivity(intent)

        }

    }

    private fun setData() {

        val database = Mydatabase.getInstance(this@MainActivity)
        val data = database!! .noteDao()

        CoroutineScope(Dispatchers.IO).launch {

            val list:List<Note> = data.getAllNote()
            withContext(Dispatchers.Main){
                adapter = Note_adapter(list)
                binding.recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()

            }
        }
    }

}

