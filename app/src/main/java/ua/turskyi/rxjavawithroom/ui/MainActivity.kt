package ua.turskyi.rxjavawithroom.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import ua.turskyi.rxjavawithroom.data.entity.Country
import ua.turskyi.rxjavawithroom.data.room.CountriesDataBase
import ua.turskyi.rxjavawithroom.view.CountriesAdapter
import ua.turskyi.rxjavawithroom.viewmodel.CountriesViewModel
import ua.turskyi.rxjavawithroom.R

/**
 * The app shows a list of all countries in the world using https://restcountries.eu/
 */
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var countriesViewModel: CountriesViewModel
    private lateinit var adapter: CountriesAdapter
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        countriesViewModel = ViewModelProviders.of(this).get(CountriesViewModel::class.java)
        setUpTheView()
        getOfflineCountries()
    }

    private fun setUpTheView() {
        adapter = CountriesAdapter { country ->
            countriesViewModel.country.postValue(country)
        }
        val layoutManager = LinearLayoutManager(this)
        rv.layoutManager = layoutManager
        rv.adapter = adapter
    }

    private fun getOfflineCountries() {
        val disposable = countriesViewModel.countriesFromRxDB2?.observeOn(Schedulers.io())
                ?.observeOn(Schedulers.io())
                ?.subscribe({ countries ->
                    updateAdapter(countries)
                }, { throwable ->
                    Timber.d(throwable, "error :(")
                })
        disposable?.let { compositeDisposable.add(it) }
    }

    private fun updateAdapter(countries: List<Country>) {
        adapter.setData(countries)
        toolbarTitle.text = getString(R.string.num_of_countries, countries.size.toString())
        adapter.visibilityLoader.observe(this, Observer { currentVisibility ->
            pb.visibility = currentVisibility
        })
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        CountriesDataBase.destroyInstance()
        super.onDestroy()
    }
}
