package com.project.notes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class Mydatabase : RoomDatabase() {

    companion object {

        private var INSTANCE : Mydatabase? = null

        fun getInstance (context: Context) : Mydatabase? {//safe call

            if(INSTANCE == null) {
                synchronized(Mydatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        Mydatabase::class.java,
                        "MyDatabase"
                    ).build()

                    return INSTANCE
                }
            }
            return INSTANCE
        }
    }


    abstract fun noteDao() : NoteDao

}

