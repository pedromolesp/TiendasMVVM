package com.cursosant.android.stores.mainModule.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cursosant.android.stores.common.entities.StoreEntity
import com.cursosant.android.stores.mainModule.model.MainInteractor
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private var storeList: MutableList<StoreEntity>
    private var interactor: MainInteractor
    private val stores: LiveData<MutableList<StoreEntity>>

    init {
        storeList = mutableListOf()
        interactor = MainInteractor()
        stores = interactor.stores
    }

    private val showProgress: MutableLiveData<Boolean> = MutableLiveData()

//    private val stores: MutableLiveData<MutableList<StoreEntity>> by lazy {
//        MutableLiveData<MutableList<StoreEntity>>().also {
//            loadStores()
//        }
//    }

    fun showProgress(): LiveData<Boolean> {
        return showProgress
    }

    fun getStores(): LiveData<MutableList<StoreEntity>> {
        return stores
    }

    //    private fun loadStores() {
//        showProgress.value = Constants.SHOW
//        interactor.getStores {
//            showProgress.value = Constants.HIDE
//            stores.value = it
//            storeList = it
//        }
//    }
    fun deleteStore(storeEntity: StoreEntity) {
        interactor.deleteStore(storeEntity) {
            val index = storeList.indexOf(storeEntity)
            if (index != -1) {
                storeList.removeAt(index)
//                stores.value = storeList
            }
        }
    }

    fun updateStore(storeEntity: StoreEntity) {
        viewModelScope.launch {
            storeEntity.isFavorite = !storeEntity.isFavorite
            interactor.updateStore(storeEntity)
        }
    }
}