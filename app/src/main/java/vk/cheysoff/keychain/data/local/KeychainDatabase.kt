package vk.cheysoff.keychain.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import vk.cheysoff.keychain.data.local.converters.Converters
import vk.cheysoff.keychain.data.local.entities.AppDataEntity
import vk.cheysoff.keychain.data.local.entities.PasswordEntity

@Database(
    entities = [PasswordEntity::class, AppDataEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class KeychainDatabase: RoomDatabase() {

    abstract val dao: KeychainDao
}