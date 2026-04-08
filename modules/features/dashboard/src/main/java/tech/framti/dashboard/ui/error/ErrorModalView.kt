package tech.framti.dashboard.ui.error

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import tech.framti.common.R
import tech.framti.common.ui.widget.CamlFilledButton
import tech.framti.theme.CamlTypography
import tech.framti.theme.whiteBg

@Composable
fun ErrorModalViewRoot(
    @StringRes title: Int,
    @StringRes subtitle: Int,
    viewModel: ErrorModalViewModel = hiltViewModel()
) {
    ErrorModalView(
        title = stringResource(title),
        subtitle = stringResource(subtitle),
        onDismiss = { viewModel.onViewEvent(UiEvent.Dismiss) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ErrorModalView(
    title: String,
    subtitle: String,
    onDismiss: () -> Unit
) {
    BasicAlertDialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = whiteBg,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = CamlTypography.current.h2
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = subtitle,
                    style = CamlTypography.current.paragraph
                )

                Spacer(modifier = Modifier.height(24.dp))

                CamlFilledButton(
                    text = stringResource(R.string.general_ok),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onDismiss
                )
            }
        }
    }
}

@Composable
@Preview
private fun ErrorModalPreview() {
    ErrorModalView(
        title = "Something went wrong",
        subtitle = "Please contact customer support.",
        onDismiss = {}
    )
}

