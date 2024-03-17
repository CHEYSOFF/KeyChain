package vk.cheysoff.keychain.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "appdata")
data class AppDataEntity(
    @PrimaryKey(autoGenerate = false) val id: Int = 0,
    val hashedMasterKey: String,
    val isBiometricEnabled: Boolean
)