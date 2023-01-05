package com.cursosant.android.stores.mainModule.model

import com.cursosant.android.stores.StoreApplication
import com.cursosant.android.stores.common.entities.StoreEntity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainInteractor {

    interface StoreCallback{
        fun getStoresCallback(storesList: MutableList<StoreEntity>)
    }

    fun getStoresCallback(callBack: StoreCallback){
        doAsync {
            val storesList = StoreApplication.database.storeDao().getAllStores()
            uiThread {
                callBack.getStoresCallback(storesList)
            }
        }
    }
}