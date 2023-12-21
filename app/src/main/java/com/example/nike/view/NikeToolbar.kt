package com.example.nike.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.example.nike.R

class NikeToolbar(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    var onBackBtnToolbar: OnClickListener? = null
        set(value) {
            field = value
            val toolbarBack: ImageView = this.findViewById(R.id.backBtn)
            toolbarBack.setOnClickListener(onBackBtnToolbar)
        }

    init {
        inflate(context, R.layout.view_toolbar, this)
        val toolbarTitle = this.findViewById<TextView>(R.id.toolbarTitle)
        if (attrs != null) {

            val a = context.obtainStyledAttributes(attrs, R.styleable.NikeToolbar)
            val title = a.getString(R.styleable.NikeToolbar_nt_title)
            if (title != null && title.isNotEmpty())
                toolbarTitle.text = title

            a.recycle()

        }

    }
}