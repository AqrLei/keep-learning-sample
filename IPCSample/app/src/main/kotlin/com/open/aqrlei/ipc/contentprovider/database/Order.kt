package com.open.aqrlei.ipc.contentprovider.database

/**
 * @author aqrlei on 2019/2/27
 */
data class Order(
    var id: Int,
    var customName: String,
    var orderPrice: Int,
    var country: String
)