package vk.cheysoff.keychain.data.local

import androidx.sqlite.db.SupportSQLiteOpenHelper
import net.sqlcipher.database.SupportFactory

class SQLCipherHelperFactory(passphrase: ByteArray) : SupportSQLiteOpenHelper.Factory {
    private val factory: SupportFactory = SupportFactory(passphrase)
    override fun create(configuration: SupportSQLiteOpenHelper.Configuration): SupportSQLiteOpenHelper {
        return factory.create(configuration)
    }
}