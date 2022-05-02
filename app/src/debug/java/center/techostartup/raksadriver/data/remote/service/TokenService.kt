package center.techostartup.raksadriver.data.remote.service

import center.techostartup.raksadriver.data.remote.request.RefreshTokenRequest
import center.techostartup.raksadriver.data.remote.response.NewTokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenService {

    @POST("token/refresh")
    fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest): Call<NewTokenResponse>
}