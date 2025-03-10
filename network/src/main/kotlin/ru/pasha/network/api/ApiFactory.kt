package ru.pasha.network.api

import retrofit2.Retrofit

class ApiFactory(val retrofit: Retrofit) {
    inline fun <reified T> create(): T = retrofit.create(T::class.java)
}
