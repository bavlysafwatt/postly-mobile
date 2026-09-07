package com.example.postly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.postly.core.data.local.ThemeModePreference
import com.example.postly.core.navigation.RootNavHost
import com.example.postly.ui.theme.PostlyTheme
import com.example.postly.ui.theme.ThemeMode
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var themeModePreference: ThemeModePreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by themeModePreference.observeThemeMode().collectAsState(initial = ThemeMode.SYSTEM)

            PostlyTheme(themeMode = themeMode) {
                RootNavHost()
            }
        }
    }
}