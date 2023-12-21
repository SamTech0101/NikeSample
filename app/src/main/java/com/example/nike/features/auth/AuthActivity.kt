package com.example.nike.features.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nike.R

class AuthActivity : AppCompatActivity() {
    companion object{var isSelected=false}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        isSelected=true
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container,LoginFragment())
                    .commit()
        }
    }

    override fun onStop() {
        super.onStop()
        isSelected=false
    }
}