package com.elyeproj.origin

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.elyeproj.origin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val viewModel: MyViewModel by viewModels{
        MyViewModelFactory(this, Repository(),
            intent.extras)
    }

    private val textDataObserver =
        Observer<String> { data -> binding.textView.text = data }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.showTextDataNotifier.observe(this, textDataObserver)
        binding.btnFetch.setOnClickListener { viewModel.fetchValue() }
    }
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

class Repository {
    fun getMessage() = "From Repository"
}
