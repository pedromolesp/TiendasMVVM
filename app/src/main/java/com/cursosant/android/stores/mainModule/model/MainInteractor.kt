package com.cursosant.android.stores.mainModule.model

import android.util.Log
import com.cursosant.android.stores.StoreApplication
import com.cursosant.android.stores.common.entities.StoreEntity
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainInteractor {
    //https://stores.free.beeceptor.com/api/stores
    /*interface StoreCallback {
        fun getStoresCallback(storesList: MutableList<StoreEntity>)
    }

    fun getStoresCallback(callBack: StoreCallback) {
        doAsync {
            val storesList = StoreApplication.database.storeDao().getAllStores()
            uiThread {
                callBack.getStoresCallback(storesList)
            }
        }
    }*/

    fun getStores(callBack: (MutableList<StoreEntity>) -> Unit) {
        doAsync {
            val storesList = StoreApplication.database.storeDao().getAllStores()
            uiThread {
                val gson = Gson().toJson(storesList)
                Log.i("Gson", gson)
                callBack(storesList)
            }
        }
    }

    fun deleteStore(storeEntity: StoreEntity, callBack: (StoreEntity) -> Unit) {

        doAsync {
             StoreApplication.database.storeDao().deleteStore(storeEntity)
            uiThread {
                callBack(storeEntity)
            }
        }
    }
    fun updateStore(storeEntity: StoreEntity, callback:(StoreEntity)->Unit){
        doAsync {
            StoreApplication.database.storeDao().updateStore(storeEntity)
            uiThread {
                callback(storeEntity)
            }
        }
    }
}