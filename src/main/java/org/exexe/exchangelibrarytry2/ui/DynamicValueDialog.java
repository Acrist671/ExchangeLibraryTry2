package org.exexe.exchangelibrarytry2.ui;

import org.exexe.exchangelibrarytry2.client.CBApiClient;
import org.exexe.exchangelibrarytry2.models.DynamicExchangeRates;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

public class DynamicValueDialog extends JDialog {
    private final Map<String, String> names;
    private final JTextField startDateField;
    private final JTextField endDateField;
    private final JComboBox valueComboBox;
    private final JTextArea textArea;


    public DynamicValueDialog(JFrame parent, Map<String, String> names) {
        super(parent, "Dynamic Value", true);
        setSize(400, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        this.names = names;

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));  // Изменили на 3 строки
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Начальная дата (yyyy-MM-dd):"));
        startDateField = new JTextField();
        inputPanel.add(startDateField);

        inputPanel.add(new JLabel("Конечная дата (yyyy-MM-dd):"));
        endDateField = new JTextField();
        inputPanel.add(endDateField);

        inputPanel.add(new JLabel("Выберите валюту:"));
        valueComboBox = new JComboBox<>(names.keySet().toArray(new String[0]));
        inputPanel.add(valueComboBox);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(this::SubmitValueAndDates);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450, 150));

        add(inputPanel, BorderLayout.NORTH);
        add(submitButton, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
    }

    private void SubmitValueAndDates(ActionEvent actionEvent) {
        if (startDateField.getText().isEmpty() || endDateField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Дата не может быть пустой");
            return;
        }
        if (LocalDate.parse(startDateField.getText()).isAfter(LocalDate.parse(endDateField.getText()))) {
            JOptionPane.showMessageDialog(this, "Начальная дата позже конечной - это плоха");
            return;
        }
        DateTimeFormatter dateFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate.parse(startDateField.getText(), dateFormatter);
            LocalDate.parse(endDateField.getText(), dateFormatter);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Неправильный формат");
            return;
        }
        try {
            String id = names.get(valueComboBox.getSelectedItem().toString());
            DynamicExchangeRates rates = CBApiClient.
                    GetDynamicExchange(startDateField.getText(), endDateField.getText(), id);
            textArea.setText(rates.toString());
        }
        catch (Exception ex) {
            textArea.setText("Ошибка: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
