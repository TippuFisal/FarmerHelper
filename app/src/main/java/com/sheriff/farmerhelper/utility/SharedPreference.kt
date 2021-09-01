package com.sheriff.farmerhelper.utility

import android.content.Context
import android.content.SharedPreferences

class SharedPreference {

    companion object {
        private val sharedPref = "sharedpreference"
        val USER_EMAIL: String = "USER_EMAIL"
        val USER_NAME: String = "USER_NAME"
        val USER_PASSWORD: String = "USER_PASSWORD"
        val USER_PHONE_NUMBER: String = "USER_PHONE_NUMBER"
        val USER_NAME_FIRST_LETTER: String = "USER_NAME_FIRST_LETTER"
        val USER_LOGIN_STATUS: String = "USER_LOGIN_STATUS"

        /**
         * Save preference manager is used to save data in SharedPreference Data
         *
         * @param key
         * @param value
         * @param context
         */
        fun savePreferenceManager(key: String, value: Any, context: Context) {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(sharedPref, Context.MODE_PRIVATE)
            when (value) {
                is Boolean -> sharedPreferences.edit().putBoolean(key, value).apply()
                is Int -> sharedPreferences.edit().putInt(key, value).apply()
                is Long -> sharedPreferences.edit().putLong(key, value).apply()
                is Float -> sharedPreferences.edit().putFloat(key, value).apply()
                is String -> sharedPreferences.edit().putString(key, value).apply()
                is Set<*> -> {
                    for ((index, item) in value.withIndex()) {
                        sharedPreferences.edit().putString(key + "_$index", item.toString()).apply()
                    }
                }
                else -> {
                    sharedPreferences.edit().putString(key, value.toString()).apply()
                }
            }
        }

        /**
         * Retrive preference manager is used to get data in SharedPreference, which we stored.
         *
         * @param key
         * @param context
         * @return
         */
        fun retrivePreferenceManager(key: String, context: Context): Any {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(sharedPref, Context.MODE_PRIVATE)
            return if (sharedPreferences.all.containsKey(key)) {
                sharedPreferences.all[key] as Any
            } else {
                key
            }
        }

        /**
         * Clear preference manager is used to clear all data form SharedPreference, which we stored.
         *
         * @param context
         */
        fun clearPreferenceManager(context: Context) {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(sharedPref, Context.MODE_PRIVATE)
            sharedPreferences.edit().clear().apply()
        }

    }

}