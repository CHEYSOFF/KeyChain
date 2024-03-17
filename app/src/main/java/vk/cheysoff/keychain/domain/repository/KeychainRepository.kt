package vk.cheysoff.keychain.domain.repository

import vk.cheysoff.keychain.domain.model.PasswordModel
import vk.cheysoff.keychain.domain.util.Resource

interface KeychainRepository {

    suspend fun getPasswords(): Resource<List<PasswordModel>>

    suspend fun searchPasswords(query: String): Resource<List<PasswordModel>>

    suspend fun getPasswordById(id: Long): Resource<PasswordModel?>

    suspend fun insertPassword(passwordModel: PasswordModel): Resource<Long>

    suspend fun deletePassword(id: Long): Resource<Unit>
}