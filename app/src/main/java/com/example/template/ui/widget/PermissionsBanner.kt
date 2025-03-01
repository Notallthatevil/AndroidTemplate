package com.example.template.ui.widget

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.template.ui.PreviewDefault
import com.example.template.ui.theme.AppTheme
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@Composable
fun PermissionsBanner(
    text: String,
    permissions: List<String>,
    modifier: Modifier = Modifier,
    showSystemSettings: () -> Unit,
    onPermissionStatusChanged: ((PermissionStatus) -> Unit)? = null,
) {
    val permissionState = rememberMultiplePermissionsState(permissions)

    LaunchedEffect(permissionState.allPermissionsGranted) {
        val sb = StringBuilder()
        permissionState.permissions.forEach {
            sb.appendLine("${it.permission}: ${it.status}")
        }

        Log.i(
            "SMT", """allPermissionsGranted: ${permissionState.allPermissionsGranted}
            |$sb
        """.trimMargin()
        )
//        onPermissionStatusChanged?.invoke(permissionState.allPermissionsGranted)
    }

    if (!permissionState.allPermissionsGranted) {
        // If permission denied previously (shouldShowRationale == true) AND !isGranted:  This
        // permission is now in a "Blocked" or "Never ask again"
        // state (prompt will no longer work)
        val permissionBlocked = permissionState.shouldShowRationale

        Banner(modifier = modifier, text = text, onAllow = {
            // "Allow" was clicked...
            if (!permissionBlocked) {
                // Prompt the user in context
                permissionState.launchMultiplePermissionRequest()
            } else {
                // deep link intent right into the settings
                showSystemSettings()
            }
        })

        // check to see if we should prompt now
        if (!permissionBlocked) {
            SideEffect {
                permissionState.launchMultiplePermissionRequest()
            }
        }
    }
}

@Composable
private fun Banner(
    text: String,
    modifier: Modifier = Modifier,
    onAllowText: String = "Allow",
    onClick: () -> Unit = {},
    onAllow: (() -> Unit)? = null
) {
    Surface {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.Warning,
                contentDescription = null,
                modifier = Modifier.padding(end = 16.dp),
            )
            Text(
                modifier = Modifier
                    .weight(1f),
                text = text
            )
            onAllow?.let {
                TextButton(
                    onClick = it
                ) {
                    Text(text = onAllowText.uppercase(), color = AppTheme.colors.primary)
                }
            }
        }
    }
}

@PreviewDefault
@Composable
private fun BannerPreview() {
    AppTheme {
        Banner(
            text = "Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Donec velit neque, auctor sit amet aliquam vel, ullamcorper sit amet ligula.",
            onAllow = {},
        )
    }
}