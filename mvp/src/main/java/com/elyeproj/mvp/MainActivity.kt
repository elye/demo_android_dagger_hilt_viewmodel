package com.elyeproj.mvp

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Inject
import javax.inject.Scope

class MainActivity : AppCompatActivity(), MainView {
    companion object {
        const val KEY = "KEY"
    }

    private val presenter = MainPresenter(this)

    private lateinit var button: Button
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.btn_fetch)
        textView = findViewById(R.id.text_view)
        MainApplication.mainComponent.activityComponent.inject(presenter)

        if (savedInstanceState == null) {
            setText(intent.extras?.getString(KEY))
        } else {
            setText(savedInstanceState.getString(KEY))
        }

        button.setOnClickListener {
            presenter.fetchValue()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY, textView.text.toString())
    }

    override fun setText(text: String?) {
        textView.text = text
    }
}

class MainApplication : Application() {
    companion object {
        val mainComponent: MainComponent by lazy {
            DaggerMainComponent.create()
        }
    }
}

interface MainView {
    fun setText(text: String?)
}

class MainPresenter(private val view: MainView) {
    @Inject
    lateinit var repository: Repository

    fun fetchValue() {
        view.setText(repository.getMessage())
    }
}

@Component
interface MainComponent {
    val activityComponent: ActivityComponent
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope

@Subcomponent
interface ActivityComponent {
    fun inject(presenter: MainPresenter)
}

class Repository @Inject constructor() {
    fun getMessage() = "From Repository"
}
