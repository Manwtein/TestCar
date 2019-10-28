package ru.mainwtein.testcitymobil.utils

import android.view.View
import android.widget.Toast

fun View.showShortToast(message: String) {
    Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
}