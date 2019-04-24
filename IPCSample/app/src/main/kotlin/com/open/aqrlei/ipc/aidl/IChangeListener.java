package com.open.aqrlei.ipc.aidl;

public interface IChangeListener extends android.os.IInterface {
    /**
     * Local-side IPC implementation stub class.
     */
    public static abstract class Stub extends android.os.Binder implements com.open.aqrlei.ipc.aidl.IChangeListener {
        /**
         * Binder的唯一标识
         */
        private static final java.lang.String DESCRIPTOR = "com.open.aqrlei.ipc.aidl.IChangeListener";

        /**
         * Construct the stub at attach it to the interface.
         */
        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        /**
         * @return
         * 将服务端的Binder对象转换成客户端所需的AIDL接口对象。
         * 如果是同一进程中，此方法返回的是服务端的Stub对象本生
         * 如果是不同进程，返回的是系统封装后的Stub.proxy对象
         */
        public static com.open.aqrlei.ipc.aidl.IChangeListener asInterface(android.os.IBinder obj) {
            if ((obj == null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin != null) && (iin instanceof com.open.aqrlei.ipc.aidl.IChangeListener))) {
                return ((com.open.aqrlei.ipc.aidl.IChangeListener) iin);
            }
            return new com.open.aqrlei.ipc.aidl.IChangeListener.Stub.Proxy(obj);
        }

        /**
         * @return 返回当前的 Binder对象
         */
        @Override
        public android.os.IBinder asBinder() {
            return this;
        }

        /**
         * 运行在服务端的Binder线程池中，当客户端发起跨进程请求时，远程请求通过系统底层封装后交由此方法处理
         * {@link android.os.Binder}
         * @param code  服务端通过code可以确定所请求的方法是什么
         * @param data  从data中取出目标方法所需要的参数(如果需要的话),然后执行目标方法
         * @param reply 在reply中写入返回值(如果需要的话)
         * @return 返回 false ，客户端的请求就会失败
         * */
        @Override
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            java.lang.String descriptor = DESCRIPTOR;
            getCallingPid();
            switch (code) {
                case INTERFACE_TRANSACTION: {
                    reply.writeString(descriptor);
                    return true;
                }
                case TRANSACTION_msgChange: {
                    data.enforceInterface(descriptor);
                    com.open.aqrlei.ipc.Info _arg0;
                    if ((0 != data.readInt())) {
                        _arg0 = com.open.aqrlei.ipc.Info.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    this.msgChange(_arg0);
                    reply.writeNoException();
                    return true;
                }
                default: {
                    return super.onTransact(code, data, reply, flags);
                }
            }
        }

        private static class Proxy implements com.open.aqrlei.ipc.aidl.IChangeListener {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                mRemote = remote;
            }

            @Override
            public android.os.IBinder asBinder() {
                return mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            /**
             * 此方法运行在客户端。首先会创建两个对象，输入型Parcel:"_data",输出型Parcel:"_reply",如果有返回值
             * 会创建一个返回值对象。其次，将方法需要的参数写入_data(如果有参数的话);接着调用transact
             * 发起RPC(远程过程调用)请求同时当前线程挂起，服务端的onTransact会被调用，直到RPC过程返回后，当前线程
             * 继续执行,并从_reply中取出结果，如果有返回值的话，赋值给返回值对象。然后_data,_reply调用recycle
             * 最后如果有返回值的话，返回返回值
             *
             */
            @Override
            public void msgChange(com.open.aqrlei.ipc.Info info) throws android.os.RemoteException {
                //输入型Parcel
                android.os.Parcel _data = android.os.Parcel.obtain();
                //输出型Parcel
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    if ((info != null)) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    mRemote.transact(Stub.TRANSACTION_msgChange, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        static final int TRANSACTION_msgChange = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    }

    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    public void msgChange(com.open.aqrlei.ipc.Info info) throws android.os.RemoteException;
}