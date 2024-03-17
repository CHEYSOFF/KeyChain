package vk.cheysoff.keychain.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import vk.cheysoff.keychain.data.local.converters.Converters

@Entity(tableName = "passwords")
data class PasswordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    val websiteUrl: String,
    @TypeConverters(Converters::class)
    val loginCyphered: ByteArray,
    @TypeConverters(Converters::class)
    val passwordCyphered: ByteArray,
    val notes: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PasswordEntity

        if (id != other.id) return false
        if (websiteUrl != other.websiteUrl) return false
        if (!loginCyphered.contentEquals(other.loginCyphered)) return false
        return passwordCyphered.contentEquals(other.passwordCyphered)
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + websiteUrl.hashCode()
        result = 31 * result + loginCyphered.contentHashCode()
        result = 31 * result + passwordCyphered.contentHashCode()
        return result
    }
}
