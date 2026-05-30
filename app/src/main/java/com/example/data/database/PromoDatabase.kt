package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.*

@Database(
    entities = [
        UserEntity::class,
        StoreEntity::class,
        PromoEntity::class,
        FollowEntity::class,
        NotificationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PromoDatabase : RoomDatabase() {
    abstract fun promoDao(): PromoDao

    companion object {
        @Volatile
        private var INSTANCE: PromoDatabase? = null

        fun getDatabase(context: Context): PromoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PromoDatabase::class.java,
                    "promo_facil_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
