package com.open.aqrlei.ipc.contentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import com.open.aqrlei.ipc.contentprovider.database.DatabaseOperator

/**
 * @author aqrlei on 2019/4/2
 */
class OrderProvider : ContentProvider() {
    companion object {
        //必须和AndroidManifest.xml中配置的一样，这是ContentProvider的唯一标识
        private const val AUTHORITY = "aqrlei.OrderProvider"

        val ORDER_URL = Uri.parse("content://$AUTHORITY/order")
        private const val ORDER_CODE = 0X01
        private val mMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "order", ORDER_CODE)
        }
    }

    private lateinit var dataBaseOperator: DatabaseOperator

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        dataBaseOperator.insert(false)
        return uri
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        return dataBaseOperator.query()
    }

    override fun onCreate(): Boolean {
        DatabaseOperator.init(context!!)
        dataBaseOperator = DatabaseOperator.getInstance()
        return true
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return dataBaseOperator.update(false)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return dataBaseOperator.delete(false)
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    //自定义的数据访问
    override fun call(method: String, arg: String?, extras: Bundle?): Bundle? {
        return Bundle()
    }

}