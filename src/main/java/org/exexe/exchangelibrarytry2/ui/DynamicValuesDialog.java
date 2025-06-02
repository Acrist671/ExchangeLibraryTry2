package org.exexe.exchangelibrarytry2.ui;

import org.exexe.exchangelibrarytry2.client.CBApiClient;
import org.exexe.exchangelibrarytry2.models.Exchange;
import org.exexe.exchangelibrarytry2.models.ExchangeRates;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.List;

public class DynamicValuesDialog extends JDialog {
    private final JTextField startDateField;
    private final JTextField endDateField;
    private final JTable table;
    private final DefaultTableModel tableModel;

    DynamicValuesDialog(JFrame parent) {
        super(parent, "Dynamic Values", true);

        setSize(600, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Начальная дата (yyyy-MM-dd):"));
        startDateField = new JTextField();
        inputPanel.add(startDateField);

        inputPanel.add(new JLabel("Конечная дата (yyyy-MM-dd):"));
        endDateField = new JTextField();
        inputPanel.add(endDateField);

        JButton fetchButton = new JButton("Получить данные");
        fetchButton.addActionListener(e -> SubmitDates());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(fetchButton, BorderLayout.SOUTH);

        List<String> strings = new ArrayList<>();
        strings.add(" ");
        try {
            strings.addAll(1, CBApiClient.GetExchangeRatesbyDate(LocalDate.
                    now().toString()).stream().map(Exchange::getName).toList());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        String[] defaultNames = strings.toArray(new String[0]);
        tableModel = new DefaultTableModel(defaultNames, 0);
        table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        TableColumnModel columnModel = table.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setPreferredWidth(150);
        }

        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
    }

    private void SubmitDates() {
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
        List<LocalDate> dateList = new ArrayList<>();
        for (LocalDate date = LocalDate.parse(startDateField.getText());
             !date.isAfter(LocalDate.parse(endDateField.getText()));
             date = date.plusDays(1)) {
            dateList.add(date);
        }
        List<ExchangeRates> exchangeRates = new ArrayList<>();
        try {
            exchangeRates = CBApiClient.
                    GetDynamicExchangesRates(startDateField.getText(), endDateField.getText());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < dateList.size(); i++) {
            Vector<String> vector = new Vector<>();
            vector.add(dateList.get(i).toString());
            vector.addAll(exchangeRates.get(i).getExchanges().
                    stream().map(Exchange::getValuebyString).toList());
            tableModel.addRow(vector);
        }
    }
}
