package center.techostartup.raksadriver.data.remote.service

import center.techostartup.raksadriver.data.model.Driver
import center.techostartup.raksadriver.data.remote.request.TokenRequest
import center.techostartup.raksadriver.data.remote.response.NewTokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DriverService {

    @GET("driver/profile-info/")
    fun driver(): Call<Driver>

    @POST("driver/firebase/login-phone")
    fun phoneLogin(@Body tokenResponse: TokenRequest): Call<NewTokenResponse>
}