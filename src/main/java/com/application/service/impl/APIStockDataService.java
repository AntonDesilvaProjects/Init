package com.application.service.impl;

import com.application.service.StockDataService;
import com.init.annotation.Injectable;
import com.init.annotation.Property;

@Injectable
public class APIStockDataService implements StockDataService {

    @Property("rest.timeout")
    private int timeout;

    @Injectable
    public StockDataService stockDataServiceBuilder() {
        System.out.println(timeout);
        return new APIStockDataService();
    }

    @Override
    public double getValue(String ticker) {
        return 0;
    }
}
