package org.exexe.exchangelibrarytry2.ui;

import org.exexe.exchangelibrarytry2.client.CBApiClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class MainFrame extends JFrame {
    private Map<String, String> nameofValues;

    public MainFrame() {
       try {
            nameofValues = CBApiClient.GetNameofExchanges();
        }
       catch (Exception e) {
            e.printStackTrace();
        }
        this.setTitle("Exchange Library");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel label = new JLabel("Добро пожаловать в скромную библиотеку! Выберите действие:", SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10)); // Отступы для заголовка
        mainPanel.add(label, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 30, 50)); // Отступы по бокам и снизу

        String[] buttonLabels = {
                "Показать названия валют",
                "Показать курс валюты по дате",
                "Показать курс валют по дате",
                "Показать курс валюты в период",
                "Показать курс валют в период"
        };

        ActionListener[] actionListeners = {
                this::buttonAction1,
                this::buttonAction2,
                this::buttonAction3,
                this::buttonAction4,
                this::buttonAction5
        };
        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = new JButton(buttonLabels[i]);

            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(300, 40));
            button.setPreferredSize(new Dimension(280, 35));
            button.setFont(new Font("Arial", Font.PLAIN, 13));
            button.addActionListener(actionListeners[i]);

            buttonPanel.add(button);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }


        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void buttonAction1(ActionEvent e) {
        NamesofValuesDialog dialog = new NamesofValuesDialog(this, nameofValues);
        dialog.setVisible(true);
    }

    private void buttonAction2(ActionEvent e) {
        ValueByDateDialog dialog = new ValueByDateDialog(this, nameofValues);
        dialog.setVisible(true);
    }
    private void buttonAction3(ActionEvent e) {
        ValuesByDateDialog dialog = new ValuesByDateDialog(this);
        dialog.setVisible(true);
    }

    private void buttonAction4(ActionEvent e) {
        DynamicValueDialog dialog = new DynamicValueDialog(this, nameofValues);
        dialog.setVisible(true);
    }

    private void buttonAction5(ActionEvent e) {
        DynamicValuesDialog dialog = new DynamicValuesDialog(this);
        dialog.setVisible(true);
    }
}
