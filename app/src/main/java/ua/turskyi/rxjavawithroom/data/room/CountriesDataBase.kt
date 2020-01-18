package ua.turskyi.rxjavawithroom.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ua.turskyi.rxjavawithroom.data.entity.Country
import ua.turskyi.rxjavawithroom.data.room.dao.CountryDAO

@Database(entities = [Country::class], version = 1, exportSchema = false)
abstract class CountriesDataBase : RoomDatabase() {

    abstract fun countryDAO(): CountryDAO

    companion object {

        private var INSTANCE: CountriesDataBase? = null

        fun getInstance(context: Context): CountriesDataBase? {
            synchronized(CountriesDataBase::class) {
                INSTANCE ?: run {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        CountriesDataBase::class.java,
                        "countries.db"
                    ).build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}