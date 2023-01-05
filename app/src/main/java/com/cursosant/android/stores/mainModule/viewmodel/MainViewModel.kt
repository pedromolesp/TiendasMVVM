package com.cursosant.android.stores.mainModule.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cursosant.android.stores.common.entities.StoreEntity
import com.cursosant.android.stores.mainModule.model.MainInteractor

class MainViewModel : ViewModel() {
    private var stores: MutableLiveData<List<StoreEntity>> = MutableLiveData()
    private var interactor: MainInteractor = MainInteractor()

    init {
        loadStores()
    }

    fun getStores(): LiveData<List<StoreEntity>> {
        return stores
    }

    private fun loadStores() {
        interactor.getStoresCallback(object :MainInteractor.StoreCallback{
            override fun getStoresCallback(storesList: MutableList<StoreEntity>) {
               this@MainViewModel.stores.value = storesList

            }

        })

    }
}