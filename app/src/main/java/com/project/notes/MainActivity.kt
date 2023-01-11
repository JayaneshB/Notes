package com.project.notes

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.notes.adapter.Note_adapter
import com.project.notes.database.Mydatabase
import com.project.notes.database.Note
import com.project.notes.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


const val REQUEST_CODE = 1
const val EDIT_REQUEST_CODE = 2
const val NOTE_ID: String = "NOTE_ID"
const val TITLE = "TITLE"
const val DESC = "DESC"
const val DATE = "DATE"

class MainActivity : AppCompatActivity(), Note_adapter.noteClickListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: Note_adapter

    private lateinit var list: MutableList<Note>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))


        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

        setData()


        /**
         *  Using startActivityForResult for displaying the result in the same activity
         *  without creating a new activity
         */


        binding.btnAdd.setOnClickListener {

            val intent = Intent(this@MainActivity, AddNew::class.java)
            startActivityForResult(intent, REQUEST_CODE)

        }

    }

    private fun setData() {

        val database = Mydatabase.getInstance(this@MainActivity)
        val data = database!!.noteDao()


        CoroutineScope(Dispatchers.IO).launch {

            list = data.getAllNote()

            withContext(Dispatchers.Main) {
                adapter = Note_adapter(list, this@MainActivity)
                binding.recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()

            }
        }
    }

    /**
     *  Adding the details to be updated
     */

    override fun onItemClick(position: Int) {

        val note: Note = list[position]
        val intent = Intent(this@MainActivity, AddNew::class.java)
        intent.putExtra(NOTE_ID, note.id)
        intent.putExtra(TITLE, note.title)
        intent.putExtra(DESC, note.desc)
        intent.putExtra(DATE, note.date)
        startActivityForResult(intent, EDIT_REQUEST_CODE)
    }


    /**
     *  Retrieving the result using received from the startActivityForResult
     */


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show()
            setData()
        } else if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show()
            setData()
        }

    }

    /**
     *  Creating a menu
     */


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_list, menu)

        return super.onCreateOptionsMenu(menu)
    }

    /**
     *  Functions performed when an item is selected
     */

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.deleteAll -> {

                showDialog()
            }

        }

        return super.onOptionsItemSelected(item)
    }

    /**
     *  Displaying a alert pop box to confirm the selected details
     */


    private fun showDialog() {

        val dialog = AlertDialog.Builder(this@MainActivity)
            .setTitle("Delete")
            .setMessage("Are you sure to delete this ?")
            .setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    deleteAllNotes()
                }
            })
            .setNegativeButton("NO", null).show()

    }

    /**
     *  Function for deleting all the notes created
     */

    private fun deleteAllNotes() {

        val database = Mydatabase.getInstance(this@MainActivity)
        val noteDao = database?.noteDao()

        CoroutineScope(Dispatchers.IO).launch {

            noteDao?.deleteAllNotes()
            withContext(Dispatchers.Main) {

                Toast.makeText(this@MainActivity,"Deleted Successfully",Toast.LENGTH_SHORT).show()
                adapter.clearList()

            }
        }
    }

    /**
     *  Performing longClick operation for
     *  deleting a particular item
     */

    override fun onLongClick(position: Int){

        val note: Note = list[position]
        val dialog = AlertDialog.Builder(this@MainActivity)
            .setTitle("Delete")
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Yes") { dialog: DialogInterface, which: Int ->

            val database = Mydatabase.getInstance(this@MainActivity)
            val data = database!!.noteDao()
            CoroutineScope(Dispatchers.IO).launch {
                data.delete(note)
                list.removeAt(position)
                adapter.notifyItemRemoved(position)
            }
        }
        dialog.setNegativeButton("No", null)
        val alert = dialog.create()
        alert.show()
    }
}

