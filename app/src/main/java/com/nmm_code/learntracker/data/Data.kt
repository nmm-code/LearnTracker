package com.nmm_code.learntracker.data

import android.content.Context

interface Data<T> {
    fun read(context: Context): List<T>
    fun save(context: Context,list: List<T>)
}

