package com.example.nike.common

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nike.R
import com.example.nike.features.auth.AuthActivity
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class NikeFragment:Fragment(), NikeView {
    override val rootView: CoordinatorLayout?
        get() = view as CoordinatorLayout
    override val contextView: Context?
        get() = context

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }


}
abstract class NikeActivity:AppCompatActivity(), NikeView {
    override val rootView: CoordinatorLayout?
    /**
    * DecorView > LinearLayout > FrameLayout > action_bar_root >
     * content(ContentFrameLayout) > CoordinatorLayout
    * */
        get() {
          val viewGroup=  window.decorView.findViewById(android.R.id.content) as  ViewGroup
            if (viewGroup !is CoordinatorLayout){
                viewGroup.children.forEach {
                    if (it is CoordinatorLayout)
                        return it
                }
                throw IllegalStateException("rootView must be instance of CoordinatorLayout")
            }else
                return viewGroup
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)

    }
    override val contextView: Context?
        get() = this
}
interface NikeView {
    val rootView:CoordinatorLayout?
    val contextView:Context?

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showError(nikeException: NikeException){
        contextView?.let {
            when (nikeException.type) {
                NikeException.Type.SIMPLE -> showSnackBar(nikeException.serverMessage ?: it.getString(nikeException.userFriendlyMessage))
                NikeException.Type.AUTH -> {
                    if (!AuthActivity.isSelected)
                        it.startActivity(Intent(it, AuthActivity::class.java))
                        Toast.makeText(it, nikeException.serverMessage, Toast.LENGTH_SHORT).show()

                }
            }
        }
    }
    fun showSnackBar(message:String,duration:Int=Snackbar.LENGTH_SHORT){
        rootView?.let {
            Snackbar.make(it,message,duration).show()
        }
    }
    fun setProgressIndicator(mustShow:Boolean){
        rootView.let {
            contextView.let {contextView ->
                var loadingView=it?.findViewById<View>(R.id.loadingView)
                if (loadingView==null && mustShow){
                    loadingView=LayoutInflater.from(contextView).inflate(R.layout.view_loading,it,false)
                    it?.addView(loadingView)
            }
                loadingView?.visibility=if (mustShow) View.VISIBLE else View.GONE

            }
        }

    }
    fun setEmptyState( layoutRes: Int):View?{
        rootView?.let {
            contextView?.let {contextView ->
                var emptyState=it.findViewById<View>(R.id.emptyStateRootView)
               if (emptyState==null){
                   emptyState=LayoutInflater.from(contextView).inflate(layoutRes,it,false)
                   it.addView(emptyState)
               }
                emptyState.visibility=View.VISIBLE
                return emptyState

            }
        }
        return null
    }
abstract class NikeViewModel:ViewModel(){
    var compositeDisposable=CompositeDisposable()
    protected var _progressBarLiveData=MutableLiveData<Boolean>()
    val progressBarLiveData:LiveData<Boolean>
        get() =_progressBarLiveData

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}


}