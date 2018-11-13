package com.umairadil.smartdownloaderapp.data.network

import com.umairadil.smartdownloaderapp.data.PinsResponse
import io.reactivex.Observable
import retrofit2.http.GET

interface API {

    @GET("raw/wgkJgazE")
    fun getBoardPins(): Observable<List<PinsResponse>>
}