package com.example.nike

import android.app.Application
import android.content.SharedPreferences
import android.os.Bundle
import com.example.nike.data.repository.*
import com.example.nike.data.source.*
import com.example.nike.features.auth.AuthViewModel
import com.example.nike.features.cart.CartViewModel
import com.example.nike.features.home.HomeViewModel
import com.example.nike.features.list.ProductListViewModel
import com.example.nike.features.product.ProductListAdapter
import com.example.nike.features.product.comment.CommentListViewModel
import com.example.nike.features.product.detail.ProductDetailViewModel
import com.example.nike.services.FrescoImageLoadingService
import com.example.nike.services.ImageLoadingService
import com.example.nike.services.http.ApiService
import com.example.nike.services.http.createApiServiceInstance
import com.facebook.drawee.backends.pipeline.Fresco
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class App:Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Fresco.initialize(this)

        val myModules= module {
            single<ApiService> { createApiServiceInstance()}
            factory<CommentRepository> {CommentRepositoryImpl(CommentRemoteDataSource(get()))  }
            factory<ImageLoadingService> {FrescoImageLoadingService() }
            single<SharedPreferences> { this@App.getSharedPreferences("Nike_setting", MODE_PRIVATE)}
            single<UserRepository> {UserRepositoryImpl(UserLocalDataSource(get()),UserRemoteDataSource(get()))  }
            single { UserLocalDataSource(get()) }
            factory { (viewType:Int) -> ProductListAdapter(viewType,get()) }
            factory<ProductRepository> {ProductRepositoryImpl(ProductRemoteDataSource(get()), ProductLocalDataSource())  }
            factory<CartRepository> {CartRepositoryImpl(CartRemoteDataSource(get()))  }
            factory<BannerRepository> {BannerRepositoryImpl(BannerRemoteDataSource(get())) }
            viewModel { HomeViewModel(get(),get()) }
            viewModel { (productId:Int) -> CommentListViewModel(productId,get()) }
            viewModel {(bundle: Bundle)-> ProductDetailViewModel(bundle,get(),get()) }
            viewModel {(sort:Int)-> ProductListViewModel(sort,get()) }
            viewModel {AuthViewModel(get())  }
            viewModel {CartViewModel(get()) }
        }
        startKoin {
            androidContext(this@App)
            modules(myModules)
        }
        val userRepository:UserRepository=get()
        userRepository.loadToken()

    }
}