package com.manish.teachmintassignment.utils

import android.content.Context
import androidx.compose.runtime.MutableState
import com.manish.teachmintassignment.R
import com.manish.teachmintassignment.data.remote.NetworkResult

class CommonFunUtils {
}

fun <T> NetworkResult<T?>.handleResponse(
    context: Context,
    progress: MutableState<Boolean>,
    onErrorMsg: String?,
    showProgress: Boolean = false,
    onSuccess: () -> Unit
) {
    val errMsg = context.getString(R.string.server_unreachable)
    when (this) {
        is NetworkResult.Error -> {
            progress.value = false
            context.showToast(onErrorMsg ?: errMsg)
        }

        is NetworkResult.Loading -> {
            progress.value = true
        }

        is NetworkResult.Success -> {
            progress.value = showProgress
            onSuccess.invoke()
        }
    }
}