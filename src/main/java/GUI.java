import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

// TODO switch to html/css setup
    // TODO build out more GUI
public class GUI implements ActionListener {

    private JFrame frame;
    private JPanel panel;

    private JButton button;
    private JLabel label;
    
    public GUI() {
        frame = new JFrame();
        panel = new JPanel();
        button = new JButton("Scan Declaration of Independence");
        label = new JLabel("");

        button.addActionListener(this);
        button.setSize(20, 20);

        panel.setBorder(BorderFactory.createEmptyBorder(100, 100, 50, 100));
        panel.setLayout(new GridLayout(0, 1));
        panel.add(button);
        panel.add(label);

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Simple Words Counter");
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        try {
            Counter indexer = new Counter();

            String fileName = "Inputs/DeclarationOfIndependence.txt";
            InputStream is = this.getFileFromResourceAsStream(fileName);

		    indexer.printIndex(readInputStream(is));
            label.setText("Spreadsheet Created...");
            label.setHorizontalAlignment(0);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
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
