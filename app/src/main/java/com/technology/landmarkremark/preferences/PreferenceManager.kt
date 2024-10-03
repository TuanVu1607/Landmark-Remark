package com.technology.landmarkremark.preferences

import android.content.Context
import com.google.gson.Gson
import com.technology.landmarkremark.data.entity.UserEntity
import javax.inject.Inject

class PreferenceManager @Inject constructor(context: Context) : SharedPreferences(context),
    Preferences {
    override fun getPrefName() = PreferencesConstants.KEYSTORE_NAME

    override fun getAccessToken(): String {
        val accessToken = preferences.getString(PreferencesConstants.KEY_ACCESS_TOKEN, "")
        return if (!accessToken.isNullOrEmpty()) {
            accessToken
        } else {
            ""
        }
    }

    override fun setAccessToken(accessToken: String) {
        preferences.edit().putString(PreferencesConstants.KEY_ACCESS_TOKEN, accessToken).apply()
    }

    override fun deleteAccessToken() {
        preferences.edit().remove(PreferencesConstants.KEY_ACCESS_TOKEN).apply()
    }

    override fun getUserData(): UserEntity {
        val userDataCache = preferences.getString(PreferencesConstants.KEY_USER_DATA, "")
        return if (!userDataCache.isNullOrEmpty()) {
            Gson().fromJson(userDataCache, UserEntity::class.java)
        } else {
            UserEntity()
        }
    }

    override fun setUserData(userData: UserEntity) {
        preferences.edit().putString(PreferencesConstants.KEY_USER_DATA, Gson().toJson(userData))
            .apply()
    }

    override fun deleteUserData() {
        preferences.edit().remove(PreferencesConstants.KEY_USER_DATA).apply()
    }

    override fun isLogin() = getUserData().id > 0
}