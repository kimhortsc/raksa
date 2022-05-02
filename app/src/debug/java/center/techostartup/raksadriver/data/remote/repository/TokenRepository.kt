package center.techostartup.raksadriver.data.remote.repository

import android.app.Application
import center.techostartup.raksadriver.data.remote.request.RefreshTokenRequest
import center.techostartup.raksadriver.data.remote.response.NewTokenResponse
import center.techostartup.raksadriver.data.remote.service.RetrofitClient
import center.techostartup.raksadriver.data.remote.service.TokenService
import retrofit2.Call

object TokenRepository {
    private val TAG = TokenRepository::class.simpleName

    fun refreshToken(application: Application, refreshTokenRequest: RefreshTokenRequest): Call<NewTokenResponse> {
        val tokenService = RetrofitClient(application).createService(TokenService::class.java)

        return tokenService.refreshToken(refreshTokenRequest)
    }
}