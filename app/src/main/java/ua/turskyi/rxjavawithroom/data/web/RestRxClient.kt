package ua.turskyi.rxjavawithroom.data.web

import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ua.turskyi.rxjavawithroom.data.web.api.CountriesRxAPI
import ua.turskyi.rxjavawithroom.utils.Constants.API_BASE_URL

class RestRxClient {
    private val _countriesRxAPI: CountriesRxAPI
    val countriesRxAPI: CountriesRxAPI
        get() = _countriesRxAPI

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
        _countriesRxAPI = retrofit.create(CountriesRxAPI::class.java)
    }
}