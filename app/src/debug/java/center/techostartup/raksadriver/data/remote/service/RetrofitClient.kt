package center.techostartup.raksadriver.data.remote.service

import android.app.Application
import center.techostartup.raksadriver.data.local.AppSharedPreference
import center.techostartup.raksadriver.utils.AppConstants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient(val application: Application) {

    private val retrofit = Retrofit.Builder()
        .baseUrl(AppConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())


    fun <S> createService(serviceClass: Class<S>): S {
        val build = retrofit.build()
        return build.create(serviceClass)
    }

    fun <S> createServiceWithToken(serviceClass: Class<S>): S {
        val build = retrofit.client(okHttpClient()).build()
        return build.create(serviceClass)
    }

    private fun okHttpClient(): OkHttpClient {
        val sharedPreference = AppSharedPreference(application)

        return OkHttpClient.Builder()
            .addInterceptor(AccessTokenInterceptor(sharedPreference.get(AppConstants.ACCESS_TOKEN)))
           // .authenticator(AccessTokenAuthenticator(application))
            .build()

    }
}