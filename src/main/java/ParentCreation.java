import androidx.annotation.NonNull;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;

public class ParentCreation implements Runnable {
    private static DatabaseReference firebaseDatabase;
    List<ChildNodeWithDBReference> childNodeWithDBReferences;
    CountDownLatch cl;
    LinkedHashMap<String, String> groupIds = new LinkedHashMap<>();

    public ParentCreation(List<ChildNodeWithDBReference> childNodeWithDBReferences, CountDownLatch cl, LinkedHashMap<String, String> groupIds ) {
        if(firebaseDatabase ==null)
        firebaseDatabase = getFirebaseAccess().child("Groups");
        this.childNodeWithDBReferences = childNodeWithDBReferences;
        this.groupIds = groupIds;
        this.cl = cl;
    }

    @Override
    public void run() {
        createGroup(childNodeWithDBReferences, 0);
    }

    private void createGroup(final List<ChildNodeWithDBReference> groupPathWithName, final int level) {


        final String currentLevel = "level - " + level;
        boolean isFunctionCallAtLeafNode = false;

        if (level == groupPathWithName.size() - 1) {
            isFunctionCallAtLeafNode = true;
        }

        final boolean leafLevel = isFunctionCallAtLeafNode;

       if(level == 0)
       {
           firebaseDatabase.child(currentLevel).addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                   Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                   Group requiredNodeAtCurrentLevel = null;

                   while (iterator.hasNext()) {
                       Group group = iterator.next().getValue(Group.class);
                       String newGroupName = groupPathWithName.get(level).getRequest().getGroupNameInEng().trim();
                       String existingName = group.getEngName();

                       if (group != null && existingName.equalsIgnoreCase(newGroupName)) {
                           requiredNodeAtCurrentLevel = group;
                           if(level == groupPathWithName.size() - 1)
                               System.out.println("WARNING DUPLICATE GROUP");
                           break;
                       }
                   }

                   if (requiredNodeAtCurrentLevel == null) {
                       DatabaseReference newGroupDBRefId = firebaseDatabase.child(currentLevel).push();
                       final String groupId = newGroupDBRefId.getKey();
                       requiredNodeAtCurrentLevel = createGroup(groupPathWithName.get(level).getRequest(), groupId, level, leafLevel);


                       if (level == 0) {
                           requiredNodeAtCurrentLevel.setParentId("root");
                           requiredNodeAtCurrentLevel.setRootId(groupId);
                           groupPathWithName.get(level).setRootId(groupId);
                       } else {
                           requiredNodeAtCurrentLevel.setParentId(groupPathWithName.get(level - 1).getCurrentNodeDbRef());
                           requiredNodeAtCurrentLevel.setRootId(groupPathWithName.get(level - 1).getRootId());
                           groupPathWithName.get(level).setRootId(groupPathWithName.get(level - 1).getRootId());
                       }
                   }

                   groupPathWithName.get(level).setCurrentNodeDbRef(requiredNodeAtCurrentLevel.getId());

                   final Map<String, Object> updatedNodeAtCurrentLevelDetail = new HashMap<>();

                   if(level == 0)
                   {
                       updatedNodeAtCurrentLevelDetail.put(currentLevel + "/" + requiredNodeAtCurrentLevel.getId() + "/",
                               requiredNodeAtCurrentLevel);
                   }
                   else
                   {
                       updatedNodeAtCurrentLevelDetail.put(currentLevel + "/"
                                       + groupPathWithName.get(level - 1).getCurrentNodeDbRef() + "/"
                                       + requiredNodeAtCurrentLevel.getId(),
                               requiredNodeAtCurrentLevel);
                   }

                   final Group finalNodeHere = requiredNodeAtCurrentLevel;

                   firebaseDatabase.updateChildren(updatedNodeAtCurrentLevelDetail, new DatabaseReference.CompletionListener() {
                       @Override
                       public void onComplete(DatabaseError error, DatabaseReference ref)
                       {
                           if (!leafLevel)
                           {
                               createGroup(groupPathWithName, level + 1);
                           }
                           else
                           {
                               Map<String, Object> updateLeafNodes = new HashMap<>();
                               updateLeafNodes.put("leafs" + "/" + finalNodeHere.getId() + "/", finalNodeHere);
                               firebaseDatabase.updateChildren(updateLeafNodes, new DatabaseReference.CompletionListener() {
                                   @Override
                                   public void onComplete(DatabaseError error, DatabaseReference ref)
                                   {


                                       System.out.println("--------------------- DATABASE REFERENCE-------------------------");
                                       for(int i = 0; i< groupPathWithName.size(); i++)
                                       {
                                           System.out.println("Level : " + i);
                                           System.out.println("Child : " + groupPathWithName.get(i).getCurrentNodeDbRef() + "Parent :" + groupPathWithName.get(i).getCurrentNodeDbRef());
                                       }

                                       cl.countDown();
                                       System.out.println("--------------------- DATABASE REFERENCE ENDED-------------------------");
                                   }
                               });
                           }
                       }
                   });


               }
               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });

       }
       else
       {
           firebaseDatabase.child(currentLevel).child(groupPathWithName.get(level-1).getCurrentNodeDbRef()).addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                   Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                   Group requiredNodeAtCurrentLevel = null;

                   while (iterator.hasNext()) {
                       Group group = iterator.next().getValue(Group.class);
                       String newGroupName = groupPathWithName.get(level).getRequest().getGroupNameInEng().trim();
                       String existingName = group.getEngName();

                       if (group != null && existingName.equalsIgnoreCase(newGroupName)) {
                           requiredNodeAtCurrentLevel = group;
                           if(level == groupPathWithName.size() - 1)
                               System.out.println("\n\n\n---------------------------------------WARNING DUPLICATE GROUP---------------------------");
                           break;
                       }
                   }

                   if (requiredNodeAtCurrentLevel == null) {
                       DatabaseReference newGroupDBRefId = firebaseDatabase.child(currentLevel).push();
                       final String groupId = newGroupDBRefId.getKey();
                       requiredNodeAtCurrentLevel = createGroup(groupPathWithName.get(level).getRequest(), groupId, level, leafLevel);


                       if (level == 0) {
                           requiredNodeAtCurrentLevel.setParentId("root");
                           groupPathWithName.get(level).setRootId(groupId);
                       } else {
                           requiredNodeAtCurrentLevel.setParentId(groupPathWithName.get(level - 1).getCurrentNodeDbRef());
                           requiredNodeAtCurrentLevel.setRootId(groupPathWithName.get(level - 1).getRootId());
                           groupPathWithName.get(level).setRootId(groupPathWithName.get(level - 1).getRootId());
                       }
                   }

                   groupPathWithName.get(level).setCurrentNodeDbRef(requiredNodeAtCurrentLevel.getId());

                   final Map<String, Object> updatedNodeAtCurrentLevelDetail = new HashMap<>();

                   if(level == 0)
                   {

                       updatedNodeAtCurrentLevelDetail.put(currentLevel + "/" + requiredNodeAtCurrentLevel.getId() + "/",
                               requiredNodeAtCurrentLevel);
                   }
                   else
                   {
                       updatedNodeAtCurrentLevelDetail.put(currentLevel + "/"
                                       + groupPathWithName.get(level - 1).getCurrentNodeDbRef() + "/"
                                       + requiredNodeAtCurrentLevel.getId(),
                               requiredNodeAtCurrentLevel);
                   }

                   final Group finalNodeHere = requiredNodeAtCurrentLevel;

                   firebaseDatabase.updateChildren(updatedNodeAtCurrentLevelDetail, new DatabaseReference.CompletionListener() {
                       @Override
                       public void onComplete(DatabaseError error, DatabaseReference ref)
                       {
                           if (!leafLevel)
                           {
                               createGroup(groupPathWithName, level + 1);
                           }
                           else
                           {
                               Map<String, Object> updateLeafNodes = new HashMap<>();
                               updateLeafNodes.put("leafs" + "/" + finalNodeHere.getId() + "/", finalNodeHere);
                               firebaseDatabase.updateChildren(updateLeafNodes, new DatabaseReference.CompletionListener() {
                                   @Override
                                   public void onComplete(DatabaseError error, DatabaseReference ref)
                                   {


                                       System.out.println("\n\n----------------------------------------------<DATABASE REFERENCE>-----------------------------");
                                       for(int i = 0; i< groupPathWithName.size(); i++)
                                       {
                                           System.out.println("Level : " + i);
                                           System.out.println("Child : " + groupPathWithName.get(i).getCurrentNodeDbRef() + "\tParent : ." + groupPathWithName.get(i).getCurrentNodeDbRef());
                                       }

                                       System.out.println("\n----------------------------------------------<DATABASE REFERENCE ENDED>  ----------------------------------");
                                       cl.countDown();
                                   }
                               });
                           }
                       }
                   });


               }
               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });

       }
    }

    private Group createGroup(GroupCreationRequest request, String id, int level, boolean isLeaf) {

        String currentUser = "admin";
        if(!groupIds.containsKey(request.getGroupNameInEng())){
            groupIds.put(request.getGroupNameInEng(), id);
        }
        Group grp = new Group(request.getGroupNameInEng(), id, currentUser, getCurrentDate(), getCurrentTime(), isLeaf, level, new ArrayList<String>());
        grp.setEngDesc(request.getGroupDescInEng());
        grp.setHinName(request.getGroupNameInHin());
        grp.setHinDesc(request.getGroupDescInHin());
        return grp;
    }


    private String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        return currentDate.format(calendar.getTime());
    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        return currentTime.format(calendar.getTime());
    }

    private DatabaseReference getFirebaseAccess() {
        DatabaseReference ref = null;
        try {
            FileInputStream serviceAccount = new FileInputStream("/Users/bijaysharma/Desktop/frienders-46611-bf60a660bc50.json");

// Initialize the app with a service account, granting admin privileges
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://frienders-46611.firebaseio.com/")
                    .build();
            FirebaseApp.initializeApp(options);


// As an admin, the app has access to read and write all data, regardless of Security Rules
             ref = FirebaseDatabase.getInstance().getReference();
//
        } catch (Exception ex) {
        }

        return ref;
    }

}
