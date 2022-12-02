import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class GUI {

    private Counter counter;

    private JFrame frame;
    private JPanel panel;

    private JButton declarationButton;
    private JButton customInputButton;
    private JLabel label;

    private JTextField customInputField = new JTextField();
    
    public GUI() throws InvalidFormatException, IOException {
        counter = new Counter();
        frame = new JFrame();
        panel = new JPanel();
        label = new JLabel("");

        declarationButton = new JButton(new AbstractAction("Scan Declaration Of Independence") {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    String fileName = "Inputs/DeclarationOfIndependence.txt";
                    InputStream is = this.getFileFromResourceAsStream(fileName);

	                String message = counter.printIndex(readInputStream(is), "Declaration Of Independence");
                    label.setText(message);
                    label.setHorizontalAlignment(0);
                } catch (FileNotFoundException e) {
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
        });
        declarationButton.setSize(50, 50);

        customInputButton = new JButton(new AbstractAction("Scan Custom Input") {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    String message = counter.printIndex(customInputField.getText(), "Custom Input");
                    label.setText(message);
                    label.setHorizontalAlignment(0);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        customInputButton.setSize(50, 50);

        panel.setBorder(BorderFactory.createEmptyBorder(200, 200, 100, 200));
        panel.setLayout(new GridLayout(0, 1));
        panel.add(declarationButton);
        panel.add(customInputButton);
        panel.add(label);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(customInputField, BorderLayout.SOUTH);
        customInputField.setText("Enter your custom text here...");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Simple Words Counter");
        frame.pack();
        frame.setVisible(true);
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
