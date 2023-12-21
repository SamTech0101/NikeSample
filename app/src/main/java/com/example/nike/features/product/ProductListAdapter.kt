package com.example.nike.features.product

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nike.R
import com.example.nike.common.formatPrice
import com.example.nike.common.implementSpringAnimationTrait
import com.example.nike.data.Product
import com.example.nike.services.ImageLoadingService
import com.example.nike.view.NikeImageView

const val VIEW_TYPE_ROUND = 0
const val VIEW_TYPE_SMALL = 1
const val VIEW_TYPE_LARGE = 2
class ProductListAdapter( var viewType: Int= VIEW_TYPE_ROUND, val imageLoadingService: ImageLoadingService): RecyclerView.Adapter<ProductListAdapter.ProductListViewHoler>() {
   //item list is blank for first time and notifyDataSetChanged when  items arrived
    var products= ArrayList<Product>()
    set(value) {
        field=value
        notifyDataSetChanged()
    }
    //clickListener must be var because
    // in default this variable is null and in Homefragment is this
    var clickListener: OnProductClickListener?=null

    inner class ProductListViewHoler(itemView:View):RecyclerView.ViewHolder(itemView){
        /**
         * if you findViewById here . when items recycle then create it one time
         * */
        val productIv: NikeImageView = itemView.findViewById(R.id.productIv)
        val titleTv: TextView = itemView.findViewById(R.id.productTitleTv)
        val currentPriceTv: TextView = itemView.findViewById(R.id.currentPriceTv)
        val previousPriceTv: TextView = itemView.findViewById(R.id.previousPriceTv)

        fun bindProduct(product: Product){
            imageLoadingService.load(productIv,product.image)
            titleTv.text=product.title
            currentPriceTv.text= formatPrice(product.price)
            previousPriceTv.text= formatPrice(product.previous_price)
            previousPriceTv.paintFlags=Paint.STRIKE_THRU_TEXT_FLAG
            /**
             * dear Tokyo
             *  [implementSpringAnimationTrait()] is a Extension fun for view in utils
            [implementSpringAnimationTrait()] is a clickable method then you must set click listener for it*/

            itemView.implementSpringAnimationTrait()
            itemView.setOnClickListener {
                clickListener?.onProductClick(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListViewHoler {
      //create layouts when the layouts change
        val layoutResIs=when(viewType){
          VIEW_TYPE_ROUND -> R.layout.item_product
          VIEW_TYPE_SMALL -> R.layout.item_product_small
          VIEW_TYPE_LARGE -> R.layout.item_product_large
          else ->throw IllegalStateException("viewType is not valid")
      }

        return ProductListViewHoler(LayoutInflater.from(parent.context).inflate(layoutResIs,parent,false))
    }

    override fun onBindViewHolder(holder: ProductListViewHoler, position: Int) {
       holder.bindProduct(products[position])
    }
     // viewType with my viewType . no position
    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    override fun getItemCount(): Int =products.size

    interface OnProductClickListener{
        fun onProductClick(product: Product)
    }
}