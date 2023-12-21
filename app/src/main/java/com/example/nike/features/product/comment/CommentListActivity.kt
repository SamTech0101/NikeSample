package com.example.nike.features.product.comment

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nike.R
import com.example.nike.common.EXTRA_KEY_ID
import com.example.nike.common.NikeActivity
import com.example.nike.data.Comment
import com.example.nike.databinding.ActivityCommentListBinding
import com.example.nike.features.product.detail.CommentAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CommentListActivity : NikeActivity() {
    private lateinit var binding: ActivityCommentListBinding
    private val commentAdapter = CommentAdapter(true)

    private val commentListViewModel: CommentListViewModel by viewModel {
        parametersOf(intent.extras!!.getInt(
                EXTRA_KEY_ID
        ))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_comment_list)
        binding.commentsRv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.commentsRv.adapter = commentAdapter
        //comments
        commentListViewModel.commentLiveData.observe(this, {
            commentAdapter.comments = it as ArrayList<Comment>
        })
        /**
         *set finish on ic_bak in NikeToolbar
         */
        binding.commentListToolbar.onBackBtnToolbar = View.OnClickListener {
            finish()
        }
        commentListViewModel.progressBarLiveData.observe(this, {
            setProgressIndicator(it)
        })

    }
}