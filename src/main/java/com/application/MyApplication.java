package com.application;

import com.application.service.StockDataService;
import com.application.service.impl.APIStockDataService;
import com.init.annotation.Init;
import com.init.annotation.InjectableApplication;
import com.init.container.ApplicationRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@InjectableApplication
public class MyApplication implements ApplicationRunner {

    @Init("someImpl")
    public StockDataService stockDataService;

    public static void main(String... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        Class<APIStockDataService> stockDataServiceClass = APIStockDataService.class;
        Constructor<APIStockDataService> no_arg_ctor = stockDataServiceClass.getConstructor();
        APIStockDataService instance = no_arg_ctor.newInstance();

        Field field = instance.getClass().getDeclaredField("timeout");
        field.setAccessible(true);
        field.set(instance, 100);

        Method method = instance.getClass().getDeclaredMethod("stockDataServiceBuilder");
        APIStockDataService result = (APIStockDataService) method.invoke(instance);

        System.out.println(result);

        //Application.configureAndRun(args);
    }

    public void run(String... args) {
        stockDataService.getValue("FB");
    }
}
