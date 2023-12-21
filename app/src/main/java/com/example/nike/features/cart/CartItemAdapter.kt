package com.example.nike.features.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.nike.R
import com.example.nike.common.formatPrice
import com.example.nike.data.CartItem
import com.example.nike.data.PurchaseDetail
import com.example.nike.databinding.ItemCartBinding
import com.example.nike.databinding.ItemPurchaseDetailsBinding
import com.example.nike.services.ImageLoadingService
const val VIEW_TYPE_CART_ITEM=0
const val VIEW_TYPE_PURCHASE_DETAILS=1
class CartItemAdapter(
        val cartItems: MutableList<CartItem>,
        val imageLoadingService: ImageLoadingService,
        val cartItemViewCallbacks: CartItemViewCallbacks): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
     var purchaseDetail:PurchaseDetail?=null
    //cart viewHolder
    inner class CartViewHolder(val binding: ItemCartBinding):RecyclerView.ViewHolder(binding.root){
        fun bindCartItem(cartItem: CartItem){
            binding.apply {
                productTitleTv.text=cartItem.product.title
                cartItemCountTv.text=cartItem.count.toString()
                previousPriceTv.text= formatPrice(cartItem.product.price+cartItem.product.discount)
                priceTv.text= formatPrice(cartItem.product.price)
                imageLoadingService.load(productIv,cartItem.product.image)
                removeFromCartBtn.setOnClickListener {
                    cartItemViewCallbacks.onRemoveCartItemButtonClick(cartItem)
                }
                increaseBtn.setOnClickListener {
                    cartItem.cartItemProgressbarIsVisible=true
                    cartItemViewCallbacks.onIncreaseCartItemButtonClick(cartItem)
                changeCountProgressBar.visibility=View.VISIBLE
                    cartItemCountTv.visibility=View.INVISIBLE
                }
                decreaseBtn.setOnClickListener {
                    if (cartItem.count>1){
                        cartItem.cartItemProgressbarIsVisible=true
                        cartItemViewCallbacks.onDecreaseCartItemButtonClick(cartItem)
                        changeCountProgressBar.visibility=View.VISIBLE
                        cartItemCountTv.visibility=View.INVISIBLE

                    }
                    }
                removeFromCartBtn.setOnClickListener {
                    cartItemViewCallbacks.onRemoveCartItemButtonClick(cartItem)
                }
                productIv.setOnClickListener {
                    cartItemViewCallbacks.onProductImageClick(cartItem)
                }
                changeCountProgressBar.visibility=
                        if (cartItem.cartItemProgressbarIsVisible)
                    View.VISIBLE else View.GONE
                cartItemCountTv.visibility=if (cartItem.cartItemProgressbarIsVisible)
                    View.INVISIBLE else View.VISIBLE


            }

            }

        }
    //PurchaseDetailViewHolder
    inner class PurchaseDetailViewHolder(val binding:ItemPurchaseDetailsBinding):RecyclerView.ViewHolder(binding.root){

        fun bindPurchase(totalPrice:Int,shippingCost:Int,payablePrice:Int){
            binding.apply {
                totalPriceTv.text=totalPrice.toString()
                shippingCostTv.text=shippingCost.toString()
                payablePriceTv.text=payablePrice.toString()
            }
        }
    }

    // TODO: 4/2/2021 check  binding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType== VIEW_TYPE_CART_ITEM)
            CartViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_cart,parent,false))
        else PurchaseDetailViewHolder(ItemPurchaseDetailsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CartViewHolder)
            holder.bindCartItem(cartItems[position])
        else if (holder is PurchaseDetailViewHolder){
            purchaseDetail?.let {
                holder.bindPurchase(it.totalPrice,it.shipping_cost,it.payable_price)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position==cartItems.size)
            VIEW_TYPE_PURCHASE_DETAILS
        else VIEW_TYPE_CART_ITEM
    }

        //cartItems+ a purchase
    override fun getItemCount(): Int =cartItems.size+1
    interface CartItemViewCallbacks {
        fun onRemoveCartItemButtonClick(cartItem: CartItem)
        fun onIncreaseCartItemButtonClick(cartItem: CartItem)
        fun onDecreaseCartItemButtonClick(cartItem: CartItem)
        fun onProductImageClick(cartItem: CartItem)
    }
    /**
     when index is -1 it means list is empty.
     when list is empty you must reset purchase
     */
    fun removeItem(cartItem: CartItem){
        val index=cartItems.indexOf(cartItem)
        if (index>-1){
            cartItems.removeAt(index)
            notifyItemRemoved(index)
        }
        if (cartItems.isEmpty()){
            purchaseDetail?.let {
                it.payable_price=0
                it.totalPrice=0
                it.shipping_cost=0
            }

        }

    }
    fun changeCountItem(cartItem: CartItem){
        val index=cartItems.indexOf(cartItem)
        if (index>-1){
            cartItems[index].cartItemProgressbarIsVisible=false
            notifyItemChanged(index)
        }
    }
}

