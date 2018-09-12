package org.string.employees

sealed class Result
data class Success(val success: Boolean = true): Result()
data class Error(val exception: Exception): Result()