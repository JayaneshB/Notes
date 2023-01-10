package com.project.notes.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "note_table")
class Note(
    @ColumnInfo(name = "Id")
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "Title")
    var title: String,
    @ColumnInfo(name = "Description")
    var desc: String,
    @ColumnInfo(name = "Date Created")
    var date: String
) {
}