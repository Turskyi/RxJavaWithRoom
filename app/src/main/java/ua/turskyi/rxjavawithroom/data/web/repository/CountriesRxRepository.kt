package ua.turskyi.rxjavawithroom.data.web.repository

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import ua.turskyi.rxjavawithroom.CountriesApplication
import ua.turskyi.rxjavawithroom.data.entity.Country
import ua.turskyi.rxjavawithroom.data.room.CountriesDataBase
import ua.turskyi.rxjavawithroom.data.web.RestRxClient

class CountriesRxRepository {
    private val countriesRxAPI = RestRxClient().countriesRxAPI

    private fun getCountriesFromApi(): Observable<List<Country>> {
        return countriesRxAPI.getCountriesFromApi()
    }

    suspend fun refreshCountries() {
        withContext(Dispatchers.IO) {
            val countriesAPI = getCountriesFromApi()
            val compositeDisposable = CompositeDisposable()
            val database =
                CountriesDataBase.getInstance(CountriesApplication.instance.applicationContext)
            val disposable = countriesAPI.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({ countries ->
                    database?.countryDAO()?.insertAll(countries)
                }, { throwable ->
                    Timber.d(throwable, "error :(")
                })
            compositeDisposable.add(disposable)
        }
    }
}