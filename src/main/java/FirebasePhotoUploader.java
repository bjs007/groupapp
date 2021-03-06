import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FirebasePhotoUploader {

    public static void main(String args[]) {
        getFirebaseAccess();
    }
    private static DatabaseReference getFirebaseAccess() {
        DatabaseReference ref = null;
        try {
            FileInputStream serviceAccount = new FileInputStream("/Users/bijaysharma/Desktop/firestore/frienders-46611-firebase-adminsdk-m6msi-e3b8b9c83d.json");

// Initialize the app with a service account, granting admin privileges
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .setDatabaseUrl("https://frienders-46611.firebaseio.com/")
            .setStorageBucket("frienders-46611.appspot.com")
                    .build();
            FirebaseApp.initializeApp(options);

//            Bucket bucket = StorageClient.getInstance().bucket();

            try {
                File groupNameAndImageId =new File("/Users/bijaysharma/Desktop/output.txt");    //creates a new file instance
                FileReader fr=new FileReader(groupNameAndImageId);   //reads the file
                BufferedReader br=new BufferedReader(fr);
                String line;
                int i = 1;
                while((line=br.readLine())!=null)
                {
                    InputStream file = new FileInputStream("/Users/bijaysharma/Desktop/firestore/" +i +".png");
                    Blob blob = StorageClient.getInstance().bucket().create("Groups_image/" + line+".jpeg", file, "image/jpeg");
                    i++;
                }



            }catch (Exception e){

            }



//            BlobId blobId = BlobId.of("frienders-46611.appspot.com", "fileName");
//            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
//            StorageClient.getInstance().bucket().create(String.valueOf(blobInfo), Files.readAllBytes(Paths.get("/Users/bijaysharma/Desktop/firestore/2.png")));

//
        } catch (Exception ex) {
        }

        return ref;
    }
}
