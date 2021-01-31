package com.application.service.impl;

import com.application.service.StockDataService;
import com.init.annotation.Injectable;
import com.init.annotation.Property;

@Injectable
public class APIStockDataService implements StockDataService {

    @Property("rest.timeout")
    private int timeout;

    @Override
    public double getValue(String ticker) {
        return 0;
    }
}
