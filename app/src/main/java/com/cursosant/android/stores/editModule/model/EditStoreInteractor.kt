package com.cursosant.android.stores.editModule.model

import androidx.lifecycle.LiveData
import com.cursosant.android.stores.StoreApplication
import com.cursosant.android.stores.common.entities.StoreEntity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class EditStoreInteractor {

    fun getStoreById(id:Long):LiveData<StoreEntity>{
        return StoreApplication.database.storeDao().getStoreById(id)
    }
    fun saveStore(storeEntity: StoreEntity, callBack: (Long) -> Unit) {
        doAsync {
            val newId = StoreApplication.database.storeDao().addStore(storeEntity)
            uiThread {
                callBack(newId)
            }
        }
    }

    fun updateStore(storeEntity: StoreEntity) {

    }
}