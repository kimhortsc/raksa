package center.techostartup.raksadriver.data.remote.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import center.techostartup.raksadriver.data.local.AppSharedPreference
import center.techostartup.raksadriver.data.model.Driver
import center.techostartup.raksadriver.data.remote.request.TokenRequest
import center.techostartup.raksadriver.data.remote.response.NewTokenResponse
import center.techostartup.raksadriver.data.remote.service.DriverService
import center.techostartup.raksadriver.data.remote.service.RetrofitClient
import center.techostartup.raksadriver.utils.AppConstants

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DriverRepository(val application: Application) {
    private val TAG = DriverRepository::class.java.simpleName

    fun driver(): MutableLiveData<Driver> {
        val driver: MutableLiveData<Driver> = MutableLiveData()

        val driverService: DriverService = RetrofitClient(application).createServiceWithToken(DriverService::class.java)


        Log.d(TAG, "DriverRepository - driver()")

        driverService.driver().enqueue(object: Callback<Driver> {
            override fun onResponse(call: Call<Driver>, response: Response<Driver>) {
                if(response.isSuccessful){
                    driver.value = response.body()
                }
            }

            override fun onFailure(call: Call<Driver>, t: Throwable) {
                Log.d(TAG, "Failed")
            }
        })

        return driver
    }

    fun phoneLogin(tokenRequest: TokenRequest): MutableLiveData<NewTokenResponse> {
        val liveData = MutableLiveData<NewTokenResponse>()


        val driverService: DriverService = RetrofitClient(application).createService(DriverService::class.java)


        driverService.phoneLogin(tokenRequest).enqueue(object : Callback<NewTokenResponse> {
            override fun onResponse(
                call: Call<NewTokenResponse>,
                response: Response<NewTokenResponse>
            ) {
                if(response.isSuccessful){
                    val token = response.body()
                    liveData.postValue(token) //= response.body()
                    Log.e(TAG, "onResponse: ${liveData.value?.access}")

                    val sharedPreference = AppSharedPreference(application)
                    token?.access?.let { sharedPreference.insert(AppConstants.ACCESS_TOKEN, it) }
                    token?.refresh?.let { sharedPreference.insert(AppConstants.REFRESH_TOKEN, it) }
                }

            }

            override fun onFailure(call: Call<NewTokenResponse>, t: Throwable) {
                Log.d(TAG, t.printStackTrace().toString())
                Log.e(TAG, "onFailure: ")
            }
        })

        return liveData
    }
}