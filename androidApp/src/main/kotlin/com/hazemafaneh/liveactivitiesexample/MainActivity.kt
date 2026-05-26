package com.hazemafaneh.liveactivitiesexample

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                PostNotificationsGate {
                    App()
                }
            }
        }
    }
}

/**
 * Requests `POST_NOTIFICATIONS` on API 33+ before letting the demo run.
 *
 * Why this lives in MainActivity rather than the shared Compose tree: the runtime permission
 * APIs are Android-only. We block the rest of the demo until the user grants the permission so
 * `LiveActivityManager.start()` never fails its `areNotificationsEnabled()` precheck. If the
 * user denies, a Snackbar offers to re-request.
 */
@Composable
private fun PostNotificationsGate(content: @Composable () -> Unit) {
    // Pre-Tiramisu doesn't need the permission at all — pass straight through.
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        content()
        return
    }

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var status by remember {
        val initial = if (
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            PermissionStatus.Granted
        } else {
            PermissionStatus.Pending
        }
        mutableStateOf(initial)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        status = if (isGranted) PermissionStatus.Granted else PermissionStatus.Denied
    }

    LaunchedEffect(Unit) {
        if (status == PermissionStatus.Pending) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    LaunchedEffect(status) {
        if (status == PermissionStatus.Denied) {
            val result = snackbarHostState.showSnackbar(
                message = "This demo needs notification permission to post Live Updates.",
                actionLabel = "Grant",
                withDismissAction = true,
            )
            if (result == SnackbarResult.ActionPerformed) {
                status = PermissionStatus.Pending
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data -> Snackbar(snackbarData = data) }
        },
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (status == PermissionStatus.Granted) content()
        }
    }
}

private enum class PermissionStatus { Pending, Granted, Denied }

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
