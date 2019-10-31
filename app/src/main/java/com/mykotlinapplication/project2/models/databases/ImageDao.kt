package com.mykotlinapplication.project2.models.databases

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ImageDao {

    @Insert
    fun insertImage(image: Image)

    @Query("SELECT * FROM image_table WHERE type = :type")
    fun getImageFromCategory(type: String): List<Image>
}