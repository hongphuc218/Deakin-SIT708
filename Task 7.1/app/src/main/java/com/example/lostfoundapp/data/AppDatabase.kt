import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lostfoundapp.data.AdvertDao



@Database(entities = [Advert::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun advertDao(): AdvertDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "lost_found_db"
                ).build().also { INSTANCE = it }
            }
    }
}
