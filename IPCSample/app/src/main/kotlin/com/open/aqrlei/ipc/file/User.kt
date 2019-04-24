package com.open.aqrlei.ipc.file

import java.io.Serializable

/**
 * @author aqrlei on 2019/4/8
 */
data class User(var name: String,
                var time: String) : Serializable {
    companion object {
        private const val serialVersionUID = 7L
    }
}