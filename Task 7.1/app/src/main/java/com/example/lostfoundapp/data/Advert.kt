import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "adverts")
data class Advert(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val postType: String,
    val phone: String,
    val name: String,
    val description: String,
    val location: String,
    val date: String
)

