import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.FoodIntake.FoodIntake
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.FoodIntake.FoodIntakeDao
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Insight.Insight
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Insight.InsightDao
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.NutriCoachTip.NutriCoachTip
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.NutriCoachTip.NutriCoachTipDao
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Patient.Patient
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Patient.PatientDao

/**
 * This is the database class for the application. It is a Room database.
 * It contains 4 entities: [Patient] [FoodIntake] [NutriCoachTip] [Insight].
 * The version is 1 and exportSchema is false.
 */
@Database(entities = [Patient::class, FoodIntake::class, NutriCoachTip::class, Insight::class], version = 16, exportSchema = false)
abstract class ClinicDatabase : RoomDatabase() {

    /**
     * Returns the [PatientDao] object.
     * This is an abstract function that is implemented by Room.
     */
    abstract fun patientDao(): PatientDao
    abstract fun foodIntakeDao(): FoodIntakeDao
    abstract fun nutriCoachTipDao(): NutriCoachTipDao
    abstract fun insightDao(): InsightDao

    companion object {
        /**
         * This is a volatile variable that holds the database instance.
         * It is volatile so that it is immediately visible to all threads.
         */
        @Volatile
        private var Instance: ClinicDatabase? = null

        /**
         * Returns the database instance.
         * If the instance is null, it creates a new database instance.
         * @param context The context of the application.
         */
        fun getDatabase(context: Context): ClinicDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    ClinicDatabase::class.java,
                    "hospital_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
