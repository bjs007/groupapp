import javafx.util.Pair;

import java.io.*;
import java.util.*;
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


        LinkedHashMap<String, String> groupIds = new LinkedHashMap<>();
        for(List<GroupCreationRequest> groupCreationRequests : allGroups){

            CountDownLatch cl = new CountDownLatch(1);
            final List<ChildNodeWithDBReference> list = new ArrayList();
            for (final GroupCreationRequest dbNode : groupCreationRequests) {

                list.add(new ChildNodeWithDBReference(dbNode, null, null));
            }


            Thread t1 = new Thread(new ParentCreation(list, cl, groupIds));
            t1.start();
            cl.await();
        }

        File outputFile = new File("/Users/bijaysharma/Desktop/output.txt");
        try {
            FileOutputStream fos = new FileOutputStream(outputFile);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            Iterator iterator = groupIds.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String,String> entry = (Map.Entry<String, String>) iterator.next();
                bw.write(entry.getValue());
                bw.newLine();
            }
            bw.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
