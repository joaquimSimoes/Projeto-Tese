package com.android.example.teseproject.ExtraFunctions

object TouchCounter {
    private var count = 0

    fun increment(){
        count ++
    }

    fun reset(){
        count = 0
    }

    fun getCount(): Int = count
}