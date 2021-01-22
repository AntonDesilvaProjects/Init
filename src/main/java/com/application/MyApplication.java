package com.application;

import com.application.service.StockDataService;
import com.init.annotation.Init;
import com.init.annotation.InjectableApplication;
import com.init.container.Application;
import com.init.container.ApplicationRunner;

@InjectableApplication
public class MyApplication implements ApplicationRunner {

    @Init
    public StockDataService stockDataService;

    public static void main(String... args) {
        Application.configureAndRun(args);
    }

    public void run(String... args) {
        stockDataService.getValue("FB");
    }
}
