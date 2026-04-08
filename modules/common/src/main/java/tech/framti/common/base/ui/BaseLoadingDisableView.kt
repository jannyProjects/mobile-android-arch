package tech.framti.common.base.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager

@Composable
fun BaseLoadingDisableView(
    modifier: Modifier = Modifier,
    contentEnabled: Boolean,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        content.invoke()

        if (!contentEnabled) {
            val focusManager = LocalFocusManager.current

            LaunchedEffect(Unit) {
                focusManager.clearFocus()
            }
            // Full-screen transparent overlay that eats clicks
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(androidx.compose.ui.graphics.Color.Transparent)
                    .clickable(
                        enabled = true,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        // Do nothing, just consume clicks
                    }
            )
        }
    }
}