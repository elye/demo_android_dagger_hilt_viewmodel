package com.elyeproj.hilt

import android.app.Application
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.elyeproj.hilt.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltAndroidApp
class MainApplication: Application()

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MyViewModel by viewModels()

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

@HiltViewModel
class MyViewModel @Inject constructor(
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
