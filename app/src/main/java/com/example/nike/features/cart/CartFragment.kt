package com.example.nike.features.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nike.R
import com.example.nike.common.EXTRA_KEY_DATA
import com.example.nike.common.NikeCompletableObserver
import com.example.nike.common.NikeFragment
import com.example.nike.data.CartItem
import com.example.nike.databinding.FragmentCartBinding
import com.example.nike.features.auth.AuthActivity
import com.example.nike.features.product.detail.ProductDetailActivity
import com.example.nike.services.ImageLoadingService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartFragment:NikeFragment(), CartItemAdapter.CartItemViewCallbacks {
    private lateinit var binding: FragmentCartBinding
    private val viewModel:CartViewModel by viewModel()
    private  var cartItemAdapter: CartItemAdapter?=null
    private val imageLoadingService:ImageLoadingService by inject()
    private val compositeDisposable=CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_cart,container,false)
        return binding.root
    }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.cartItemsLiveData.observe(viewLifecycleOwner,{
          binding.cartItemsRv.layoutManager= LinearLayoutManager(view.context,LinearLayoutManager.VERTICAL,false)
            cartItemAdapter= CartItemAdapter(it as MutableList<CartItem>,imageLoadingService,this)
            binding.cartItemsRv.adapter=cartItemAdapter
        })
        viewModel.progressBarLiveData.observe(viewLifecycleOwner,{
            setProgressIndicator(it)
        })
        viewModel.purchaseDetailLiveData.observe(viewLifecycleOwner,{
          cartItemAdapter?.let { cartItemAdapter ->
              cartItemAdapter.purchaseDetail=it
              //change lase item.It's purchase Detail
              cartItemAdapter.notifyItemChanged(cartItemAdapter.cartItems.size)

          }

        })
       viewModel.emptyStateLiveData.observe(viewLifecycleOwner,{
           if(it.mustShow){
            val emptyState=setEmptyState(R.layout.view_cart_empty_state)
               emptyState?.let {view ->
                   view.findViewById<TextView>(R.id.emptyStateMessageTv).text=getString(it.messageResId)
                   view.findViewById<Button>(R.id.emptyStateCtaBtn).visibility=if (it.showActionButton)
                               View.VISIBLE  else View.INVISIBLE
                   view.findViewById<Button>(R.id.emptyStateCtaBtn).setOnClickListener {
                       startActivity(Intent(requireContext(),AuthActivity::class.java))
                   }
               }
           }else{
               val rootView= rootView?.findViewById<View>(R.id.emptyStateRootView)
               rootView?.visibility=View.GONE

           }

       })
    }

    override fun onStart() {
        super.onStart()
        viewModel.refresh()
    }



    override fun onRemoveCartItemButtonClick(cartItem: CartItem) {
        viewModel.remove(cartItem).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        cartItemAdapter?.removeItem(cartItem)
                    }

                })
    }

    override fun onIncreaseCartItemButtonClick(cartItem: CartItem) {
        viewModel.increaseCartItemCount(cartItem).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        cartItemAdapter?.changeCountItem(cartItem)
                    }

                })
    }

    override fun onDecreaseCartItemButtonClick(cartItem: CartItem) {
        viewModel.decreaseCartItemCount(cartItem).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        cartItemAdapter?.changeCountItem(cartItem)
                    }

                })
    }

    override fun onProductImageClick(cartItem: CartItem) {
        startActivity(Intent(requireContext(),ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA,cartItem.product)
        })
    }
}