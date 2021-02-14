package com.example.googlebooksapi.extensions

import com.example.googlebooksapi.database.EmptyDataException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

fun <T> Flow<T>.convertToFlowViewState(
    dispatcher: CoroutineDispatcher = Dispatchers.Default
): Flow<ViewState<T>> {
    return this
        .map { list -> ViewState(status = Status.SUCCESS, data = list) }
        .catch { cause: Throwable -> emitAll(flowOf(ViewState(Status.ERROR, error = cause))) }
        .flowOn(dispatcher)
}

fun <T> Flow<List<T>>.convertToFlowListViewState(
    dispatcher: CoroutineDispatcher = Dispatchers.Default
): Flow<ViewState<List<T>>> {
    return this
        .map { list ->
            if (list.isNullOrEmpty()) {
                throw EmptyDataException("Data is empty")
            } else {
                ViewState(status = Status.SUCCESS, data = list)
            }
        }
        .catch { cause: Throwable -> emitAll(flowOf(ViewState(Status.ERROR, error = cause))) }
}
