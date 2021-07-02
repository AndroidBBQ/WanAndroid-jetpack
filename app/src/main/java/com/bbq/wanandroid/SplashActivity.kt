package com.bbq.wanandroid

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (XXPermissions.isGranted(this, Permission.Group.STORAGE)) {
            doThings()
        } else {
            XXPermissions.with(this)
                .permission(Permission.Group.STORAGE)
                .request { permissions, all ->
                    doThings()
                }
        }
    }

    private fun doThings() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}