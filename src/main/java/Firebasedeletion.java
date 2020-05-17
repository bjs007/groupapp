import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class Firebasedeletion
{
    static DatabaseReference databaseReference;
    public static void main(String args[]) throws InterruptedException
    {

        DatabaseRefSingleton databaseRefSingleton = DatabaseRefSingleton.getInstance();
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter id of the group to delete : ");
        String groupId = sc.next();

        System.out.println("Please enter the level : ");
        int level = Integer.parseInt(sc.next());

        System.out.println("Is it a leaf (y/n)? : ");
        String str = sc.next();
        boolean flag = str.equals("y") || str.equals("yes");

        CountDownLatch cl = new CountDownLatch(1);

        databaseReference = databaseRefSingleton.getFirebaseAccess();

        if(flag)
        {

            databaseReference.child("leafs").child(groupId).removeEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    String key = snapshot.getKey();

                    databaseReference.child("Subscribed").child(groupId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if(snapshot.exists())
                            {
                                final long childresn = snapshot.getChildrenCount();
                                final int[] count = new int[]{0};
                                Iterator<DataSnapshot> it = snapshot.getChildren().iterator();
                                while (it.hasNext())
                                {
                                    final String userId = it.next().getKey();
                                    databaseReference.child("Subscribed").child(groupId).child(userId).removeValue(new DatabaseReference.CompletionListener
                                            (){
                                        @Override
                                        public void onComplete(DatabaseError error, DatabaseReference ref)
                                        {
                                            databaseReference.child("Users").child(userId)
                                                    .child("subscribed").child(groupId).removeValue(new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError error, DatabaseReference ref) {
                                                    System.out.println("Deleted " + groupId);
                                                    count[0]++;
                                                    if(count[0] == childresn)
                                                    {
                                                        databaseReference.child("Subscribed").child(groupId).removeValue(new DatabaseReference.CompletionListener() {
                                                            @Override
                                                            public void onComplete(DatabaseError error, DatabaseReference ref) {

                                                            }
                                                        });
                                                    }
                                                    cl.countDown();
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                            else
                            {
                                System.out.println("Subscribed group does not exist");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            System.out.println("Subscribed group does not exist");
                            cl.countDown();
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }

            });
        }
        cl.await();
    }


    private DatabaseReference getFirebaseAccess() {
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
