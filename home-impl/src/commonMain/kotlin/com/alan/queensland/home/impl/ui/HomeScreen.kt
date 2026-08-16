package com.alan.queensland.home.impl.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alan.queensland.core.ui.base.FormFactor
import com.alan.queensland.core.ui.base.compose.components.AppButton
import com.alan.queensland.core.ui.base.compose.themes.Paddings
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import queensland.home_impl.generated.resources.Res
import queensland.home_impl.generated.resources.app_icon
import queensland.home_impl.generated.resources.leaderboard
import queensland.home_impl.generated.resources.new_game
import queensland.home_impl.generated.resources.resume_game

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
) {
    if (remember { FormFactor.isTablet() }) {
        HomeTabletScreen(viewModel)
    } else {
        HomePhoneScreen(viewModel)
    }
}

@Composable
private fun HomeTabletScreen(
    viewModel: HomeViewModel,
) {
    // Tablet-specific layout will replace this functional phone fallback.
    HomePhoneScreen(viewModel)
}

@Composable
private fun HomePhoneScreen(
    viewModel: HomeViewModel,
) {
    val hasActiveGame by viewModel.hasActiveGame.collectAsStateWithLifecycle()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(Paddings.one),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(Res.drawable.app_icon),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(HOME_IMAGE_ASPECT_RATIO),
            )
            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Paddings.half),
            ) {
                AppButton(
                    text = stringResource(Res.string.new_game),
                    onClick = viewModel::onNewGameClick,
                    modifier = Modifier.fillMaxWidth(),
                )
                if (hasActiveGame) {
                    AppButton(
                        text = stringResource(Res.string.resume_game),
                        onClick = viewModel::onResumeGameClick,
                        modifier = Modifier.fillMaxWidth(),
                        isOutlined = true,
                    )
                }
                AppButton(
                    text = stringResource(Res.string.leaderboard),
                    onClick = viewModel::onLeaderBoardClick,
                    modifier = Modifier.fillMaxWidth(),
                    isOutlined = true,
                )
            }
        }
    }
}

private const val HOME_IMAGE_ASPECT_RATIO = 1312f / 1199f
