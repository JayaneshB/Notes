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
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class AddNew : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var binding: ActivityAddNewBinding

    private lateinit var calendar: Calendar

    private lateinit var formatter: SimpleDateFormat

    private lateinit var title: String
    private lateinit var desc: String
    private lateinit var date: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        calendar = Calendar.getInstance()

        /**
         *  Added a calendar image button
         *  to select the date for the aplication
         */

        formatter = SimpleDateFormat("dd/MM/yyyy", Locale.US)

        binding.btnCalendar.setOnClickListener {

            DatePickerDialog(
                this@AddNew,
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        if (intent.hasExtra(NOTE_ID)) {

            title = intent.getStringExtra(TITLE).toString()
            desc = intent.getStringExtra(DESC).toString()
            date = intent.getStringExtra(DATE).toString()


            binding.titleEdit.setText(title)
            binding.descEdit.setText(desc)
            binding.dateEdit.setText(date)


        }

        /**
         *  Save button operation for both
         *  update and save option
         */


        binding.btnSave.setOnClickListener {

            if (validate()) {
                if (intent.hasExtra(NOTE_ID)) {

                    updateNote()

                } else {

                    saveNote()

                }
            }
        }
    }

    private fun updateNote() {

        val id = intent.getIntExtra(NOTE_ID, -1)


        val database = Mydatabase.getInstance(this)
        val noteDao = database?.noteDao()

        CoroutineScope(Dispatchers.IO).launch {

            val note = Note(
                id,
                binding.titleEdit.text.toString(),
                binding.descEdit.text.toString(),
                binding.dateEdit.text.toString()
            )
            noteDao?.update(note)
            withContext(Dispatchers.Main) {
                val intent = Intent(this@AddNew, MainActivity::class.java)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }


    private fun saveNote() {

        val database = Mydatabase.getInstance(this)
        val noteDao = database?.noteDao()
        val note = Note(
            0,
            binding.titleEdit.text.toString(),
            binding.descEdit.text.toString(),
            binding.dateEdit.text.toString()
        )
        GlobalScope.launch {
            noteDao?.insert(note)
            withContext(Dispatchers.Main) {
                val intent = Intent(this@AddNew, MainActivity::class.java)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun validate(): Boolean {

        if (binding.titleEdit.text.toString().isEmpty() || binding.descEdit.text.toString()
                .isEmpty() || binding.dateEdit.text.toString().isEmpty()
        ) {

            Toast.makeText(this@AddNew, "Please fill the requirements", Toast.LENGTH_SHORT).show()
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