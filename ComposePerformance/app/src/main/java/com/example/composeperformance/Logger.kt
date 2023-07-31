package com.example.composeperformance

import android.util.Log

object Logger {
    private const val TAG = "ComposePerformance"

    fun d(message: String, filter: LogFilter? = null) {
        Log.d("$TAG-${filter ?: ""}", message)
    }
}

enum class LogFilter {
    ReAllocation,
    Recomposition
}