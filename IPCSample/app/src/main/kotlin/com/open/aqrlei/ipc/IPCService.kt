package com.open.aqrlei.ipc

import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import android.util.Log
import com.open.aqrlei.ipc.contentprovider.OrderProvider
import com.open.aqrlei.ipc.file.FileStreamUtil
import com.open.aqrlei.ipc.file.User
import kotlinx.coroutines.Job
import java.io.*
import java.net.ServerSocket
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

/**
 * @author  aqrLei on 2018/7/20
 */
class IPCService : Service() {
    companion object {
        const val MESSENGER_BINDER_CODE = 0
        const val AIDL_BINDER_CODE = 1
        const val RECEIVE_FROM_CLIENT_CODE_INIT = 2
        const val RECEIVE_FROM_CLIENT_CODE_NORMAL = 21
        const val RECEIVE_FROM_CLIENT_CODE_FILE = 31
    }

    private var client: Messenger? = null
    private var job: Job? = null
    private var serviceMessengerHandler: Handler? = null

    private val mIBinderPool = object : IBinderPool.Stub() {
        @Throws(RemoteException::class)
        override fun queryBinder(binderCode: Int): IBinder {
            return when (binderCode) {
                MESSENGER_BINDER_CODE -> { // Binder 连接池，选用Messenger

                    // 运行在Binder线程池中
                    // 此处必须加上Looper.getMainLooper()
                    serviceMessengerHandler = object : Handler(Looper.getMainLooper()) {
                        override fun handleMessage(msg: Message?) {
                            when (msg?.what) {
                                RECEIVE_FROM_CLIENT_CODE_INIT -> { //初次信息传递

                                    if (client != msg.replyTo) {// 获取客户端返回的Messenger，用于之后两端通信
                                        client = msg.replyTo
                                    }
                                    client?.let {
                                        //通知客户端，首次消息接收成功
                                        sendMsgInit(it)
                                    }
                                }
                                RECEIVE_FROM_CLIENT_CODE_NORMAL -> {
                                    client?.let { sendContentProviderMsg(it) }
                                }
                                RECEIVE_FROM_CLIENT_CODE_FILE -> {
                                    FileStreamUtil.getCacheFile(this@IPCService)?.let { file ->
                                        FileStreamUtil.readChar(file) {
                                            Log.d("IPC_FILE", it)
                                        }
                                        FileStreamUtil.readObject(FileStreamUtil.getObjectFile(this@IPCService)) {
                                            if (it != null) {
                                                Log.d("IPC_FILE", "User: name:${it.name}-time:${it.time}")
                                            }
                                        }
                                        val time = SimpleDateFormat("hh:mm:ss.SSS", Locale.ROOT).format(System.currentTimeMillis())
                                        val str = "write by service-$time"
                                        FileStreamUtil.writeChar(file, str)
                                        val objectFile = FileStreamUtil.getObjectFile(this@IPCService)
                                        FileStreamUtil.writeObject(objectFile, User("service", time))
                                        client?.let {
                                            notifyFileWrite(it)
                                        }

                                    }

                                }
                                else -> {
                                    super.handleMessage(msg)
                                }
                            }
                        }
                    }
                    //返回binder给客户端
                    Messenger(serviceMessengerHandler).binder
                }
                else -> {
                    mListenerManager
                }
            }
        }
    }
    private val mListener = object:IChangeListener.Stub(){
        override fun msgChange(info: Info?) {

        }
    }

    private val listenerList = RemoteCallbackList<IChangeListener>()
    private val mListenerManager = object : IListenerManager.Stub() {
        override fun registerChangeListener(listener: IChangeListener?) {
            listenerList.register(listener)
            sendMsgChange(Info("receive from service", 0))
        }

        override fun unregisterChangeListener(listener: IChangeListener?) {
            listenerList.unregister(listener)
        }
    }

    fun sendMsgChange(info: Info) {

        //必须先调用这个方法
        val n = listenerList.beginBroadcast()
        for (i in 0 until n) {
            listenerList.getBroadcastItem(0).msgChange(info)
        }
        listenerList.finishBroadcast()
    }

