import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map.Entry;  
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Collections;  
import java.util.Comparator;  

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Counter {

    private Workbook wb;
    private Sheet spreadsheet;
    private Row header;
    private XSSFFont font;
    private CellStyle headerStyle;
    private CellStyle contentStyle;
    private Cell headerCell;

    private String[] inputWordsArr;
    private HashMap<String, Integer> inputWordsMap;
    private int rowCounter;

    private List<String> excludedStrings = Arrays.asList(" ", "of", "the", "to", "and", "for");

    public Counter() throws InvalidFormatException, IOException {
        wb = new XSSFWorkbook();;
        spreadsheet = wb.createSheet("Test");
        spreadsheet.setColumnWidth(0, 6000);
        spreadsheet.setColumnWidth(1, 6000);
        header = spreadsheet.createRow(0);

        headerStyle = wb.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        font = ((XSSFWorkbook) wb).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);

        headerStyle.setFont(font);

        headerCell = header.createCell(0);
        headerCell.setCellValue("Word");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Occurances");
        headerCell.setCellStyle(headerStyle);

        contentStyle = wb.createCellStyle();
        contentStyle.setWrapText(true);

        inputWordsArr = new String[]{};
        inputWordsMap = new HashMap<>();
    }

    public String printIndex(String input, String title) throws FileNotFoundException, IOException {
        // Place words from input string into array
        input = input.trim();
        inputWordsArr = input.replaceAll("\\p{Punct}", " ").toLowerCase().split("\\s+");

        if (inputWordsArr[0].equals("")) {
            return "Error: Input Is Empty";
        }
        
        // Put each word into hashmap, counting number of occurances
        for (int i = 0; i < inputWordsArr.length; i++) {
            if (!excludedStrings.contains(inputWordsArr[i])) {
                inputWordsMap.put(inputWordsArr[i], inputWordsMap.get(inputWordsArr[i]) == null ? 1 : inputWordsMap.get(inputWordsArr[i]) + 1);
            }
        }

        // Sort results by value, decending
        List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(inputWordsMap.entrySet());
        Collections.sort(list, new Comparator<Entry<String, Integer>>() {
            public int compare(Entry<String, Integer> item1, Entry<String, Integer> item2) {
                return item2.getValue().compareTo(item1.getValue());
            }
        });

        // Secondarily sort by key, alphabetically
        for (int i = 0; i < list.size(); i++) {
            int currentValue = list.get(i).getValue();
            int startIndex = i;
            int endIndex = getEndIndexOfSameValueSubList(list, startIndex, currentValue);
            Collections.sort(list.subList(startIndex, endIndex), new Comparator<Entry<String, Integer>>() {
                public int compare(Entry<String, Integer> item1, Entry<String, Integer> item2) {
                    return item1.getKey().compareTo(item2.getKey());
                }
            });
        }

        // Write into spreadsheet object
        rowCounter = spreadsheet.getLastRowNum() + 1;
 
        for (Entry<String, Integer> entry : list) {
            Row row = spreadsheet.createRow(rowCounter++);
            int cellNum = 0;

            Cell wordCell = row.createCell(cellNum++);
            wordCell.setCellValue(entry.getKey());
            wordCell.setCellStyle(contentStyle);
            Cell wordCountCell = row.createCell(cellNum++);
            wordCountCell.setCellValue(entry.getValue());
            wordCell.setCellStyle(contentStyle);
        }

        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + title + ".xlsx";

        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        wb.write(outputStream);

        return title + " Spreadsheet Created...";
    }

    private int getEndIndexOfSameValueSubList(List<Entry<String, Integer>> list, int startIndex, int value) {
        int counter = startIndex;
        for (; counter < list.size(); counter++) {
            int currentValue = list.get(counter).getValue();
            if (currentValue != value) {
                return counter;
            }
        }
        return counter;
    }
}