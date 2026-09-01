package com.example.postly.features.splash.presentation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onNavigateToOnboarding: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val destination by viewModel.destination.collectAsStateWithLifecycle()

    LaunchedEffect(destination) {
        when (destination) {
            SplashDestination.Onboarding -> onNavigateToOnboarding()
            SplashDestination.Home -> onNavigateToHome()
            SplashDestination.Login -> onNavigateToLogin()
            null -> Unit
        }
    }

    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isVisible = true }

    // One-shot entrance (fade + scale in)
    val entranceScale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.85f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "splashEntranceScale"
    )
    val entranceAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "splashEntranceAlpha"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "splashPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logoPulseScale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.82f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logoPulseAlpha"
    )

    val colorScheme = MaterialTheme.colorScheme
    val decorationColor = colorScheme.primary.copy(alpha = 0.10f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        colorScheme.surface,
                        colorScheme.surfaceContainerHighest.copy(alpha = 0.45f)
                    )
                )
            )
    ) {
        Icon(
            imageVector = Icons.Outlined.Favorite,
            contentDescription = null,
            tint = decorationColor,
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 35.dp, y = 90.dp)
                .size(42.dp)
                .rotate(-9f)
        )
        Icon(
            imageVector = Icons.Outlined.Notifications,
            contentDescription = null,
            tint = decorationColor,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-40).dp, y = 130.dp)
                .size(34.dp)
                .rotate(11f)
        )
        Icon(
            imageVector = Icons.Outlined.ChatBubbleOutline,
            contentDescription = null,
            tint = decorationColor,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 25.dp, y = (-230).dp)
                .size(40.dp)
                .rotate(7f)
        )
        Icon(
            imageVector = Icons.Outlined.Send,
            contentDescription = null,
            tint = decorationColor,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-30).dp, y = (-270).dp)
                .size(38.dp)
                .rotate(-10f)
        )
        Icon(
            imageVector = Icons.Outlined.CameraAlt,
            contentDescription = null,
            tint = decorationColor,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 65.dp, y = (-110).dp)
                .size(30.dp)
                .rotate(8f)
        )
        Icon(
            imageVector = Icons.Outlined.ThumbUp,
            contentDescription = null,
            tint = decorationColor,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-65).dp, y = (-125).dp)
                .size(28.dp)
                .rotate(-6f)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer {
                    val scale = entranceScale * pulseScale
                    scaleX = scale
                    scaleY = scale
                    alpha = entranceAlpha * pulseAlpha
                }
        ) {
            PostlyLogo(
                size = 120.dp,
                color = colorScheme.primary
            )
        }

        LinearProgressIndicator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .width(140.dp)
                .clip(RoundedCornerShape(10.dp)),
            color = colorScheme.primary,
            trackColor = colorScheme.surfaceContainerHighest
        )
    }
}

@Composable
private fun PostlyLogo(
    size: Dp,
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val strokeWidth = this.size.minDimension * 0.055f
        val stroke = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        val corner = CornerRadius(this.size.minDimension * 0.22f)
        val inset = strokeWidth / 2

        drawRoundRect(
            color = color,
            topLeft = Offset(inset, inset),
            size = Size(this.size.width - inset * 2, this.size.height - inset * 2),
            cornerRadius = corner,
            style = stroke
        )

        val headerY = this.size.height * 0.34f

        drawLine(
            color = color,
            start = Offset(inset, headerY),
            end = Offset(this.size.width - inset, headerY),
            strokeWidth = strokeWidth
        )

        val dotRadius = this.size.minDimension * 0.035f
        val dotY = this.size.height * 0.19f
        drawCircle(
            color = color,
            radius = dotRadius,
            center = Offset(this.size.width * 0.24f, dotY)
        )
        drawCircle(
            color = color,
            radius = dotRadius,
            center = Offset(this.size.width * 0.38f, dotY)
        )

        drawLine(
            color = color,
            start = Offset(this.size.width * 0.52f, dotY),
            end = Offset(this.size.width * 0.82f, dotY),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        val lineStartX = this.size.width * 0.20f
        val widthFractions = listOf(0.46f, 0.80f, 0.66f)
        val lineYs = listOf(0.52f, 0.66f, 0.80f).map { this.size.height * it }
        widthFractions.forEachIndexed { index, widthFraction ->
            drawLine(
                color = color,
                start = Offset(lineStartX, lineYs[index]),
                end = Offset(lineStartX + (this.size.width * 0.62f * widthFraction), lineYs[index]),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
        }
    }
}