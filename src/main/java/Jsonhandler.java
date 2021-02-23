import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Jsonhandler
{
    private static Jsonhandler jsonhandler = null;
    private List<GroupCreationRequest> groupCreationRequests = new ArrayList<>();
    private List<List<GroupCreationRequest>> allGroups = new ArrayList<>();

    public static Jsonhandler getJsonhandler() {
        if(jsonhandler == null)
        {
            jsonhandler = new Jsonhandler();
        }

        return jsonhandler;
    }

    public List<List<GroupCreationRequest>> getGrups(String filepath)
    {
        String line = "";
        String splitBy = ",";
        int depth = 3;
        try
        {

            Workbook workbook = WorkbookFactory.create(new File(filepath));


            Iterator<Sheet> sheetIterator = workbook.sheetIterator();
            while (sheetIterator.hasNext())
            {
                Sheet sheet = sheetIterator.next();
            }

            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();
            Iterator<Row> rowIterator = sheet.rowIterator();

            System.out.println("------------------------------  <FILE LOADING STARTED>  ----------------------------------");

            boolean root = true;

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if (row.getRowNum() == 0)
                {
                    System.out.println("Skipping row 0\n");
                    continue;
                }

                // Now let's iterate over the columns of the current row
                Iterator<Cell> cellIterator = row.cellIterator();

               List<String> values = new ArrayList<>();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    String cellValue = dataFormatter.formatCellValue(cell);
                    values.add(cellValue.trim());
                    System.out.print(cellValue + "\t\t");
                }

                GroupCreationRequest groupCreationRequest = null;

                if(root)
                    groupCreationRequest   = new GroupCreationRequest(values.get(0) , values.get(1)  ,values.get(3) ,values.get(4));
                else
                    groupCreationRequest   = new GroupCreationRequest(values.get(0) + " * " + groupCreationRequests.get(0).getGroupNameInEng() , values.get(1)  ,values.get(2), values.get(3) + " * " + groupCreationRequests.get(0).getGroupNameInHin(), values.get(4), values.get(5) );

                root = false;
                groupCreationRequests.add(groupCreationRequest);
                System.out.println();
            }
            System.out.println("\n------------------------------  <FILE LOADING ENDED>  ----------------------------------");

            int i = 0;
            System.out.println("\n\n\n------------------------------  <GROUP CREATION STARTED>  ----------------------------------");
            for(GroupCreationRequest groupCreationRequest : groupCreationRequests)
            {
                System.out.println("LEVEL : " + i);
                System.out.println("ENG NAME : " + groupCreationRequest.getGroupNameInEng()
                +"\nENG DESC : " + groupCreationRequest.getGroupDescInEng()
                        +"\nENG DETAIL : " + groupCreationRequest.getGroupDetailInEng()
                +"\nHINDI NAME: " + groupCreationRequest.getGroupNameInHin()
                +"\nHINDI DESC : " + groupCreationRequest.getGroupDescInHin()
                        +"\nHINDI DETAIL : " + groupCreationRequest.getGroupDetailInHin());
                i++;
                System.out.println("\n\n");
            }

            boolean isrootUsed = false;
           for(int k = 0; k < groupCreationRequests.size();) {
               List<GroupCreationRequest> temp = new ArrayList<>();

               if(!isrootUsed) {
                   for(int d = 0; d < depth; d++){
                       temp.add(groupCreationRequests.get(d));
                       k++;
                   }
                   isrootUsed = true;
               }else {
                   for(int d = 0; d < depth - 1; d++){
                       temp.add(groupCreationRequests.get(d));
                   }
                   temp.add(groupCreationRequests.get(k));
                   k++;
               }
               allGroups.add(temp);
           }

            System.out.println("------------------------------  <GROUP CREATION ENDED>  ----------------------------------");

        }
        catch (IOException | InvalidFormatException e)
        {
            e.printStackTrace();
        }

        return allGroups;
    }

}
