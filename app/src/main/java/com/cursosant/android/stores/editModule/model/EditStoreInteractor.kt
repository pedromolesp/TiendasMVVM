package com.cursosant.android.stores.editModule.model

import com.cursosant.android.stores.StoreApplication
import com.cursosant.android.stores.common.entities.StoreEntity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class EditStoreInteractor {

    fun saveStore(storeEntity: StoreEntity, callBack: (Long) -> Unit) {
        doAsync {
            val newId = StoreApplication.database.storeDao().addStore(storeEntity)
            uiThread {
                callBack(newId)
            }
        }
    }

    fun updateStore(storeEntity: StoreEntity, callBack: (StoreEntity) -> Unit) {
        doAsync {
            StoreApplication.database.storeDao().updateStore(storeEntity!!)
            uiThread {
                callBack(storeEntity)
            }
        }
    }
}