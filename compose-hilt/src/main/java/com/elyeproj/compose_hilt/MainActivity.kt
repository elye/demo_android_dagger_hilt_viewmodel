package com.elyeproj.compose_hilt

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.elyeproj.compose_hilt.ui.theme.ViewModelHiltInjectionTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltAndroidApp
class MainApplication: Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        const val KEY = "KEY"
    }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val stateVariable = viewModel.showTextDataNotifier.collectAsState()

            ViewModelHiltInjectionTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column (
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ){
                        Text(
                            stateVariable.value,
                            modifier = Modifier
                                .padding(0.dp, 80.dp, 0.dp, 0.dp)
                                .width(250.dp)
                                .background(Color.LightGray)
                                .padding(16.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp
                        )
                        Button(
                            onClick = { viewModel.fetchValue() },
                            modifier = Modifier
                                .padding(0.dp, 50.dp, 0.dp, 0.dp)
                                .width(250.dp)
                        ) {
                            Text(
                                "Fetch",
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    companion object {
        const val KEY = "KEY"
    }

    private val showTextStateFlow
            = savedStateHandle.getStateFlow(KEY, "Initial Default")

    val showTextDataNotifier: StateFlow<String>
        get() = showTextStateFlow

    fun fetchValue() {
        savedStateHandle[KEY] = repository.getMessage()
    }
}

class Repository @Inject constructor() {
    fun getMessage() = "From Repository"
}
