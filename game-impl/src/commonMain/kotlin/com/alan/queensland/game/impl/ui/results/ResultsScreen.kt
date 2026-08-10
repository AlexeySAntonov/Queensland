package com.alan.queensland.game.impl.ui.results

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.alan.queensland.core.ui.base.compose.components.AppToolbar
import com.alan.queensland.core.ui.base.compose.themes.Paddings

@Composable
fun ResultsScreen(
    viewModel: ResultsViewModel,
) {
    Scaffold(
        topBar = {
            AppToolbar(
                title = "Results",
                navigationIcon = Icons.Default.Close,
                navigationIconContentDescription = "Back",
                onNavigationClick = viewModel::onBackClick,
            )
        },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(Paddings.one),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "No completed games yet",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}
