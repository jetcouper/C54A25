import android.content.Context
import java.io.*

data class EtatApplication(
    val activiteCourante: String,
    val extraIntent: HashMap<String, Any> = hashMapOf()
) : Serializable

object SerialisationUtil {

    private const val FILE_NAME = "serialisationUtil.ser"

    fun sauvegarderEtat(context: Context, etat: EtatApplication) {
        try {
            val file = File(context.filesDir, FILE_NAME)
            ObjectOutputStream(FileOutputStream(file)).use { it.writeObject(etat) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun restaurerEtat(context: Context): EtatApplication? {
        val file = File(context.filesDir, FILE_NAME)
        if (!file.exists()) return null
        return try {
            ObjectInputStream(FileInputStream(file)).use {
                it.readObject() as EtatApplication
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun effacerEtat(context: Context) {
        val file = File(context.filesDir, FILE_NAME)
        if (file.exists()) file.delete()
    }
}