    private var serviceDestroyed = false
    override fun onCreate() {
        Thread(TcpServer()).start()
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        val check = checkCallingOrSelfPermission("com.aqrlei.permission.PROVIDER")
        return if (check == PackageManager.PERMISSION_DENIED) {
            null
        } else {
            mIBinderPool
        }


    }

    override fun onUnbind(intent: Intent?): Boolean {
        stopSelf()
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        clear()
        serviceDestroyed = true
        super.onDestroy()
    }

    private fun clear() {
        serviceMessengerHandler = null
        job = null
        client = null
    }

    fun sendMsgInit(client: Messenger) {
        client.send(Message.obtain(null, IPCActivity.RECEIVE_FROM_SERVICE_CODE_INIT))
    }

    fun notifyFileWrite(client: Messenger) {
        client.send(Message.obtain(null, IPCActivity.RECEIVE_FROM_SERVICE_CODE_FILE))
    }

    fun sendContentProviderMsg(client: Messenger) {
        val sb = StringBuilder()
        try {
            contentResolver.query(OrderProvider.ORDER_URL, null, null, null, null)?.use { cursor ->
                while (cursor.moveToNext()) {
                    sb.append("Id: ${cursor.getInt(cursor.getColumnIndexOrThrow("Id"))} \n")
                    sb.append("CustomName: ${cursor.getString(cursor.getColumnIndexOrThrow("CustomName"))}\n")
                    sb.append("OrderPrice: ${cursor.getInt(cursor.getColumnIndexOrThrow("OrderPrice"))}\n")
                    sb.append("Country: ${cursor.getString(cursor.getColumnIndexOrThrow("Country"))}\n")
                    sb.append("----------\n")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            sb.append(e.message ?: "UnknownError")
        }
        if (sb.isEmpty()) {
            sb.append("no content")
        }

        client.send(
                Message.obtain(
                        null,
                        IPCActivity.RECEIVE_FROM_SERVICE_CODE_NORMAL)
                        .apply {
                            data = Bundle().also {
                                it.putString(
                                        IPCActivity.RECEIVE_FROM_SERVICE_DATA,
                                        sb.toString())
                            }
                        })
    }

    private inner class TcpServer : Runnable {
        private var client: Socket? = null
        override fun run() {
            val serverSocket: ServerSocket
            try {
                serverSocket = ServerSocket(9999)

            } catch (e: IOException) {
                e.printStackTrace()
                return
            }
            while (!serviceDestroyed) {
                try {
                    Log.d("Socket", "TcpServer Run")
                    client = serverSocket.accept()
                    thread {
                        try {
                            responseClient(client!!)
                        } catch (e: IOException) {
                            e.printStackTrace()
                            Log.d("Socket", "TcpServer Thread Run onError ${e.message}")
                        }

                    }
                } catch (e: IOException) {
                    Log.d("Socket", "TcpServer Run OnError ${e.message}")
                    e.printStackTrace()
                }
            }
            client?.close()
            Log.d("Socket", "TcpServer Client close $serviceDestroyed")
        }

        @Throws(IOException::class)
        private fun responseClient(client: Socket) {
            val inReader: BufferedReader? = BufferedReader(InputStreamReader(client.getInputStream()))
            val outWriter: PrintWriter? = PrintWriter(BufferedWriter(OutputStreamWriter(client.getOutputStream())), true)
            Log.d("Socket", "TcpServer response $serviceDestroyed")
            while (!serviceDestroyed) {
                Log.d("Socket", "TcpServer response before $serviceDestroyed")
                val str = inReader?.readLine()
                if (str != null) {
                    Log.d("Socket", "TcpServer response $str")
                    val time = SimpleDateFormat("hh:mm:ss.SSS", Locale.ROOT).format(System.currentTimeMillis())
                    outWriter?.println("通过Socket回传：$str-$time")
                    sendMsgChange(Info("AIDL回传：$str-$time", -1))
                    /*outWriter?.close()
                    inReader?.close()*/
                }
            }
            Log.d("Socket", "TcpServer response after $serviceDestroyed")
        }
    }
}