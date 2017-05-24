package com.example.tse.myapplication

import com.google.gson.Gson
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Type

/**
 * Created by TSE on 24/5/2017.
 */
object JsonReader {

    private val TAG = JsonReader::class.java.simpleName

    @Throws(IOException::class)
    fun <T> readJsonStream(inputStream: InputStream, typeOfT: Type): T {
       val jsonString = inputStream.bufferedReader().use { it.readText() }
        val gson = Gson()
        return gson.fromJson<T>(jsonString, typeOfT)
    }
}