package com.mykotlinapplication.project2.models.databases

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_table")
data class Image (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "image_link")
    val imageLink: String

)