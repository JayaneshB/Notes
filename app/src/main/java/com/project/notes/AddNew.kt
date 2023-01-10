package com.project.notes

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import com.project.notes.database.Mydatabase
import com.project.notes.database.Note
import com.project.notes.databinding.ActivityAddNewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class AddNew : AppCompatActivity(),DatePickerDialog.OnDateSetListener {
    private lateinit var binding : ActivityAddNewBinding
    
    private lateinit var calendar : Calendar

    private lateinit var formatter : SimpleDateFormat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        calendar = Calendar.getInstance()
        
        binding.btnCalendar.setOnClickListener { 
            
            DatePickerDialog(
                this@AddNew,
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.btnSave.setOnClickListener {
            
            if(validate()) {
                
                saveNote()
            }

            val intent = Intent(this@AddNew,MainActivity::class.java)
            startActivity(intent)
        }
    }


    private fun saveNote() {

        val database = Mydatabase.getInstance(this)
        val noteDao = database?.noteDao()
        val note = Note(0,binding.titleEdit.text.toString(),binding.descEdit.text.toString(),binding.dateEdit.text.toString())
        GlobalScope.launch {
            noteDao?.insert(note)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@AddNew,"Note Saved",Toast.LENGTH_SHORT).show()
                finish()
            }
        } 
    }

    private fun validate(): Boolean {

         if(binding.titleEdit.text.toString().isEmpty() || binding.descEdit.text.toString().isEmpty() || binding.dateEdit.text.toString().isEmpty()) {

             Toast.makeText(this@AddNew,"Please fill the requirements",Toast.LENGTH_SHORT).show()
             return false
         }
        return true
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, date: Int) {

        calendar.set(year, month, date)
        displayFormattedDate(calendar.timeInMillis)

    }

    private fun displayFormattedDate(data: Long) {

        binding.dateEdit.setText(formatter.format(data))

    }

}