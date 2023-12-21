package com.example.nike.features.list

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.nike.R
import com.example.nike.common.EXTRA_KEY_DATA
import com.example.nike.common.NikeActivity
import com.example.nike.data.Product
import com.example.nike.databinding.ActivityProductListBinding
import com.example.nike.features.product.ProductListAdapter
import com.example.nike.features.product.VIEW_TYPE_LARGE
import com.example.nike.features.product.VIEW_TYPE_SMALL
import com.example.nike.features.product.detail.ProductDetailActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ProductListActivity : NikeActivity() ,ProductListAdapter.OnProductClickListener{
    private lateinit var binding: ActivityProductListBinding
    private val viewModel:ProductListViewModel by viewModel { parametersOf(intent.extras!!.getInt(
        EXTRA_KEY_DATA))  }
    private val productListAdapter:ProductListAdapter by inject { parametersOf(VIEW_TYPE_SMALL)  }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_product_list)


        //grid
        val gridLayoutManager=GridLayoutManager(this,2)
        binding.productsRv.layoutManager=gridLayoutManager
        binding.productsRv.adapter=productListAdapter

        //change viewType and spanCount
         binding.viewTypeChangerBtn.setOnClickListener {
             if (productListAdapter.viewType== VIEW_TYPE_SMALL){
                 binding.viewTypeChangerBtn.setImageResource(R.drawable.ic_view_type_large)
                 productListAdapter.viewType= VIEW_TYPE_LARGE
                 gridLayoutManager.spanCount=1
                 productListAdapter.notifyDataSetChanged()
             }
             else
             {
                 binding.viewTypeChangerBtn.setImageResource(R.drawable.ic_grid)
                 productListAdapter.viewType = VIEW_TYPE_SMALL
                 gridLayoutManager.spanCount = 2
                 productListAdapter.notifyDataSetChanged()
             }
         }


       //product List
        viewModel.productLiveData.observe(this,{
              productListAdapter.products=it as ArrayList<Product>
        })
        //send product to ProductDetail
        productListAdapter.clickListener=this

        //progressbar
        viewModel.progressBarLiveData.observe(this,{
            setProgressIndicator(it)
        })


        // MaterialAlertDialog
        binding.sortBtn.setOnClickListener{
            val dialog=MaterialAlertDialogBuilder(this)
                .setSingleChoiceItems(R.array.sortTitle,viewModel.sort,object :DialogInterface.OnClickListener{
                    override fun onClick(dialogInterface: DialogInterface?, selectedIndexItem: Int) {
                        if (selectedIndexItem==viewModel.sort){
//                            dialogInterface?.cancel()
                        }else{
                            dialogInterface?.dismiss()
                            viewModel.changeProductSortByIndex(selectedIndexItem)

                        }
                    }
                }).setTitle(R.string.sort)
            dialog.show()
        }
        /**
         * we have mutableLiveData<Int> .
         * so we must use getString(it).
         */
        viewModel.sortLiveData.observe(this,{
            binding.selectedSortTitleTv.text=getString(it)
        })

        //onBackClickListener
        binding.toolbarView.onBackBtnToolbar= View.OnClickListener {
            finish()
        }



    }

    //send product_id from here to productDetail
    override fun onProductClick(product: Product) {
        startActivity(Intent(this,ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA,product)
        })
    }
}