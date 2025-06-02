package org.exexe.exchangelibrarytry2.ui;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class NamesofValuesDialog extends JDialog {
    private final JTextArea textArea;

    NamesofValuesDialog(JFrame parent, Map<String, String> names) {
        super(parent, "Value By Date", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10)); // Основной layout для всего окна

        // Панель с текстовой областью (занимает CENTER BorderLayout)
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
        textArea.setText(String.join("\n", names.keySet()));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        textPanel.add(scrollPane, BorderLayout.CENTER); // Растягиваем на всю панель

        // Добавляем текстовую панель в центр окна
        add(textPanel, BorderLayout.CENTER);

        // Опционально: кнопка "OK" внизу (SOUTH)
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dispose());
        add(okButton, BorderLayout.SOUTH);
    }
}
