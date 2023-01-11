package com.project.notes.database

import androidx.room.*


@Dao
interface NoteDao {


    @Insert
    fun insert(note: Note)

    @Update
    fun update(note:Note)

    @Delete
    fun delete(note:Note)

    @Query("SELECT * FROM note_table")
    fun getAllNote() : MutableList<Note>

    @Query("DELETE FROM note_table")
    fun deleteAllNotes()

}