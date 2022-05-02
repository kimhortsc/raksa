package center.techostartup.raksadriver.data.remote.service

import android.app.Application
import android.util.Log
import center.techostartup.raksadriver.data.local.AppSharedPreference
import center.techostartup.raksadriver.data.remote.repository.TokenRepository
import center.techostartup.raksadriver.data.remote.request.RefreshTokenRequest
import center.techostartup.raksadriver.utils.AppConstants
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class AccessTokenAuthenticator(val application: Application) : Authenticator {

    private val TAG = AccessTokenAuthenticator::class.simpleName

    override fun authenticate(route: Route?, response: Response): Request? {
        try {

            val sharedPreference = AppSharedPreference(application)

            val refreshToken = sharedPreference.get(AppConstants.REFRESH_TOKEN)

            // make request to get new access token and new refresh token
            val call = TokenRepository.refreshToken(application, RefreshTokenRequest(refreshToken)).execute()

            val newToken = call.body()

            // save new tokens in app shared preference
            newToken?.refresh?.let { sharedPreference.insert(AppConstants.REFRESH_TOKEN, it) }
            newToken?.access?.let { sharedPreference.insert(AppConstants.ACCESS_TOKEN, it) }

            // pass new access token to request method
            return newToken?.let { request(response, it.access) }

        } catch (exception: Exception) {
            Log.d(TAG, exception.stackTrace.toString())
            return null
        }
    }

    private fun request(response: Response, token: String): Request? {
        return response.request()
            .newBuilder()
            .header(AppConstants.AUTHORIZATION_HEADER, "Bearer $token")
            .build()
    }
}