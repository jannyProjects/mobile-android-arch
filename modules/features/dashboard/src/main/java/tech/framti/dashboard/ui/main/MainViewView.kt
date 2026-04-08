package tech.framti.dashboard.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import tech.framti.common.R
import tech.framti.common.base.ui.BaseLoadingDisableView
import tech.framti.common.ui.widget.AssetCard
import tech.framti.common.ui.widget.CamlFilledButton
import tech.framti.common.ui.widget.StatusBadge
import tech.framti.theme.whiteBg

@Composable
fun MainViewRoot(viewModel: MainViewModel = hiltViewModel()) {
    val loading = viewModel.loadingState.value
    BaseLoadingDisableView(contentEnabled = !loading) {
        MainView(viewModel.state.value, viewModel.loadingState.value) { event ->
            viewModel.onViewEvent(event)
        }
    }
}

@Composable
private fun MainView(
    viewState: UiState,
    isLoading: Boolean,
    onEvent: (event: UiEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(whiteBg)
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        AssetCard(
            serialNumber = viewState.generatedUuid
                ?: stringResource(R.string.main_view_generate_uuid)
        )

        Spacer(modifier = Modifier.height(24.dp))

        StatusBadge(authState = viewState.authState)

        Spacer(modifier = Modifier.height(16.dp))

        CamlFilledButton(
            text = stringResource(R.string.main_view_scratch_card),
            isLoading = isLoading
        ) {
            onEvent.invoke(UiEvent.NavigateToScratch)
        }

        Spacer(modifier = Modifier.height(16.dp))
        CamlFilledButton(
            text = stringResource(R.string.main_view_activate_card),
            isLoading = isLoading,
            enabled = viewState.canActivate
        ) {
            onEvent.invoke(UiEvent.NavigateToActivate)
        }

        Spacer(modifier = Modifier.height(16.dp))
        CamlFilledButton(
            text = stringResource(R.string.main_view_deactivate_reset),
            isLoading = isLoading
        ) {
            onEvent.invoke(UiEvent.Deactivate)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
@Preview
private fun MainViewPreview() {
    MainView(UiState(), false) {}
}