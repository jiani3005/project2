package com.mykotlinapplication.project2.models.databases

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mykotlinapplication.project2.MyApplication

@Database(entities = [Image::class], version = 1)
abstract class ImageDatabase: RoomDatabase() {
    abstract fun imageDao(): ImageDao

    companion object {
        var instance: ImageDatabase? = null

        fun getImageDatabaseInstance(): ImageDatabase {
            if (instance == null) {
                synchronized(ImageDatabase::class) {
                    instance = Room.databaseBuilder(MyApplication.context, ImageDatabase::class.java, "avantGardeDb").allowMainThreadQueries().build()
                }
            }
            return instance!!
        }

    }
}