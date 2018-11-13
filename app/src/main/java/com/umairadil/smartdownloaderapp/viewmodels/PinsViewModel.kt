package com.umairadil.smartdownloaderapp.viewmodels

import androidx.lifecycle.ViewModel
import com.umairadil.smartdownloaderapp.data.Pins
import com.umairadil.smartdownloaderapp.data.PinsResponse
import com.umairadil.smartdownloaderapp.data.network.RestClient
import io.reactivex.Observable

class PinsViewModel : ViewModel() {

    private var apiClient = RestClient.apiService()

    fun getBoardPins(): Observable<List<PinsResponse>> {
        return apiClient.getBoardPins()
    }

    fun mapPinsResponse(response: List<PinsResponse>?): ArrayList<Pins> {
        val pins = arrayListOf<Pins>()

        response?.let {

            it.forEach { res ->
                val pin = Pins(res.id)

                //Set Image URL
                res.urls?.regular?.let {
                    pin.url = it
                }

                //Set User Name
                res.user?.name?.let {
                    pin.user_name = it
                }

                //Set User profile Image
                res.user?.profile_image?.small?.let {
                    pin.user_profile_image = it
                }

                //Add to list
                pins.add(pin)
            }
        }

        return pins
    }
}