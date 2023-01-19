package com.cursosant.android.stores.mainModule

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.cursosant.android.stores.R
import com.cursosant.android.stores.common.entities.StoreEntity
import com.cursosant.android.stores.common.utils.TypeError
import com.cursosant.android.stores.databinding.ActivityMainBinding
import com.cursosant.android.stores.editModule.EditStoreFragment
import com.cursosant.android.stores.editModule.viewModel.EditStoreViewModel
import com.cursosant.android.stores.mainModule.adapter.OnClickListener
import com.cursosant.android.stores.mainModule.adapter.StoreListAdapter
import com.cursosant.android.stores.mainModule.viewmodel.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), OnClickListener {

    private lateinit var mBinding: ActivityMainBinding

    private lateinit var mAdapter: StoreListAdapter
    private lateinit var mGridLayout: GridLayoutManager

    //MVVM
    private lateinit var mMainViewModel: MainViewModel
    private lateinit var mEditSoreViewModel: EditStoreViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.fab.setOnClickListener { launchEditFragment() }

        setupRecylcerView()
        setupViewModel()
    }

    private fun setupViewModel() {
        mMainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mMainViewModel.getStores().observe(this, { stores ->
            mBinding.progressBar.visibility = View.GONE
            mAdapter.submitList(stores)
        })

        mMainViewModel.showProgress().observe(this) { isShowProgress ->
            mBinding.progressBar.visibility = if (isShowProgress) View.VISIBLE else View.GONE
        }

        mMainViewModel.getTypeError().observe(this) { typeError ->
            val msgRes = when (typeError) {
                TypeError.GET -> "Error al consultar datos"
                TypeError.DELETE -> "Error al eliminar datos"
                TypeError.INSERT -> "Error al insertar datos"
                TypeError.UPDATE -> "Error al actualizar datos"
                else -> "Error desconocido"
            }
            Snackbar.make(mBinding.root, msgRes, Snackbar.LENGTH_SHORT).show()
        }
        mEditSoreViewModel = ViewModelProvider(this).get(EditStoreViewModel::class.java)
        mEditSoreViewModel.getShowFav().observe(this, { isVisible ->
            if (isVisible) mBinding.fab.show() else mBinding.fab.hide()
        })

    }

    private fun launchEditFragment(storeEntity: StoreEntity = StoreEntity()) {
        mEditSoreViewModel.setShowFab(false)
        mEditSoreViewModel.setStoreSelected(storeEntity)
        val fragment = EditStoreFragment()
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.add(R.id.containerMain, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun setupRecylcerView() {
        mAdapter = StoreListAdapter(this)
        mGridLayout = GridLayoutManager(this, resources.getInteger(R.integer.main_columns))
//        getStores()

        mBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = mGridLayout
            adapter = mAdapter
        }
    }

    private fun getStores() {
        /* doAsync {
             val stores = StoreApplication.database.storeDao().getAllStores()
             uiThread {
                 mAdapter.setStores(stores)
             }
         }*/
    }

    /*
    * OnClickListener
    * */
    override fun onClick(storeEntity: StoreEntity) {
        launchEditFragment(storeEntity)
    }

    override fun onFavoriteStore(storeEntity: StoreEntity) {
        storeEntity.isFavorite = !storeEntity.isFavorite
        mMainViewModel.updateStore(storeEntity)
    }

    override fun onDeleteStore(storeEntity: StoreEntity) {
        val items = resources.getStringArray(R.array.array_options_item)

        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_options_title)
            .setItems(items) { dialogInterface, i ->
                when (i) {
                    0 -> confirmDelete(storeEntity)

                    1 -> dial(storeEntity.phone)

                    2 -> goToWebsite(storeEntity.website)
                }
            }
            .show()
    }

    private fun confirmDelete(storeEntity: StoreEntity) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_delete_title)
            .setPositiveButton(R.string.dialog_delete_confirm) { _, _ ->
                mMainViewModel.deleteStore(storeEntity)
            }
            .setNegativeButton(R.string.dialog_delete_cancel, null)
            .show()
    }

    private fun dial(phone: String) {
        val callIntent = Intent().apply {
            action = Intent.ACTION_DIAL
            data = Uri.parse("tel:$phone")
        }

        startIntent(callIntent)
    }

    private fun goToWebsite(website: String) {
        if (website.isEmpty()) {
            Toast.makeText(this, R.string.main_error_no_website, Toast.LENGTH_LONG).show()
        } else {
            val websiteIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(website)
            }

            startIntent(websiteIntent)
        }
    }

    private fun startIntent(intent: Intent) {
        if (intent.resolveActivity(packageManager) != null)
            startActivity(intent)
        else
            Toast.makeText(this, R.string.main_error_no_resolve, Toast.LENGTH_LONG).show()
    }


}