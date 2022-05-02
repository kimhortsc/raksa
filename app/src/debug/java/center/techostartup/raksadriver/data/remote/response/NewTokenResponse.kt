package center.techostartup.raksadriver.data.remote.response

import com.google.gson.annotations.SerializedName

data class NewTokenResponse(
    @SerializedName("access")
    val access: String,

    @SerializedName("refresh")
    val refresh: String
)