package com.dudu.supertoolbox.ui

import android.content.ComponentName
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.dudu.supertoolbox.BuildConfig
import com.dudu.supertoolbox.IUserService
import com.dudu.supertoolbox.R
import com.dudu.supertoolbox.UserService
import com.dudu.supertoolbox.util.StartActivity
import com.dudu.supertoolbox.util.makeToast
import rikka.shizuku.Shizuku
import rikka.shizuku.Shizuku.UserServiceArgs


class MainActivity : AppCompatActivity(), Shizuku.OnRequestPermissionResultListener {
    companion object {
        private val userServiceArgs = UserServiceArgs(
            ComponentName(
                BuildConfig.APPLICATION_ID,
                UserService::class.java.name
            )
        )
            .daemon(false)
            .processNameSuffix("service")
            .debuggable(BuildConfig.DEBUG)
            .version(BuildConfig.VERSION_CODE)
    }

    lateinit var userService: IUserService
    private val userServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, binder: IBinder?) {
            if (binder != null && binder.pingBinder()) {
                userService = IUserService.Stub.asInterface(binder)
                makeToast(userService.exec("id"))
            }
        }

        override fun onServiceDisconnected(componentName: ComponentName) {

        }
    }

    /*  private val BINDER_RECEIVED_LISTENER = OnBinderReceivedListener {
          Log.e("","Received")
          ShizukuActivated.visibility = View.VISIBLE
          ShizukuNotActivated.visibility = View.GONE
      }
      private val BINDER_DEAD_LISTENER = Shizuku.OnBinderDeadListener {
          Log.e("","Dead")
          ShizukuActivated.visibility = View.GONE
          ShizukuNotActivated.visibility = View.VISIBLE
      }
     */
    private fun bindUserService() {
        //val res = java.lang.StringBuilder()
        //try {
        Shizuku.bindUserService(userServiceArgs, userServiceConnection)
        //} catch (tr: Throwable) {
        //tr.printStackTrace()
        //res.append(tr.toString())
        //makeToast(res.toString())
        //}
    }

    lateinit var ShizukuActivated: LinearLayout
    lateinit var ShizukuNotActivated: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            Shizuku.addRequestPermissionResultListener(this)
            //Shizuku.addBinderReceivedListener(BINDER_RECEIVED_LISTENER)
            //Shizuku.addBinderDeadListener(BINDER_DEAD_LISTENER)

            if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
                bindUserService()
            } else {
                Shizuku.requestPermission(114514)
            }
            initView()
        } catch (e: IllegalStateException) {
            Log.e("", "无法连接至shizuku服务")
            makeToast("无法连接至shizuku服务")
        }


    }

    private fun initView() {
        ShizukuActivated = findViewById(R.id.shizuku_status_activated)
        ShizukuNotActivated = findViewById(R.id.shizuku_status_not_activated)

        ShizukuActivated.setOnClickListener {
            StartActivity<ShizukuStatusActivity> { }
        }
        ShizukuNotActivated.setOnClickListener {
            StartActivity<ShizukuStatusActivity> { }
        }
        checkShizukuPermission()

    }

    override fun onRequestPermissionResult(requestCode: Int, grantResult: Int) {
        if (grantResult == 0) {
            makeToast("授权成功")
            checkShizukuPermission()
            bindUserService()
        } else {
            makeToast("授权失败,请手动授权")
            checkShizukuPermission()
        }
    }

    fun checkShizukuPermission() {
        if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
            ShizukuActivated.visibility = View.VISIBLE
            ShizukuNotActivated.visibility = View.GONE
        } else {
            ShizukuActivated.visibility = View.GONE
            ShizukuNotActivated.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            checkShizukuPermission()
        } catch (e: IllegalStateException) {
            Log.e("", "无法连接至shizuku服务")
            makeToast("无法连接至shizuku服务")
            StartActivity<CantConnectToShizukuActivity> { }
            finish()
        }

    }

}