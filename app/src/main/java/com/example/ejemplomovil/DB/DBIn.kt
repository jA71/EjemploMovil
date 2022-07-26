package com.example.ejemplomovil.DB

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DBIn(context: Context) : SQLiteOpenHelper(context, "baseejemplo", null, 1) {


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE persona (cedula TEXT PRIMARY KEY, nombres TEXT, email TEXT)")
    }

    fun borrarBase(){
        this.writableDatabase?.execSQL("DROP TABLE IF EXISTS persona")
        this.writableDatabase?.execSQL("CREATE TABLE persona (cedula TEXT PRIMARY KEY, nombres TEXT, email TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS persona")
        onCreate(db)
    }

    fun addPersona(cedula: String, nombres: String, email: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("cedula", cedula)
        contentValues.put("nombres", nombres)
        contentValues.put("email", email)
        val success = db.insert("persona", null, contentValues)
        db.close()
        return success
    }

    fun buscarPersonas(): List<String> {
        val selectQuery = "SELECT cedula FROM persona"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        val listado: ArrayList<String> = ArrayList()
        if (cursor.moveToFirst()) {
            do {
                val cedula = cursor.getString(cursor.getColumnIndex("cedula"))
                listado.add(cedula)
            } while (cursor.moveToNext())
        }
        return listado
    }

    fun buscarPersona(cedula: String): String? {
        val selectQuery = "SELECT cedula FROM persona WHERE cedula = '$cedula'"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return null
        }
        if (cursor.moveToFirst()) {
            do {
                return cursor.getString(cursor.getColumnIndex("cedula"))
            } while (cursor.moveToNext())
        }
        return null
    }

}