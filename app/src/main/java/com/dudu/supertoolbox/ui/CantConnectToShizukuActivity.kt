package com.dudu.supertoolbox.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dudu.supertoolbox.R
import com.google.android.material.button.MaterialButton

class CantConnectToShizukuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cant_connect_to_shizuku)

        findViewById<MaterialButton>(R.id.exit_bt).setOnClickListener {
            finish()
        }
    }
}