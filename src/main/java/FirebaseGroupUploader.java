import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class FirebaseGroupUploader
{
    public static void main(String args[]) throws InterruptedException {


        Jsonhandler jsonhandler = Jsonhandler.getJsonhandler();
        List<List<GroupCreationRequest> > groupCreationRequests = jsonhandler.getGrups("/Users/bijaysharma/Desktop/Group.xlsx");
        if(groupCreationRequests == null || groupCreationRequests.size() == 0)
        {
            System.out.println("There is no group in the csv file");
        }

        createGroup(groupCreationRequests);

    }

    private static void createGroup(List<List<GroupCreationRequest>> allGroups) throws InterruptedException {




        for(List<GroupCreationRequest> groupCreationRequests : allGroups){

            CountDownLatch cl = new CountDownLatch(1);
            final List<ChildNodeWithDBReference> list = new ArrayList();
            for (final GroupCreationRequest dbNode : groupCreationRequests) {

                list.add(new ChildNodeWithDBReference(dbNode, null, null));
            }


            Thread t1 = new Thread(new ParentCreation(list, cl));
            t1.start();
            cl.await();
        }


    }


}
