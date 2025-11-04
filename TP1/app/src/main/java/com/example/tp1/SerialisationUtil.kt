import android.content.Context
import java.io.*


//Objet pour sauvegarder l'état de l'application
data class EtatApplication(
    val activiteCourante: String,
    val extraIntent: HashMap<String, Any> = hashMapOf()
) : Serializable

object SerialisationUtil {

    private const val FILE_NAME = "serialisationUtil.ser"
    //Fonction pour sauvegarder l'état du programme
    fun sauvegarderEtat(context: Context, etat: EtatApplication) {
        try {
            val file = File(context.filesDir, FILE_NAME)
            ObjectOutputStream(FileOutputStream(file))
                .use{
                    it.writeObject(etat)
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    //Fonction pour restaurer l'état du programme.
    fun restaurerEtat(context: Context): EtatApplication? {
        val file = File(context.filesDir, FILE_NAME)
        if (!file.exists()) return null
        return try {
            ObjectInputStream(FileInputStream(file))
                .use{
                    it.readObject() as EtatApplication
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}