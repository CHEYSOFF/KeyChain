package vk.cheysoff.keychain.data.local.converters

import android.util.Base64
import androidx.room.TypeConverter

object Converters {
    @TypeConverter
    fun byteArrayToString(byteArray: ByteArray): String {
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    @TypeConverter
    fun stringToByteArray(string: String): ByteArray {
        return Base64.decode(string, Base64.DEFAULT)
    }
}