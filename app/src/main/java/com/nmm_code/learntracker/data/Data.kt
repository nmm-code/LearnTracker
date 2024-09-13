package com.nmm_code.learntracker.data

interface Data<T> {


    fun read(): List<T>
    fun save(list: List<T>)
    fun update(idx: Int, newData: T)
    fun delete(idx: Int)

    fun count(): Int
}