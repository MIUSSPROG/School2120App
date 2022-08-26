package com.example.school2120app.core.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.text.HtmlCompat
import com.google.android.material.snackbar.Snackbar

fun Activity.toast(text: String, length: Int = Toast.LENGTH_SHORT){
    Toast.makeText(this, text, length).show()
}

fun Int.toBoolean(): Boolean{
    return this == 1
}
