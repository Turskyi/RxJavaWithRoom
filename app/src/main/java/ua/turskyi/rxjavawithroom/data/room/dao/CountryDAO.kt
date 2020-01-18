package ua.turskyi.rxjavawithroom.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable
import ua.turskyi.rxjavawithroom.data.entity.Country

@Dao
interface CountryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(countries: List<Country>?)

    @Query("SELECT * FROM ${Country.TABLE_NAME}")
    fun getRxLiveAll(): Flowable<List<Country>>
}