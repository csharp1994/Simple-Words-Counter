

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IndexerMain {

	public static void main(String[] args) throws IOException, InvalidFormatException {
		
		IndexerMain app = new IndexerMain();
		Indexer indexer = new Indexer();

		String fileName = "Inputs/DeclarationOfIndependence.txt";
        InputStream is = app.getFileFromResourceAsStream(fileName);

		indexer.printIndex(readInputStream(is));
	}

	private InputStream getFileFromResourceAsStream(String fileName) {
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(fileName);

		if (inputStream == null) {
			throw new IllegalArgumentException("file not found: " + fileName);
		} else {
			return inputStream;
		}
	}

	private static String readInputStream(InputStream is) {

		StringBuilder sb = new StringBuilder();

        try (
			InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(streamReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
				sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

		return sb.toString();
    }
}
