package org.exexe.exchangelibrarytry2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExchangeLibraryApplication {
    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        SpringApplication.run(ExchangeLibraryApplication.class, args);
        Thread desktopThread = new Thread(() -> ExchangeLibraryDesktop.main(args));
        desktopThread.start();
    }
}
