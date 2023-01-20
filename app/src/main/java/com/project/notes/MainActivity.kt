package com.project.notes

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.notes.adapter.Note_adapter
import com.project.notes.database.Mydatabase
import com.project.notes.database.Note
import com.project.notes.databinding.ActivityMainBinding
import com.project.notes.swipegestures.SwipeGestures
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*


const val REQUEST_CODE = 1
const val EDIT_REQUEST_CODE = 2
const val NOTE_ID: String = "NOTE_ID"
const val TITLE = "TITLE"
const val DESC = "DESC"
const val DATE = "DATE"

class MainActivity : AppCompatActivity(), Note_adapter.noteClickListener {
    private var mLastClickTime: Long = 0

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
         *  Adding the swipe gestures
         *  Swipe gesture for deleting a particular view
         */

        val swipeGesture = object : SwipeGestures(this@MainActivity) {

            /**
             *  Drag and Drop gesture is implemented
             */

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {

                val fromPosition = viewHolder.absoluteAdapterPosition
                val toPosition = target.absoluteAdapterPosition

                Collections.swap(list, fromPosition, toPosition)
                adapter.notifyItemMoved(fromPosition, toPosition)
                return false

            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        // need to delete the actual record from the db
                        val dialog = AlertDialog.Builder(this@MainActivity)
                            .setTitle(resources.getString(R.string.delete))
                            .setMessage(resources.getString(R.string.confirm_message))
                            .setIcon(R.drawable.ic_delete)
                            .setPositiveButton(resources.getString(R.string.yes)) { dialog: DialogInterface, which: Int ->
                                try {
                                    val database = Mydatabase.getInstance(this@MainActivity)
                                    val note = database?.noteDao()
                                    val position = viewHolder.absoluteAdapterPosition
                                    CoroutineScope(Dispatchers.IO).launch {
                                        note?.delete(list[position])
                                        list.removeAt(position)
                                    }
                                    adapter.notifyDataSetChanged()
                                } catch (e: Exception) {

                                }
                            }
                            .setNegativeButton(resources.getString(R.string.no)) { dialog: DialogInterface, which: Int ->
                                val position = viewHolder.absoluteAdapterPosition
                                dialog.cancel()
                                adapter.notifyItemChanged(position)
                            }
                        val alert = dialog.create()
                        alert.show()
                    }
                }
            }
        }
        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(binding.recyclerView)

        /**
         *  Using startActivityForResult for displaying the result in the same activity
         *  without creating a new activity
         */

        binding.btnAdd.setOnClickListener {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            val intent = Intent(this@MainActivity, AddNew::class.java)
            startActivityForResult(intent, REQUEST_CODE)
//            val activityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), { result ->
//                if (result.resultCode == Activity.RESULT_OK) {
//                    setData()
//                    Toast.makeText(this, resources.getString(R.string.save_note), Toast.LENGTH_SHORT).show()
//                }
//            })
//            activityResult.launch(intent)
//        }
        }
    }

    /**
     *  Creating a new data to the database
     */

    private fun setData() {

        val database = Mydatabase.getInstance(this@MainActivity)
        if (database != null) {
            val data = database.noteDao()
            CoroutineScope(Dispatchers.IO).launch {
                list = data.getAllNote()
                withContext(Dispatchers.Main) {
                    adapter = Note_adapter(list, this@MainActivity)
                    binding.recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()
                    //     adapter.notifyItemChanged(-1)
                }
            }
        }
    }

    /**
     *  Adding the details to be updated
     */

    override fun onItemClick(position: Int) {

        val note: Note = list[position]

        /**
         *  Can use apply{} instead of initialising a variable
         */

        val intent = Intent(this@MainActivity, AddNew::class.java)
        intent.putExtra(NOTE_ID, note.id)
        intent.putExtra(TITLE, note.title)
        intent.putExtra(DESC, note.desc)
        intent.putExtra(DATE, note.date)
        intent.putExtra("position", position)
        startActivityForResult(intent, EDIT_REQUEST_CODE)
//        getResult.launch(intent)

    }

    /**
     *  Retrieving the result using received from the startActivityForResult
     */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            val position = data?.getIntExtra("position", -1)
            position?.let {
                if (it != -1) {
                    adapter.notifyItemChanged(it)
                }
            }
            Toast.makeText(this, resources.getString(R.string.update_note), Toast.LENGTH_SHORT)
                .show()
            setData()
        } else if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Toast.makeText(this, resources.getString(R.string.save_note), Toast.LENGTH_SHORT).show()
            setData()
        }
    }

//    val getResult= registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result: ActivityResult ->
//
//        if(result.requestCode  == EDIT_REQUEST_CODE && result.resultCode == RESULT_OK){
//
//            Toast.makeText(this, resources.getString(R.string.update_note), Toast.LENGTH_SHORT).show()
//            setData()
//        }else if(result.requestCode == EDIT_REQUEST_CODE && result.resultCode== RESULT_OK){
//            Toast.makeText(this, resources.getString(R.string.save_note), Toast.LENGTH_SHORT).show()
//            setData()
//
//        }

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

        AlertDialog.Builder(this@MainActivity)
            .setTitle(resources.getString(R.string.delete))
            .setMessage(resources.getString(R.string.confirm_message))
            .setPositiveButton(
                resources.getString(R.string.yes),
                object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        deleteAllNotes()
                    }
                })
            .setNegativeButton(resources.getString(R.string.no), null).show()
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
                Toast.makeText(
                    this@MainActivity,
                    resources.getString(R.string.deletedSuccessfully),
                    Toast.LENGTH_SHORT
                ).show()
                adapter.clearList()
            }
        }
    }

    /**
     *  Performing longClick operation for
     *  deleting a particular item
     */

    override fun onLongClick(position: Int) {

        val note: Note = list[position]
        val dialog = AlertDialog.Builder(this@MainActivity)
            .setTitle(resources.getString(R.string.delete))
            .setMessage(resources.getString((R.string.confirm_message)))
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton(resources.getString(R.string.yes)) { dialog: DialogInterface, which: Int ->

                val database = Mydatabase.getInstance(this@MainActivity)
                val data = database!!.noteDao()
                CoroutineScope(Dispatchers.IO).launch {
                    data.delete(note)
                    list.removeAt(position)
                }
                adapter.notifyDataSetChanged()
            }
        dialog.setNegativeButton(resources.getString(R.string.no), null)
        val alert = dialog.create()
        alert.show()
    }
}
