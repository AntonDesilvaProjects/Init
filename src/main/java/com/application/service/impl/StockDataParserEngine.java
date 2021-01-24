package com.application.service.impl;

import com.application.service.StockDataService;
import com.init.annotation.Init;
import com.init.annotation.Injectable;

@Injectable
public class StockDataParserEngine {
    @Init
    private StockDataService stockDataService;

    @Injectable
    private  class CurrencyExchanger {
        @Init
        private StockDataService stockDataService;
    }
}
