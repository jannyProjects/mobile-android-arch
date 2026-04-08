package tech.framti.common.ui.widget

import android.os.SystemClock
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.framti.theme.CamlTypography
import tech.framti.theme.base
import tech.framti.theme.buttonDisabled
import tech.framti.theme.textButton
import tech.framti.theme.whiteBg

const val CLICK_DEBOUNCE_TIME = 300L

@Composable
fun CamlFilledButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector? = null,
    text: String,
    buttonColors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = base,
        contentColor = textButton,
        disabledContentColor = textButton,
        disabledContainerColor = buttonDisabled
    ),
    enabled: Boolean = true,
    isLoading: Boolean = false,
    conditional: () -> Boolean = { true },
    onClick: () -> Unit
) {
    DebounceClickButton(
        onClick = onClick,
        enabled = enabled && !isLoading,
        conditional = conditional,
        modifier = modifier.sizeIn(minHeight = 50.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        colors = buttonColors,
        shape = RoundedCornerShape(16.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = whiteBg,
                strokeWidth = 2.dp
            )
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                imageVector?.let {
                    Icon(imageVector, null, tint = buttonColors.contentColor)
                    Spacer(Modifier.size(4.dp))
                }
                Text(
                    text = text,
                    style = CamlTypography.current.buttonText,
                    color = buttonColors.contentColor
                )
            }

        }
    }
}

@Composable
fun DebounceClickButton(
    onClick: () -> Unit,
    modifier: Modifier,
    conditional: () -> Boolean = { true },
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = debounceCompose(
            debounceTime = CLICK_DEBOUNCE_TIME,
            action = { if (conditional()) onClick() }),
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        elevation = elevation,
        shape = shape,
        border = border,
        colors = colors,
        contentPadding = contentPadding,
        content = content
    )
}

@Composable
fun debounceCompose(debounceTime: Long = 500L, action: () -> Unit): () -> Unit {
    val debouncedAction = remember(action) {
        var lastClickTime: Long = 0
        {
            if (SystemClock.elapsedRealtime() - lastClickTime >= debounceTime) {
                lastClickTime = SystemClock.elapsedRealtime()
                action()
            }
        }
    }
    return debouncedAction
}

@Preview
@Composable
private fun Preview() {
    Column {
        CamlFilledButton(text = "Continue", conditional = { true }, onClick = { }, enabled = false)
        CamlFilledButton(
            text = "Continue",
            imageVector = Icons.Default.Directions,
            conditional = { true },
            onClick = { },
            enabled = false
        )
        CamlFilledButton(
            text = "Continue",
            conditional = { true },
            onClick = { },
            enabled = true,
            isLoading = true
        )
    }
}