package com.example.nike.features.product.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nike.R
import com.example.nike.common.EXTRA_KEY_ID
import com.example.nike.common.NikeActivity
import com.example.nike.common.NikeCompletableObserver
import com.example.nike.common.formatPrice
import com.example.nike.data.Comment
import com.example.nike.databinding.ActivityProductDetailBinding
import com.example.nike.features.product.comment.CommentListActivity
import com.example.nike.services.ImageLoadingService
import com.example.nike.view.scroll.ObservableScrollViewCallbacks
import com.example.nike.view.scroll.ScrollState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class ProductDetailActivity : NikeActivity() {
    private lateinit var binding: ActivityProductDetailBinding
    private val viewModel: ProductDetailViewModel by viewModel { parametersOf(intent.extras) }
    private val imageLoadingService: ImageLoadingService by inject()
    private val commentAdapter= CommentAdapter()
    private val compositeDisposable=CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail)

        //get Product
        viewModel.productLiveData.observe(this, { product ->
            imageLoadingService.load(binding.productIvDetail, product.image)
            binding.titleTv.text = product.title
            binding.previousPriceTv.text = formatPrice(product.previous_price)
            binding.currentPriceTv.text = formatPrice(product.price)
            binding.toolbarTitleTv.text = product.title

        })
         //get Comments
        viewModel.commentLiveData.observe(this, {
//            Timber.i("the comment is --- > $it")
            commentAdapter.comments=it as ArrayList<Comment>
            if (it.size>3)
                binding.viewAllCommentsBtn.visibility=View.VISIBLE
            binding.viewAllCommentsBtn.setOnClickListener {
                startActivity(Intent(this,CommentListActivity::class.java)
                        .apply { putExtra(EXTRA_KEY_ID,viewModel.productLiveData.value!!.id) })
            }

        })

                //progressbar
        viewModel.progressBarLiveData.observe(this,{
            setProgressIndicator(it)
        })

        initView()

    }

   private fun initView() {

       binding.commentsRv.adapter=commentAdapter
       binding.commentsRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

       //post is a method in View.call it when layout inflate to end
       binding.productIvDetail.post {
            val productHeight = binding.productIvDetail.measuredHeight
            binding.observableScrollView.addScrollViewCallbacks(object : ObservableScrollViewCallbacks {
                override fun onScrollChanged(scrollY: Int, firstScroll: Boolean, dragging: Boolean) {
                    Timber.i("productIv height is -> $productHeight")
                    binding.toolbarView.alpha = scrollY.toFloat() / productHeight.toFloat()
                    binding.productIvDetail.translationY = scrollY.toFloat() / 2
                }

                override fun onDownMotionEvent() {
                }

                override fun onUpOrCancelMotionEvent(scrollState: ScrollState?) {
                }
            })


        }

       //sent request to server
       binding.addToCartBtn.setOnClickListener{
           viewModel.onAddToCartBtn().subscribeOn(Schedulers.newThread())
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe(object : NikeCompletableObserver(compositeDisposable){
                       override fun onComplete() {
                          showSnackBar(getString(R.string.success_addToCart))
                       }
                   })
       }
   }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}