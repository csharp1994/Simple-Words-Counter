import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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

    // TODO
    // Organize output in order of occurances
    // Support custom input from specified file
    // Build out rest of GUI 
    // Flesh out Spring configs
    // Autowire up stuff
    public Counter() throws InvalidFormatException, IOException {
        wb = new XSSFWorkbook();;
        spreadsheet = wb.createSheet("Indexed Words");
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

    public void printIndex(String input) throws FileNotFoundException, IOException {
        // Place words from input string into array
        input = input.trim();
        inputWordsArr = input.replaceAll("\\p{Punct}", "").toLowerCase().split("\\s+");
        
        // Put each word into hashmap, counting number of occurances
        for (int i = 0; i < inputWordsArr.length; i++) {
            if (!excludedStrings.contains(inputWordsArr[i])) {
                inputWordsMap.put(inputWordsArr[i], inputWordsMap.get(inputWordsArr[i]) == null ? 1 : inputWordsMap.get(inputWordsArr[i]) + 1);
            }
        }

        // Write into spreadsheet object
        Set<String> rows = inputWordsMap.keySet();
        rowCounter = spreadsheet.getLastRowNum() + 1;

        for (String key : rows) {
            Row row = spreadsheet.createRow(rowCounter++);
            int cellNum = 0;

            Cell wordCell = row.createCell(cellNum++);
            wordCell.setCellValue(key);
            wordCell.setCellStyle(contentStyle);
            Cell wordCountCell = row.createCell(cellNum++);
            wordCountCell.setCellValue(inputWordsMap.get(key));
            wordCell.setCellStyle(contentStyle);
        }

        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + "IndexedWords.xlsx";

        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        wb.write(outputStream);
        wb.close();
    }
}