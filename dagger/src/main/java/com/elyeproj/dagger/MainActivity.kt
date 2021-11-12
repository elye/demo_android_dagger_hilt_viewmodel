package com.elyeproj.dagger

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.elyeproj.dagger.MainApplication.Companion.mainComponent
import com.elyeproj.dagger.databinding.ActivityMainBinding
import dagger.*
import javax.inject.Inject
import javax.inject.Scope

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: MyViewModel

    private val textDataObserver =
        Observer<String> { data -> binding.textView.text = data }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainComponent.activityViewModelComponentBuilder()
            .componentActivity(this).build().inject(this)

        Log.d("Elisha", "$viewModel")

        viewModel.showTextDataNotifier.observe(this, textDataObserver)
        binding.btnFetch.setOnClickListener { viewModel.fetchValue() }
    }
}

class MainApplication: Application() {
    companion object {
        val mainComponent: MainComponent by lazy {
            DaggerMainComponent.create()
        }
    }
}

@Component
interface MainComponent {
    fun activityViewModelComponentBuilder(): ActivityViewModelComponent.Builder
}

@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope

@Module
class ActivityViewModelModule {
    @Provides
    @ActivityScope
    fun provideMyViewModel(activity: ComponentActivity, repository: Repository):
            MyViewModel {
        return ViewModelProvider(activity.viewModelStore,
            MyViewModelFactory(activity, repository, activity.intent.extras))
            .get(MyViewModel::class.java)
    }
}

@ActivityScope
@Subcomponent(modules = [ActivityViewModelModule::class])
interface ActivityViewModelComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun componentActivity(activity: ComponentActivity): Builder
        fun build(): ActivityViewModelComponent
    }

    fun inject(activity: MainActivity)
}

class MyViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val repository: Repository,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle
    ): T {
        return MyViewModel(
            repository, handle
        ) as T
    }
}

class MyViewModel(
    private val repository: Repository,
    savedStateHandle: SavedStateHandle
) : ViewModel(), LifecycleObserver {

    companion object {
        const val KEY = "KEY"
    }

    private val showTextLiveData
            = savedStateHandle.getLiveData<String>(KEY)

    val showTextDataNotifier: LiveData<String>
        get() = showTextLiveData

    fun fetchValue() {
        showTextLiveData.value = repository.getMessage()
    }
}

class Repository @Inject constructor() {
    fun getMessage() = "From Repository"
}
