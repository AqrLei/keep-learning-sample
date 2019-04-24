package com.open.aqrlei.ipc.contentprovider.database

import android.database.sqlite.SQLiteDatabase
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author aqrlei on 2019/2/27
 */
class DatabaseManager private constructor() {
    companion object {
        private var instance: DatabaseManager? = null
        private lateinit var databaseHelper: DatabaseHelper
        fun init(helper: DatabaseHelper) {
            databaseHelper = helper
            instance = DatabaseManager()
        }

        fun getInstance() =
                instance
                        ?: throw IllegalStateException(DatabaseManager::class.java.simpleName + "is not initialized")
    }

    //保证数据库的线程安全
    private val openCounter = AtomicInteger()
    private var database: SQLiteDatabase? = null


    @Synchronized
    fun openDatabase(): SQLiteDatabase? {
        if (openCounter.incrementAndGet() == 1) {
            database = databaseHelper.writableDatabase
        }
        return database
    }

    @Synchronized
    fun closeDatabase() {
        if (openCounter.decrementAndGet() == 0) {
            database?.close()
        }
    }

}