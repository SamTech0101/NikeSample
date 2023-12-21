package com.example.nike.features.cart

import androidx.lifecycle.MutableLiveData
import com.example.nike.R
import com.example.nike.common.NikeSingleObserver
import com.example.nike.common.NikeView
import com.example.nike.common.asyncNetworkRequest
import com.example.nike.data.*
import com.example.nike.data.repository.CartRepository
import io.reactivex.Completable

class CartViewModel(private val cartRepository: CartRepository): NikeView.NikeViewModel() {
    val cartItemsLiveData = MutableLiveData<List<CartItem>>()
    val purchaseDetailLiveData = MutableLiveData<PurchaseDetail>()
    val emptyStateLiveData=MutableLiveData<EmptyState>()

    fun getCartItems(){
        _progressBarLiveData.value=true
        if (!TokenContainer.token.isNullOrEmpty()){
            emptyStateLiveData.value= EmptyState(false)
            cartRepository.get()
                    .asyncNetworkRequest()
                    .doFinally {_progressBarLiveData.value=false}
                .subscribe(object : NikeSingleObserver<CartResponse>(compositeDisposable) {
                    override fun onSuccess(t: CartResponse) {
                        if (t.cart_items.isNotEmpty()) {
                            cartItemsLiveData.value = t.cart_items
                            purchaseDetailLiveData.value = PurchaseDetail(t.total_price, t.shipping_cost, t.payable_price)
                        }else
                            emptyStateLiveData.value= EmptyState(true, R.string.cartEmptyState)
                    }

                })
        }else
            emptyStateLiveData.value= EmptyState(true,R.string.cartEmptyStateLoginRequired,true)
    }
    /**
     * doAfterSuccess method call in background until result arrive then use postValue in doFinally
    */
    fun remove(cartItem:CartItem):Completable{
        _progressBarLiveData.value=true
       return cartRepository.remove(cartItem.cart_item_id)
               .doFinally { _progressBarLiveData.postValue(false) }
               .doAfterSuccess{calculateAndPublishPurchaseDetail()
                   cartItemsLiveData.value?.let {
                       if (it.isEmpty())
                       emptyStateLiveData.postValue(EmptyState(true,R.string.cartEmptyState))
                   }
  /*                if (cartItemsLiveData.value.isNullOrEmpty())
                      emptyStateLiveData.postValue(EmptyState(true,R.string.cartEmptyState))
*/
               }
                .ignoreElement()
    }
    fun increaseCartItemCount(cartItem: CartItem):Completable{
        return cartRepository.changeCount(cartItem.cart_item_id,++cartItem.count)
                .doAfterSuccess{calculateAndPublishPurchaseDetail()}
                .ignoreElement()
    }
    fun decreaseCartItemCount(cartItem: CartItem):Completable{
        return cartRepository.changeCount(cartItem.cart_item_id,--cartItem.count)
                .doAfterSuccess{calculateAndPublishPurchaseDetail()}
                .ignoreElement()
    }
    fun refresh(){
        getCartItems()
    }
      //this method call in background Thread then use postValue
    private fun calculateAndPublishPurchaseDetail() {
        cartItemsLiveData.value?.let {cartItems ->
        purchaseDetailLiveData.value?.let {PDetail ->
            var totalPrice=0
            var payablePrice=0
            cartItems.forEach {
                totalPrice+=it.product.price*it.count
                payablePrice+=(it.product.price-it.product.discount)*it.count
            }

            PDetail.totalPrice=totalPrice
            PDetail.payable_price=payablePrice

            purchaseDetailLiveData.postValue(PDetail)
        }

        }

    }






}