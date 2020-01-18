package ua.turskyi.rxjavawithroom.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import retrofit2.HttpException
import ua.turskyi.rxjavawithroom.data.web.repository.CountriesRxRepository

class RefreshDataWorker(appContext: Context, params: WorkerParameters):
        CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    /**
     * A coroutine-friendly method to do your work.
     */
    override suspend fun doWork(): Result {
        val repository = CountriesRxRepository()
        return try {
            repository.refreshCountries()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}
