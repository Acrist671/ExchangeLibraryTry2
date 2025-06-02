package org.exexe.exchangelibrarytry2;

import org.exexe.exchangelibrarytry2.ui.MainFrame;

import javax.swing.*;

public class ExchangeLibraryDesktop{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
        {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}
