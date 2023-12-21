package com.example.nike.features.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.nike.R
import com.example.nike.common.EXTRA_KEY_DATA
import com.example.nike.common.NikeFragment
import com.example.nike.common.convertDpToPixel
import com.example.nike.data.Product
import com.example.nike.data.SORT_LATEST
import com.example.nike.data.SORT_POPULAR
import com.example.nike.features.list.ProductListActivity
import com.example.nike.features.product.ProductListAdapter
import com.example.nike.features.product.VIEW_TYPE_ROUND
import com.example.nike.features.product.detail.ProductDetailActivity
import com.google.android.material.button.MaterialButton
import com.tbuonomo.viewpagerdotsindicator.BaseDotsIndicator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class HomeFragment : NikeFragment(), ProductListAdapter.OnProductClickListener {
    private val homeViewModel: HomeViewModel by viewModel()
    private val latestProductListAdapter: ProductListAdapter by inject {
        parametersOf(
                VIEW_TYPE_ROUND)
    }
    private val popularProductListAdapter: ProductListAdapter by inject {
        parametersOf(
                VIEW_TYPE_ROUND)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager = view.findViewById<ViewPager2>(R.id.bannerSliderViewPager)
        val sliderIndicator = view.findViewById<BaseDotsIndicator>(R.id.sliderIndicator)
        val latestProductsRv = view.findViewById<RecyclerView>(R.id.latestProductsRv)
        val popularProductsRv = view.findViewById<RecyclerView>(R.id.popularProductsRv)
        val latestProductsBtn = view.findViewById<MaterialButton>(R.id.viewLatestProductsBtn)
        val popularProductsBtn = view.findViewById<MaterialButton>(R.id.viewPopularProductsBtn)

        latestProductsBtn.setOnClickListener {
            startActivity(Intent(context, ProductListActivity::class.java).apply {
                putExtra(EXTRA_KEY_DATA, SORT_LATEST)
            })
        }
        popularProductsBtn.setOnClickListener {
            startActivity(Intent(context, ProductListActivity::class.java).apply {
                putExtra(EXTRA_KEY_DATA, SORT_POPULAR)
            })
        }

        latestProductListAdapter.clickListener = this
        popularProductListAdapter.clickListener = this
        latestProductsRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        latestProductsRv.adapter = latestProductListAdapter

        popularProductsRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        popularProductsRv.adapter = popularProductListAdapter
        homeViewModel.progressBarLiveData.observe(viewLifecycleOwner, Observer {
            setProgressIndicator(it)
        })
        homeViewModel.popularProductLiveData.observe(viewLifecycleOwner, { products ->
            popularProductListAdapter.products = products as ArrayList<Product>
        })

        homeViewModel.latestProductLiveData.observe(viewLifecycleOwner, { products ->
            latestProductListAdapter.products = products as ArrayList<Product>
            Timber.i(products.toString())
        })
        homeViewModel.bannerLiveData.observe(viewLifecycleOwner) {
            val bannerSliderAdapter = BannerSliderAdapter(this, it)
            val viewPagerHeight = ((viewPager.measuredWidth - (convertDpToPixel(32f, requireContext()))) * 173) / 328
            val layoutParams = viewPager.layoutParams
            layoutParams.height = viewPagerHeight.toInt()
            viewPager.layoutParams = layoutParams
            viewPager.adapter = bannerSliderAdapter
            sliderIndicator.setViewPager2(viewPager)
        }
    }

    override fun onProductClick(product: Product) {
        startActivity(Intent(requireContext(), ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, product)
        })

    }
}