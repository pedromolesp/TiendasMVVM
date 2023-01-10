package com.cursosant.android.stores.mainModule.model

import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.cursosant.android.stores.StoreApplication
import com.cursosant.android.stores.common.entities.StoreEntity
import com.cursosant.android.stores.common.utils.Constants
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainInteractor {
    fun getStores(callBack: (MutableList<StoreEntity>) -> Unit) {
        val url = Constants.STORES_URL + Constants.GET_ALL_STORES_URL
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            Log.i("response", response.toString())
        }, {
            it.printStackTrace()
        })
        StoreApplication.storeAPI.addToRequestQueue(jsonObjectRequest)
    }

    fun getStoresRoom(callBack: (MutableList<StoreEntity>) -> Unit) {
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

    fun updateStore(storeEntity: StoreEntity, callback: (StoreEntity) -> Unit) {
        doAsync {
            StoreApplication.database.storeDao().updateStore(storeEntity)
            uiThread {
                callback(storeEntity)
            }
        }
    }
}