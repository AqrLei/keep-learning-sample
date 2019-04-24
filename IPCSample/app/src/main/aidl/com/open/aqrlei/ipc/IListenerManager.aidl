// IListenerManager.aidl
package com.open.aqrlei.ipc;
import com.open.aqrlei.ipc.IChangeListener;

// Declare any non-default types here with import statements

interface IListenerManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void registerChangeListener(in IChangeListener listener);

    void unregisterChangeListener(in IChangeListener listener);
}
