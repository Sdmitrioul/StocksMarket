package ru.skroba.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpMethod;
import ru.skroba.service.CompanyStocksService;

public abstract class BaseStockController extends BaseController {
    protected final CompanyStocksService service;
    
    protected final ObjectMapper json;
    
    public BaseStockController(final String controllerPath, final HttpMethod controllerMethod,
                               final CompanyStocksService service) {
        super(controllerPath, controllerMethod);
        this.service = service;
        this.json = new ObjectMapper();
    }
}
