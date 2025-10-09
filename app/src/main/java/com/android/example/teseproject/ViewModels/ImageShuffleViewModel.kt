package com.android.example.teseproject.ViewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.android.example.teseproject.R

class ImageShuffleViewModel : ViewModel() {
    private val _imageList = mutableStateListOf<Int>()
    val imageList : SnapshotStateList<Int> get() = _imageList

    init {
        shuffleImages()
    }

    fun shuffleImages() {
        _imageList.clear()
        _imageList.addAll(
            listOf(
                R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4,
                R.drawable.image5, R.drawable.image6, R.drawable.image7, R.drawable.image23,
                R.drawable.image9_jpg, R.drawable.image10, R.drawable.image11, R.drawable.image12,
                R.drawable.image13, R.drawable.image14, R.drawable.image15, R.drawable.image18,
                R.drawable.image19, R.drawable.image20, R.drawable.image25, R.drawable.image22
            ).shuffled()
        )
    }
}