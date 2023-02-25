package com.dudu.supertoolbox.ui

import android.content.ComponentName
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.dudu.supertoolbox.BuildConfig
import com.dudu.supertoolbox.IUserService
import com.dudu.supertoolbox.R
import com.dudu.supertoolbox.UserService
import com.dudu.supertoolbox.util.makeToast
import rikka.shizuku.Shizuku
import rikka.shizuku.Shizuku.OnBinderReceivedListener
import rikka.shizuku.Shizuku.UserServiceArgs


class MainActivity : AppCompatActivity(),Shizuku.OnRequestPermissionResultListener {
    companion object{
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
    lateinit var userService:IUserService
    private val userServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, binder: IBinder?) {
            if (binder!=null&&binder.pingBinder()){
                userService = IUserService.Stub.asInterface(binder)
                makeToast(userService.exec("id"))
            }
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            //binding.text3.setText(
             //   """
              //  onServiceDisconnected:
              //  ${componentName.className}
              //  """.trimIndent()
            //)
        }
    }
    private val BINDER_RECEIVED_LISTENER = OnBinderReceivedListener {
    }

    private fun bindUserService() {
        val res = java.lang.StringBuilder()
        try {
            Shizuku.bindUserService(userServiceArgs, userServiceConnection)
        } catch (tr: Throwable) {
            tr.printStackTrace()
            res.append(tr.toString())
            makeToast(res.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Shizuku.addRequestPermissionResultListener(this)
        Shizuku.addBinderReceivedListener(BINDER_RECEIVED_LISTENER)

        if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED ){
        bindUserService()
        }else{
            Shizuku.requestPermission(114514)
        }

    }

    override fun onRequestPermissionResult(requestCode: Int, grantResult: Int) {
        if (grantResult==0){
            makeToast("授权成功")
            bindUserService()
        }else{
            makeToast("授权失败,请手动授权")
        }
    }

}