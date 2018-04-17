package com.andro.indie.school.learnstream

import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.v7.app.AppCompatActivity
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.animation.LinearInterpolator
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val subject: PublishSubject<Pair<Boolean, Boolean>> = PublishSubject.create()
    private var statusA: Boolean = false
    private var statusB: Boolean = false
    private val constrainSet = ConstraintSet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        constrainSet.clone(constraintLayout)

        btnToggleA.setOnClickListener {
            statusA = !statusA
            if (statusA) {
                statusB = !statusA
            }
            subject.onNext(Pair(statusA, statusB))
        }

        btnToggleB.setOnClickListener {
            statusB = !statusB
            if (statusB) {
                statusA = !statusB
            }
            subject.onNext(Pair(statusA, statusB))
        }

        observeChange()

        btnContoh.setOnClickListener {
            Toast.makeText(this@MainActivity, "${Pair(statusA, statusB)}", Toast.LENGTH_SHORT).show()
        }

    }

    private fun observeChange() {
        subject.observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val enable = it.first || it.second
                    btnContoh.text = enable.toString()
                    btnContoh.isEnabled = enable

                    btnToggleA.text = if (it.first) "ON" else "OFF"
                    btnToggleB.text = if (it.second) "ON" else "OFF"

                    val transition = ChangeBounds().apply {
                        interpolator = LinearInterpolator()
                        duration = 100
                    }
                    TransitionManager.beginDelayedTransition(constraintLayout, transition)

                    constrainSet.clear(R.id.btnContoh, ConstraintSet.TOP)
                    constrainSet.clear(R.id.btnContoh, ConstraintSet.BOTTOM)
                    if (enable) {
                        constrainSet.connect(R.id.btnContoh,
                                ConstraintSet.BOTTOM,
                                R.id.guideline,
                                ConstraintSet.TOP)
                    } else {
                        constrainSet.connect(R.id.btnContoh,
                                ConstraintSet.TOP,
                                R.id.guideline,
                                ConstraintSet.BOTTOM)
                    }

                    constrainSet.applyTo(constraintLayout)
                }
    }
}
