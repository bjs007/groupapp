import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;

public class DatabaseRefSingleton
{
    public static DatabaseRefSingleton getInstance()
    {
        return new DatabaseRefSingleton();

    }
    public DatabaseReference getFirebaseAccess() {
        DatabaseReference ref = null;
        try {
            FileInputStream serviceAccount = new FileInputStream("/Users/bijaysharma/Desktop/frienders-a1fdb-firebase-adminsdk.json");

// Initialize the app with a service account, granting admin privileges
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://frienders-a1fdb.firebaseio.com/")
                    .build();
            FirebaseApp.initializeApp(options);


// As an admin, the app has access to read and write all data, regardless of Security Rules
            ref = FirebaseDatabase.getInstance()
                    .getReference();
//
        } catch (Exception ex) {
        }

        return ref;
    }
}
