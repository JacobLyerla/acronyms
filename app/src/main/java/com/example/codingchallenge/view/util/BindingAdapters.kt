package com.example.codingchallenge.view.util

import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.acronym.data.util.AcronymResult
import com.example.codingchallenge.view.AcronymScreenState
import com.example.codingchallenge.view.LongformAdapter

@BindingAdapter("android:updateState")
fun RecyclerView.updateState(state: AcronymScreenState) {
    val longForms = when (val response = state.response) {
        is AcronymResult.Success -> response.data.map { it.lf }
        else -> emptyList()
    }
    adapter?.apply {
        (this as LongformAdapter).submitList(longForms)
    }
}
@BindingAdapter("onSearchClick")
fun TextView.setOnSearchClick(onSearchClick: () -> Unit) {
    imeOptions = EditorInfo.IME_ACTION_SEARCH
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            onSearchClick.invoke()
            true
        } else {
            false
        }
    }
}
