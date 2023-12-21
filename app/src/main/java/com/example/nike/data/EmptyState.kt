package com.example.nike.data

import androidx.annotation.StringRes

data class EmptyState(var mustShow:Boolean,@StringRes var messageResId: Int=0,var showActionButton:Boolean=false)
