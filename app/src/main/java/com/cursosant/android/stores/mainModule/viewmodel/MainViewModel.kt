package com.cursosant.android.stores.mainModule.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cursosant.android.stores.common.entities.StoreEntity
import com.cursosant.android.stores.common.utils.Constants
import com.cursosant.android.stores.mainModule.model.MainInteractor
import kotlinx.coroutines.Job
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
        executeAction {
            interactor.deleteStore(storeEntity)

        }

    }

    fun updateStore(storeEntity: StoreEntity) {
        storeEntity.isFavorite = !storeEntity.isFavorite
        executeAction {
            interactor.updateStore(storeEntity)
        }


    }


    private fun executeAction(block: suspend () -> Unit): Job {
        showProgress.value = Constants.SHOW
        return viewModelScope.launch {
            try {
                block()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                showProgress.value = Constants.HIDE
            }
        }
    }
}