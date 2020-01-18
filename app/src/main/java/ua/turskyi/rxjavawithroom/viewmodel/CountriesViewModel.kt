package ua.turskyi.rxjavawithroom.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ua.turskyi.rxjavawithroom.data.entity.Country
import ua.turskyi.rxjavawithroom.data.room.CountriesDataBase
import ua.turskyi.rxjavawithroom.data.web.repository.CountriesRxRepository
import ua.turskyi.rxjavawithroom.utils.isOnline

class CountriesViewModel(application: Application): AndroidViewModel(application) {

    /**
     * This is the job for all coroutines started by this ViewModel.
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = SupervisorJob()

    /**
     * This is the main scope for all coroutines launched by MainViewModel.
     * Since viewModelJob is passed, all coroutines launched by uiScope can be canceled by calling
     * viewModelJob.cancel()
     */
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _country = MutableLiveData<Country>()
    val country: MutableLiveData<Country>
        get() = _country

    private val database = CountriesDataBase.getInstance(application)
        val countriesFromRxDB2 = database?.countryDAO()?.getRxLiveAll()
    private val countriesRepository = CountriesRxRepository()
    private val compositeDisposable = CompositeDisposable()

    init {
        viewModelScope.launch {
            if (isOnline()) countriesRepository.refreshCountries()
        }
    }

    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        compositeDisposable.dispose()
    }
}



