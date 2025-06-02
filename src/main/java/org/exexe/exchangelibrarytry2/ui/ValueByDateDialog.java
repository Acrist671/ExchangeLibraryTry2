package org.exexe.exchangelibrarytry2.ui;

import org.exexe.exchangelibrarytry2.client.CBApiClient;
import org.exexe.exchangelibrarytry2.models.Exchange;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

public class ValueByDateDialog extends JDialog {
    private final Map<String, String> names;
    private final JTextField dateField;
    private final JComboBox valueComboBox;
    private final JTextArea textArea;


    public ValueByDateDialog(JFrame parent, Map<String, String> names) {
        super(parent, "Value By Date", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10,10));
        this.names = names;

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Введите дату(yyyy-MM-dd):"));
        dateField = new JTextField();
        inputPanel.add(dateField);

        inputPanel.add(new JLabel("Выберите валюту:"));
        valueComboBox = new JComboBox<>(names.keySet().toArray(new String[0]));
        inputPanel.add(valueComboBox);

        JButton submitButton = new JButton("Submit");
        submitButton.setSize(80, 30);
        submitButton.addActionListener(this::SubmitValueandDate);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450, 150));

        add(inputPanel, BorderLayout.NORTH);
        add(submitButton, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

    }

    private void SubmitValueandDate(ActionEvent e) {
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
            Exchange exchange = CBApiClient.GetExchangebyDate(
                    dateField.getText(),
                    String.valueOf(names.get(valueComboBox.getSelectedItem()))
            );
            String result = exchange.toString();
            textArea.setText(result);
        }
        catch (Exception ex) {
            textArea.setText("Ошибка: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
