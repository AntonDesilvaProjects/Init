package com.application;

import com.application.service.StockDataService;
import com.application.service.impl.APIStockDataService;
import com.init.annotation.Configuration;
import com.init.annotation.Injectable;

@Configuration
public class AppConfig {

    @Injectable
    public StockDataService stockDataServiceBuilder() {
        return new APIStockDataService();
    }
}
