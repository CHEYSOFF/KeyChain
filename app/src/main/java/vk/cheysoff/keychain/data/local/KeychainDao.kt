package vk.cheysoff.keychain.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import vk.cheysoff.keychain.data.local.entities.AppDataEntity
import vk.cheysoff.keychain.data.local.entities.PasswordEntity

@Dao
interface KeychainDao {

    @Query("SELECT * FROM passwords")
    suspend fun getAllPasswords() : List<PasswordEntity>

    @Query("SELECT * FROM passwords WHERE websiteUrl LIKE '%' || :query || '%'")
    suspend fun searchPasswords(query: String): List<PasswordEntity>

    @Query("SELECT * FROM appdata")
    suspend fun getAppData() : AppDataEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppData(appData: AppDataEntity)

    @Query("UPDATE appdata SET isBiometricEnabled = :isBiometricEnabled WHERE id = 0")
    suspend fun updateBiometricUsage(isBiometricEnabled: Boolean)

    @Query("UPDATE appdata SET hashedMasterKey = :hashedMasterKey WHERE id = 0")
    suspend fun updateMasterKeyHash(hashedMasterKey: String)

    @Query("SELECT * FROM passwords WHERE id = :id")
    suspend fun getPasswordById(id: Long) : PasswordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPassword(password: PasswordEntity): Long

    @Query("DELETE FROM passwords WHERE id = :id")
    suspend fun deletePassword(id: Long)
}