package com.example.postly

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application entry point. Annotating with @HiltAndroidApp triggers Hilt's code generation,
 * including a base class for the application that serves as the app-level dependency container.
 */
@HiltAndroidApp
class PostlyApplication : Application()