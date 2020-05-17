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

    public static Jsonhandler getJsonhandler() {
        if(jsonhandler == null)
        {
            jsonhandler = new Jsonhandler();
        }

        return jsonhandler;
    }

    public List<GroupCreationRequest> getGrups(String filepath)
    {
        String line = "";
        String splitBy = ",";
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


                GroupCreationRequest groupCreationRequest = new GroupCreationRequest(values.get(1) , values.get(2)  ,values.get(3) ,values.get(4) );
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
                +"\nHINDI NAME: " + groupCreationRequest.getGroupNameInHin()
                +"\nHINDI DESC : " + groupCreationRequest.getGroupDescInHin());
                i++;
                System.out.println("\n\n");
            }

            System.out.println("------------------------------  <GROUP CREATION ENDED>  ----------------------------------");

        }
        catch (IOException | InvalidFormatException e)
        {
            e.printStackTrace();
        }

        return groupCreationRequests;
    }

}
