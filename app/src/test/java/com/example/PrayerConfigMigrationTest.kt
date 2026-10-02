package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import com.example.data.local.MasjidDatabase
import com.example.data.local.MosqueConfigEntity
import androidx.room.Room
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class PrayerConfigMigrationTest {
    @Test fun roomValidatesCompleteUpgradedSchemaAndKeepsAdministratorData() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val fields = setOf("screenCustomizationJson", "adhanDurationSeconds", "fridayKhutbahEnabled", "fridayKhutbahManual", "fridayKhutbahMinutes", "focusBlankScreen", "focusShowClock")
        val template = Room.inMemoryDatabaseBuilder(context, MasjidDatabase::class.java).allowMainThreadQueries().build()
        val ddl = mutableListOf<String>()
        val columns = mutableListOf<String>(); val values = mutableListOf<Any?>()
        try {
            template.mosqueConfigDao().insertOrUpdate(MosqueConfigEntity(mosqueName = "Masjid Migrasi", latitude = -6.5, iqomahJumat = 4, webAdminPassword = "pinLama789"))
            val sql = template.openHelper.writableDatabase
            sql.query("SELECT sql FROM sqlite_master WHERE type IN ('table','index') AND sql IS NOT NULL AND name NOT LIKE 'sqlite_%' AND name NOT LIKE 'android_%' AND name != 'room_master_table' ORDER BY CASE type WHEN 'table' THEN 0 ELSE 1 END").use { cursor ->
                while (cursor.moveToNext()) {
                    var statement = cursor.getString(0)
                    if (statement.contains("CREATE TABLE", true) && statement.contains("`mosque_config`"))
                        fields.forEach { statement = statement.replace(Regex(",\\s*`$it`\\s+[^,]+"), "") }
                    ddl.add(statement)
                }
            }
            sql.query("SELECT * FROM mosque_config WHERE id=1").use { cursor ->
                assertTrue(cursor.moveToFirst())
                for (index in 0 until cursor.columnCount) {
                    if (cursor.getColumnName(index) in fields) continue
                    columns.add(cursor.getColumnName(index))
                    values.add(when (cursor.getType(index)) {
                        android.database.Cursor.FIELD_TYPE_INTEGER -> cursor.getLong(index)
                        android.database.Cursor.FIELD_TYPE_FLOAT -> cursor.getDouble(index)
                        android.database.Cursor.FIELD_TYPE_NULL -> null
                        else -> cursor.getString(index)
                    })
                }
            }
        } finally { template.close() }
        val filename = "migration-${java.util.UUID.randomUUID()}.db"
        val helper = FrameworkSQLiteOpenHelperFactory().create(SupportSQLiteOpenHelper.Configuration.builder(context).name(filename)
            .callback(object : SupportSQLiteOpenHelper.Callback(22) {
                override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                    ddl.forEach { db.execSQL(it) }
                    db.execSQL("INSERT INTO mosque_config (${columns.joinToString(",")}) VALUES (${columns.joinToString(",") { "?" }})", values.toTypedArray())
                }
                override fun onUpgrade(db: androidx.sqlite.db.SupportSQLiteDatabase, oldVersion: Int, newVersion: Int) {}
            }).build())
        helper.writableDatabase; helper.close()
        val upgraded = Room.databaseBuilder(context, MasjidDatabase::class.java, filename).allowMainThreadQueries()
            .addMigrations(MasjidDatabase.MIGRATION_22_23, MasjidDatabase.MIGRATION_23_24).build()
        try {
            val saved = upgraded.mosqueConfigDao().getConfig()!!
            assertEquals("Masjid Migrasi", saved.mosqueName)
            assertEquals("pinLama789", saved.webAdminPassword)
            assertEquals(4, saved.iqomahJumat)
            assertEquals(-6.5, saved.latitude, 0.0)
            assertTrue(saved.fridayKhutbahManual)
            assertEquals(30, saved.adhanDurationSeconds)
        } finally { upgraded.close(); context.deleteDatabase(filename) }
    }

    @Test fun migrationPreservesExistingConfigurationAndAddsSafeDefaults() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val helper = FrameworkSQLiteOpenHelperFactory().create(
            SupportSQLiteOpenHelper.Configuration.builder(context).name(null)
                .callback(object : SupportSQLiteOpenHelper.Callback(22) {
                    override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                        db.execSQL("CREATE TABLE mosque_config (id INTEGER PRIMARY KEY NOT NULL, mosqueName TEXT NOT NULL, latitude REAL NOT NULL, iqomahJumat INTEGER NOT NULL)")
                        db.execSQL("INSERT INTO mosque_config VALUES (1, 'Masjid Uji', -6.5, 4)")
                    }
                    override fun onUpgrade(db: androidx.sqlite.db.SupportSQLiteDatabase, oldVersion: Int, newVersion: Int) {}
                }).build())
        try {
            val db = helper.writableDatabase
            MasjidDatabase.MIGRATION_22_23.migrate(db)
            db.query("SELECT * FROM mosque_config WHERE id=1").use { row ->
                assertTrue(row.moveToFirst())
                assertEquals("Masjid Uji", row.getString(row.getColumnIndexOrThrow("mosqueName")))
                assertEquals(-6.5, row.getDouble(row.getColumnIndexOrThrow("latitude")), 0.0)
                assertEquals(4, row.getInt(row.getColumnIndexOrThrow("iqomahJumat")))
                assertEquals(30, row.getInt(row.getColumnIndexOrThrow("adhanDurationSeconds")))
                assertEquals(1, row.getInt(row.getColumnIndexOrThrow("fridayKhutbahManual")))
                assertEquals(20, row.getInt(row.getColumnIndexOrThrow("fridayKhutbahMinutes")))
                assertEquals(0, row.getInt(row.getColumnIndexOrThrow("focusBlankScreen")))
            }
        } finally { helper.close() }
    }
}
