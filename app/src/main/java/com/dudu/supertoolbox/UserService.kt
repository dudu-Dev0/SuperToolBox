package com.dudu.supertoolbox

import android.content.Context
import android.os.RemoteException
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader

class UserService() : IUserService.Stub() {

    override fun destroy() {
        Log.i("UserService", "destroy")
        System.exit(0)
    }

    override fun exit() {
        destroy()
    }

    override fun exec(cmd: String): String {
        val buf = StringBuilder()
        try {
            val bufferedReader = BufferedReader(InputStreamReader(Runtime.getRuntime().exec(cmd).inputStream))
            var line : String
            do{
                line = bufferedReader.readLine()
                if (line != null){
                    buf.append(line).append("\n")
                }else{
                    break
                }
            }while (true)
        }catch(e:Exception){
            buf.append(e.message).append("/n")
        }
        return buf.toString()
    }

    //companion object {
        //static {
        //    System.loadLibrary("hello-jni");
        //}
        //external fun stringFromJNI(): String?
    //}
}