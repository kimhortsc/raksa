package center.techostartup.raksadriver.data.model

import com.google.gson.annotations.SerializedName

data class Driver(
    @SerializedName("id")
    val id: Int?,

    @SerializedName("username")
    val username: String?,

    @SerializedName("first_name")
    val firstName: String?,

    @SerializedName("last_name")
    val lastName: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("gender")
    val gender: String?,

    @SerializedName("photo")
    val photo: String?,

    @SerializedName("photo_thumbnail")
    val photoThumbnail: String?,

    @SerializedName("language")
    val language: String?,

    @SerializedName("is_active")
    val active: Boolean?,

    @SerializedName("date_joined")
    val dateJoined: String?,

    @SerializedName("last_login")
    val lastLogin: String?,

    @SerializedName("driver_profile")
    val driverProfile: DriverProfile?
)