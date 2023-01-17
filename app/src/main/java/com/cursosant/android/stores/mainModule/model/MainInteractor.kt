package com.cursosant.android.stores.mainModule.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.cursosant.android.stores.StoreApplication
import com.cursosant.android.stores.common.entities.StoreEntity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainInteractor {
//    fun getStores(callBack: (MutableList<StoreEntity>) -> Unit) {
//        val url = Constants.STORES_URL + Constants.GET_ALL_STORES_URL
//        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, { response ->
////            val status = response.getInt(Constants.STATUS_PROPERTY)
//            var storeList = mutableListOf<StoreEntity>()
//            val status = response.optInt(Constants.STATUS_PROPERTY, Constants.ERROR)
//            if (status == Constants.SUCCESS) {
//
//                val jsonList =
//                    response.optJSONArray(Constants.STORES_PROPERTY)?.toString()
//                if (jsonList != null) {
//                    val mutableListType = object : TypeToken<MutableList<StoreEntity>>() {}.type
//                    storeList =
//                        Gson().fromJson<MutableList<StoreEntity>>(jsonList, mutableListType)
//                    callBack(storeList)
//                    return@JsonObjectRequest
//                }
//            }
//            callBack(storeList)
//        }, {
//            callBack(mutableListOf())
//
//            it.printStackTrace()
//        })
//        StoreApplication.storeAPI.addToRequestQueue(jsonObjectRequest)
//    }

    //    fun getStoresRoom(callBack: (MutableList<StoreEntity>) -> Unit) {
//        doAsync {
//            val storesList = StoreApplication.database.storeDao().getAllStores()
//            uiThread {
//                val gson = Gson().toJson(storesList)
//                Log.i("Gson", gson)
//                callBack(storesList)
//            }
//        }
//    }
    val stores: LiveData<MutableList<StoreEntity>> = liveData {
        val storesLiveData = StoreApplication.database.storeDao().getAllStores()
        emitSource(storesLiveData)
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