package tech.framti.common.ui.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.framti.theme.colorMain

@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(
        debounceCompose(
            debounceTime = CLICK_DEBOUNCE_TIME,
            action = { onClick() }),
        modifier = modifier.padding(start = 8.dp),
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBackIosNew,
            contentDescription = "back button",
            tint = colorMain,
        )
    }
}

@Preview
@Composable
private fun Preview() {
    Column {
        BackButton() { }
    }
}