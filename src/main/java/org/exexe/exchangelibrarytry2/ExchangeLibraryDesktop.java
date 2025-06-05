package org.exexe.exchangelibrarytry2;

import org.exexe.exchangelibrarytry2.ui.MainFrame;

import javax.swing.*;

public class ExchangeLibraryDesktop{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
        {
            try {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Ошибка инициализации: " + e.getMessage());
            }
        });
    }
}
