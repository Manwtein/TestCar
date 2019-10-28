package ru.mainwtein.testcitymobil.scenes.fragments

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.mainwtein.testcitymobil.models.Car

@InjectViewState
class CarPresenter : MvpPresenter<CarView>() {

    fun startMovement(car: Car, x: Float, y: Float) {
        viewState.startMoving(car, x, y)
    }
}