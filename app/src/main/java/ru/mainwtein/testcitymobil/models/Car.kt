package ru.mainwtein.testcitymobil.models

import android.graphics.PointF


data class Car(var location: PointF, var alpha: Double) {

    fun updatePositionState(x: Float, y: Float, alpha: Double) {
        location.x = x
        location.y = y
        this.alpha = alpha
    }
}