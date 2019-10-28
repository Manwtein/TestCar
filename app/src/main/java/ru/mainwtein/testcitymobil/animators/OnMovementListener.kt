package ru.mainwtein.testcitymobil.animators

import android.graphics.Path

interface OnMovementListener {

    fun onPositionChanged(x: Float, y: Float, alpha: Double)
    fun onDrawPath(path: Path)
}