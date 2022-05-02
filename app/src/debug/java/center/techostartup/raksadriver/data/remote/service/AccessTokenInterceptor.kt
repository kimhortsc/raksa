package center.techostartup.raksadriver.data.remote.service

import android.util.Log
import center.techostartup.raksadriver.utils.AppConstants
import okhttp3.Interceptor
import okhttp3.Response

class AccessTokenInterceptor(private val accessToken: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        return run {
            val authenticatedRequest = chain.request()
                .newBuilder()
                .header(AppConstants.AUTHORIZATION_HEADER, "Bearer $accessToken")
                .build()
            chain.proceed(authenticatedRequest)
        }
    }
}