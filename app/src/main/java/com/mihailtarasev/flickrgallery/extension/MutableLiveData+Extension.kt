package com.mihailtarasev.flickrgallery.extension

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.trigger() {
    value = value
}