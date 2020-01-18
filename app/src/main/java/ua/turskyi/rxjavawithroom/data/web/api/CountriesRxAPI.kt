package ua.turskyi.rxjavawithroom.data.web.api

import io.reactivex.Observable
import retrofit2.http.GET
import ua.turskyi.rxjavawithroom.data.entity.Country

interface CountriesRxAPI {
    @GET("rest/v2/all")
    fun getCountriesFromApi(): Observable<List<Country>>
}