package com.open.aqrlei.ipc.contentprovider.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import kotlin.random.Random

/**
 * @author aqrlei on 2019/2/27
 */

class DatabaseOperator private constructor() {
    companion object {
        private var instance: DatabaseOperator? = null
        private lateinit var databaseManager: DatabaseManager
        private var database: SQLiteDatabase? = null
        fun init(context: Context) {
            DatabaseManager.init(DatabaseHelper(context))
            databaseManager = DatabaseManager.getInstance()
            database = databaseManager.openDatabase()
            instance = DatabaseOperator()
        }

        fun getInstance() =
                instance
                        ?: throw IllegalStateException(DatabaseOperator::class.java.simpleName + "is not initialized")
    }

    fun insert(useSQL: Boolean = true) {
        database?.let { db ->
            try {
                clearTable()
                db.beginTransaction()
                if (useSQL) {
                    db.execSQL("insert into Orders(Id,CustomName,OrderPrice,Country) values(1,'LeoArc', 1000, 'US')")
                } else {
                    val values = ContentValues()
                    val id = Random.nextInt()
                    values.apply {
                        put("Id", id)
                        put("CustomName", "ArcLeo")
                        put("OrderPrice", 10000)
                        put("Country", "UK")
                    }
                    db.insertOrThrow("Orders", null, values)
                }
                db.setTransactionSuccessful()
            } catch (e: Exception) {
                Log.d("SQLTest", e.message ?: "UnKnowError")
            } finally {
                db.endTransaction()
            }
        }
    }

    @Throws(Exception::class)
    private fun clearTable() {
        database?.execSQL("delete from Orders")
    }

    fun delete(useSQL: Boolean): Int {

        var result = -1
        database?.let { db ->
            try {
                db.beginTransaction()
                if (useSQL) {
                    db.execSQL("delete from Orders where Id = 1")
                } else {
                    result = db.delete("Orders", "Id=?", arrayOf("5"))
                }
                db.setTransactionSuccessful()
            } catch (e: Exception) {
                Log.d("SQLTest", e.message ?: "UnKnowError")
            } finally {
                db.endTransaction()
            }
        }

        return result
    }

    fun update(useSQL: Boolean): Int {
        var result = -1
        database?.let { db ->
            try {
                db.beginTransaction()
                if (useSQL) {
                    db.execSQL("update Orders set OrderPrice = 5000  where Id = 1")

                } else {
                    val values = ContentValues().apply {
                        put("OrderPrice", 5000)
                    }
                    result = db.update("Orders", values, "Id=?", arrayOf("5"))
                }
                db.setTransactionSuccessful()
            } catch (e: Exception) {
                Log.d("SQLTest", e.message ?: "UnKnowError")
            } finally {
                db.endTransaction()
            }
        }
        return result
    }

    fun query(): Cursor? {
        return database?.query(
                "Orders", arrayOf("Id", "CustomName", "OrderPrice", "Country"),
                null,
                null, null, null, null)
    }

    fun search(useSQL: Boolean): List<Order> {
        val orderList = ArrayList<Order>()
        database?.let { db ->
            try {
                db.beginTransaction()
                if (useSQL) {
                    db.rawQuery(
                            "select Id, CustomName, OrderPrice, Country from Orders where CustomName = ?",
                            arrayOf("LeoArc")
                    )?.use { cursor ->
                        while (cursor.moveToNext()) {
                            orderList.add(
                                    Order(
                                            cursor.getInt(cursor.getColumnIndexOrThrow("Id")),
                                            cursor.getString(cursor.getColumnIndexOrThrow("CustomName")),
                                            cursor.getInt(cursor.getColumnIndexOrThrow("OrderPrice")),
                                            cursor.getString(cursor.getColumnIndexOrThrow("Country"))
                                    )
                            )
                        }
                    }
                } else {
                    db.query(
                            "Orders", arrayOf("Id", "CustomName", "OrderPrice", "Country"),
                            "CustomName = ?",
                            arrayOf("ArcLeo"), null, null, null
                    )?.use { cursor ->
                        while (cursor.moveToNext()) {
                            orderList.add(
                                    Order(
                                            cursor.getInt(cursor.getColumnIndexOrThrow("Id")),
                                            cursor.getString(cursor.getColumnIndexOrThrow("CustomName")),
                                            cursor.getInt(cursor.getColumnIndexOrThrow("OrderPrice")),
                                            cursor.getString(cursor.getColumnIndexOrThrow("Country"))
                                    )
                            )
                        }
                    }
                }
                db.setTransactionSuccessful()
            } catch (e: Exception) {
                Log.d("SQLTest", e.message ?: "UnKnowError")
            } finally {
                db.endTransaction()
            }
        }
        return orderList
    }

    fun close() {
        databaseManager.closeDatabase()
    }
}