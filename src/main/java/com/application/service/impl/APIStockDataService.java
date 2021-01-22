package com.application.service.impl;

import com.application.service.StockDataService;
import com.init.annotation.Injectable;

@Injectable
public class APIStockDataService implements StockDataService {
    @Override
    public double getValue(String ticker) {
        return 0;
    }
}
