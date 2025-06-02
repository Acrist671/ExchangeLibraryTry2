package org.exexe.exchangelibrarytry2.ui;

import org.exexe.exchangelibrarytry2.client.CBApiClient;
import org.exexe.exchangelibrarytry2.models.Exchange;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ValuesByDateDialog extends JDialog {
    private final JTextField dateField;
    private final JTextArea textArea;

    public ValuesByDateDialog(JFrame parent) {
        super(parent, "Value By Date", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        inputPanel.add(new JLabel("Введите дату (yyyy-MM-dd):"));
        dateField = new JTextField(15);
        inputPanel.add(dateField);

        JButton submitButton = new JButton("Submit");
        submitButton.setPreferredSize(new Dimension(100, 30));
        submitButton.addActionListener(this::SubmitDate);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(350, 150));

        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(inputPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(submitButton);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(scrollPane);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void SubmitDate(ActionEvent e) {
        if (dateField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Дата не может быть пустой");
            return;
        }
        DateTimeFormatter dateFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate.parse(dateField.getText(), dateFormatter);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Неправильный формат");
            return;
        }
        try {
            List<Exchange> exchanges = CBApiClient.
                    GetExchangeRatesbyDate(dateField.getText());
            exchanges.forEach(exc -> textArea.append(exc + "\n"));
        } catch (Exception ex) {
            textArea.setText("Ошибка: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
