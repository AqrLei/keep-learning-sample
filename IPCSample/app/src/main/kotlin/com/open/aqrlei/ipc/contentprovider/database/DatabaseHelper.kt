package com.open.aqrlei.ipc.contentprovider.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * @author aqrlei on 2019/2/26
 */

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "test.db", null, 1) {


    override fun onCreate(db: SQLiteDatabase?) {
        val sql =
            "create table if not exists Orders(Id integer primary key, CustomName text, OrderPrice integer, Country text)"
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val sql = "drop table if exists Orders"
        db?.execSQL(sql)
        onCreate(db)
    }
}
