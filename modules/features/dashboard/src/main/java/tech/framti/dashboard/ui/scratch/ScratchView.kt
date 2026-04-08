package tech.framti.dashboard.ui.scratch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import tech.framti.common.R
import tech.framti.common.ui.widget.AssetCard
import tech.framti.common.ui.widget.BackButton
import tech.framti.common.ui.widget.CamlFilledButton
import tech.framti.theme.whiteBg

@Composable
fun ScratchViewRoot(viewModel: ScratchViewModel = hiltViewModel()) {
    ScratchView(viewModel.state.value, viewModel.loadingState.value) { event ->
        viewModel.onViewEvent(event)
    }
}

@Composable
private fun ScratchView(
    viewState: UiState,
    isLoading: Boolean,
    onEvent: (event: UiEvent) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(whiteBg)
    ) {
        BackButton() {
            onEvent.invoke(UiEvent.NavigateBack)
        }
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            AssetCard(serialNumber = viewState.code ?: stringResource(R.string.main_view_generate_uuid))

            Spacer(modifier = Modifier.height(16.dp))
            CamlFilledButton(
                text = stringResource(R.string.main_view_generate_uuid),
                isLoading = isLoading
            ) {
                onEvent.invoke(UiEvent.GenerateNewCode)
            }
        }
    }
}

@Composable
@Preview
private fun ScratchPreview() {
    ScratchView(UiState(), false) {}
}