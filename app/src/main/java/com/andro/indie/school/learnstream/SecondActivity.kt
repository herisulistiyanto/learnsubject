package com.andro.indie.school.learnstream

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_second.*
import java.util.concurrent.TimeUnit

class SecondActivity: AppCompatActivity() {

    private val asyncSubject: BehaviorSubject<Int> = BehaviorSubject.create<Int>()
    private var count = 0
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        subscribeSubject()
        btnAction.setOnClickListener {
            count++
            asyncSubject.onNext(count)
        }
    }

    private fun subscribeSubject() {
        val disposable = asyncSubject
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(
                {
                    println("count : $it")
                    tvLabel.text = "$it"
                }
        )
        disposables.add(disposable)
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }
}