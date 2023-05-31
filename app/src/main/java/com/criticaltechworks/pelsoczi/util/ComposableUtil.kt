package com.criticaltechworks.pelsoczi.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

/**
 * Invoke something exactly once. [LaunchedEffect], [SideEffect], [DisposableEffect], are all
 * invoked when there is configuration change (rotating the phone from portrait to landscape).
 */
@Composable
fun RememberSaveableEffect(
    block: () -> Unit
) {
    var invoked by rememberSaveable { mutableStateOf(false) }
    if (invoked.not()) {
        block()
        invoked = true
    }
}