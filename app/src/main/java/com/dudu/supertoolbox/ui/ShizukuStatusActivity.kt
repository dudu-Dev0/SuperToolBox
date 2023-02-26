package com.dudu.supertoolbox.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.dudu.supertoolbox.R
import com.dudu.supertoolbox.util.makeToast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import rikka.shizuku.Shizuku

class ShizukuStatusActivity : AppCompatActivity(), Shizuku.OnRequestPermissionResultListener {
    private lateinit var shizukuStatusTv: MaterialTextView
    private lateinit var shizukuStatusImg: ImageView
    private lateinit var isShizukuCanUse: MaterialTextView
    private lateinit var requestPermission: MaterialButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shizuku_status)

        shizukuStatusTv = findViewById(R.id.shizuku_status_tv)
        shizukuStatusImg = findViewById(R.id.shizuku_status_img)
        isShizukuCanUse = findViewById(R.id.is_shizuku_can_use_tv)
        requestPermission = findViewById(R.id.request_shizuku_permission)

        Shizuku.addRequestPermissionResultListener(this)

        checkShizukuPermission()

        requestPermission.setOnClickListener {
            Shizuku.requestPermission(114514)
        }
    }

    fun checkShizukuPermission() {
        if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
            shizukuStatusTv.text = "已授权"
            shizukuStatusImg.background = getDrawable(R.drawable.activated)
            isShizukuCanUse.text = "恭喜,您已可使用绝大多数功能"
            requestPermission.visibility = View.GONE
        } else {
            shizukuStatusTv.text = "未授权"
            shizukuStatusImg.background = getDrawable(R.drawable.not_activated)
            isShizukuCanUse.text = "请先授权再使用哦～"
            requestPermission.visibility = View.VISIBLE
        }
    }

    override fun onRequestPermissionResult(requestCode: Int, grantResult: Int) {
        if (grantResult == 0) {
            makeToast("授权成功")
            checkShizukuPermission()
        } else {
            makeToast("授权失败")
            checkShizukuPermission()
        }
    }

}