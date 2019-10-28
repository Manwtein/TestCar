package ru.mainwtein.testcitymobil.scenes.fragments

import android.animation.Animator
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Path
import android.graphics.PointF
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_car.*
import ru.mainwtein.testcitymobil.R
import ru.mainwtein.testcitymobil.animators.OnMovementListener
import ru.mainwtein.testcitymobil.animators.PathAnimator
import ru.mainwtein.testcitymobil.models.Car
import ru.mainwtein.testcitymobil.utils.PathCalculator

class CarFragment : MvpAppCompatFragment(), CarView {

    @InjectPresenter
    lateinit var presenter: CarPresenter

    private var mCurrentAnim: Animator? = null
    private var pathCalculator = PathCalculator()
    private lateinit var car: Car
    private var pathAnimator: PathAnimator? = null
    private var mCarWidth = 0
    private var mCarHeight = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_car, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewTreeObserver = carIv.viewTreeObserver
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    initCarComponents()
                    carIv.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
        }

        pathAnimator = PathAnimator(object : OnMovementListener {

            override fun onPositionChanged(x: Float, y: Float, alpha: Double) {
                car.updatePositionState(x, y, alpha)
                updateImageViewCar(x, y, alpha)
            }

            override fun onDrawPath(path: Path) {
                signpostView.drawPath(path)
            }

        })
        setListeners()
    }

    override fun startMoving(car: Car, endX: Float, endY: Float) {
        val calculatedPath = pathCalculator.calculatePath(
            car,
            endX,
            endY
        )
        mCurrentAnim = pathAnimator?.getMovingAnimation(calculatedPath, car)
        mCurrentAnim?.start()
    }

    private fun updateImageViewCar(x: Float, y: Float, alpha: Double) {
        carIv.x = x - mCarWidth / 2f
        carIv.y = y - mCarHeight / 2f
        carIv.rotation = alpha.toFloat()
    }


    private fun initCarComponents() {
        carIv.rotation = 90f
        mCarWidth = carIv.width
        mCarHeight = carIv.height
        val carHorizontalPosition = carIv.x + mCarWidth / 2
        val carVerticalPosition = carIv.y + mCarHeight / 2
        car = Car(PointF(carHorizontalPosition, carVerticalPosition), 90.0)
    }

    @SuppressLint("InflateParams")
    private fun setListeners() {
        carFragmentView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                presenter.startMovement(car, event.x, event.y)
            }
            return@setOnTouchListener false
        }

        debugBtn.setOnClickListener {
            signpostView.switchDebugMode()
        }

        carChosenBtn.setOnClickListener {
            createChosenCarDialog()
        }
    }

    @SuppressLint("InflateParams")
    private fun createChosenCarDialog() {
        val chosenCarView = layoutInflater.inflate(R.layout.dialog_chosen_car, null)
        context?.let {
            val alertDialog = AlertDialog
                    .Builder(it)
                    .setView(chosenCarView)
                    .create()

            alertDialog.show()
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setChoseCarListeners(chosenCarView, alertDialog)
        }
    }

    private fun setChoseCarListeners(chosenCarView: View, alertDialog: AlertDialog) {
        chosenCarView.findViewById<ImageView>(R.id.orangeCar)?.setOnClickListener {
            changeCarResource(R.drawable.ic_orange_car, alertDialog)
        }
        chosenCarView.findViewById<ImageView>(R.id.purpleCar)?.setOnClickListener {
            changeCarResource(R.drawable.ic_purple_car, alertDialog)
        }
        chosenCarView.findViewById<ImageView>(R.id.blueCar)?.setOnClickListener {
            changeCarResource(R.drawable.ic_blue_car, alertDialog)
        }
        chosenCarView.findViewById<ImageView>(R.id.greenCar)?.setOnClickListener {
            changeCarResource(R.drawable.ic_green_car, alertDialog)
        }
    }

    private fun changeCarResource(resource: Int, alertDialog: AlertDialog) {
        carIv.setBackgroundResource(resource)
        alertDialog.dismiss()
    }
}
