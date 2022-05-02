package center.techostartup.raksadriver.view.driverprofile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import center.techostartup.raksadriver.data.model.Driver
import center.techostartup.raksadriver.data.remote.repository.DriverRepository
import center.techostartup.raksadriver.data.remote.request.TokenRequest
import center.techostartup.raksadriver.data.remote.response.NewTokenResponse

class DriverViewModel(application: Application): AndroidViewModel(application) {

    fun driver(): LiveData<Driver> {
        return DriverRepository(getApplication()).driver()
    }

    fun phoneLogin(tokenRequest: TokenRequest): LiveData<NewTokenResponse>{
        return DriverRepository(getApplication()).phoneLogin(tokenRequest)
    }
}