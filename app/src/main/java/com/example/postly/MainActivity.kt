package com.example.postly

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.postly.core.data.local.ThemeModePreference
import com.example.postly.core.navigation.RootNavHost
import com.example.postly.core.push.DeepLinkNotifier
import com.example.postly.core.push.PostlyFirebaseMessagingService
import com.example.postly.core.push.PushDeepLink
import com.example.postly.ui.theme.PostlyTheme
import com.example.postly.ui.theme.ThemeMode
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var themeModePreference: ThemeModePreference
    @Inject
    lateinit var deepLinkNotifier: DeepLinkNotifier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        handlePushIntent(intent)
        setContent {
            val themeMode by themeModePreference.observeThemeMode().collectAsState(initial = ThemeMode.SYSTEM)

            PostlyTheme(themeMode = themeMode) {
                RootNavHost()
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handlePushIntent(intent)
    }

    private fun handlePushIntent(intent: Intent?) {
        val type = intent?.getStringExtra(PostlyFirebaseMessagingService.EXTRA_TYPE) ?: return
        deepLinkNotifier.notify(
            PushDeepLink(
                type = type,
                postId = intent.getStringExtra(PostlyFirebaseMessagingService.EXTRA_POST_ID),
                senderId = intent.getStringExtra(PostlyFirebaseMessagingService.EXTRA_SENDER_ID)
            )
        )
        // Prevent re-handling the same tap again on a config change or recreate.
        intent.removeExtra(PostlyFirebaseMessagingService.EXTRA_TYPE)
    }
}