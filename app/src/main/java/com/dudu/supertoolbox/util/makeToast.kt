package com.dudu.supertoolbox.util

import android.widget.Toast

fun makeToast(text:String){
    Toast.makeText(ToolBoxApplication.context,text,Toast.LENGTH_SHORT).show()
}