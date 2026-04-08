package tech.framti.common.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.framti.common.R
import tech.framti.domain.model.AuthState
import tech.framti.theme.green
import tech.framti.theme.warning

@Composable
fun StatusBadge(authState: AuthState) {
    val badgeText = when (authState) {
        AuthState.UNSCRATCHED -> stringResource(R.string.main_view_status_unscratched)
        AuthState.SCRATCHED -> stringResource(R.string.main_view_status_scratched)
        AuthState.ACTIVATED -> stringResource(R.string.main_view_status_activated)
    }
    val badgeColor = when (authState) {
        AuthState.UNSCRATCHED -> Color(0xFF00E5CC)
        AuthState.SCRATCHED -> warning
        AuthState.ACTIVATED -> green
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(badgeColor)
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Text(
            text = badgeText,
            color = Color(0xFF1A1A2E),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

