package com.gasparian.rob.feature.profile.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gasparian.rob.feature.profile.presentation.ProfileUiState

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun RcvProfileComponent(
    title: String,
    uiState: ProfileUiState,
    buttonLabel: String,
    clearCacheLabel: String,
    onButtonClick: () -> Unit,
    onClearCacheClick: () -> Unit,
    onRefresh: () -> Unit,
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isRefreshing,
        onRefresh = onRefresh,
    )

    Surface(
        modifier =
        Modifier
            .fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState),
        ) {
            Column(
                modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
            ) {
                RcvPocTopBar(
                    title = title,
                    clearCacheLabel = clearCacheLabel,
                    onClearCacheClick = onClearCacheClick,
                )
                Spacer(Modifier.height(16.dp))
                when {
                    uiState.isLoading -> Text("Loading profile...")

                    uiState.errorMessage != null -> Text("Error: ${uiState.errorMessage}")

                    uiState.profile != null -> uiState.profile?.let { profile ->
                        RcvPlainField("Name", profile.displayName)
                        RcvPlainField("Headline", profile.headline)
                        RcvPlainField("Short bio", profile.shortBio)
                        RcvPlainField("Email", profile.contact.email)
                        RcvPlainField("Phone", profile.contact.phone)
                        RcvPlainField("LinkedIn", profile.contact.linkedin)
                        RcvPlainField("Professional profile", profile.professionalProfile)
                    }
                }
                Button(
                    onClick = onButtonClick,
                    modifier = Modifier.padding(top = 28.dp),
                ) {
                    Text(buttonLabel)
                }
            }
            PullRefreshIndicator(
                refreshing = uiState.isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
    }
}

@Composable
private fun RcvPocTopBar(
    title: String,
    clearCacheLabel: String,
    onClearCacheClick: () -> Unit,
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
        )
        IconButton(onClick = { isMenuExpanded = true }) {
            Text("...")
        }
        DropdownMenu(
            expanded = isMenuExpanded,
            onDismissRequest = { isMenuExpanded = false },
        ) {
            DropdownMenuItem(
                text = { Text(clearCacheLabel) },
                onClick = {
                    isMenuExpanded = false
                    onClearCacheClick()
                },
            )
        }
    }
}

@Composable
private fun RcvPlainField(
    label: String,
    value: String,
) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        readOnly = true,
        label = { Text(label) },
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        minLines = 1,
        maxLines = 4,
    )
}
