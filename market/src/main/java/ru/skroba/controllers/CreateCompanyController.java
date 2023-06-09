package ru.skroba.controllers;

import io.netty.handler.codec.http.HttpMethod;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import ru.skroba.exceptions.ControllerException;
import ru.skroba.model.BaseMessage;
import ru.skroba.service.CompanyStocksService;
import rx.Observable;

import java.util.List;

public class CreateCompanyController extends BaseStockController {
    private static final String COMPANY = "company";
    private static final String RATE = "rate";
    
    public CreateCompanyController(final CompanyStocksService service) {
        super("/stocks/company/add", HttpMethod.POST, service);
    }
    
    @Override
    protected <T> Observable<String> process(final HttpServerRequest<T> request) {
        checkRequestParameters(request, List.of(COMPANY, RATE)).ifPresent(res -> {
            throw new ControllerException(400, res);
        });
        
        String companyName = getQueryParam(request, COMPANY);
        double rate = getDoubleParam(request, RATE);
        
        return service.addCompany(companyName, rate)
                .map(it -> new BaseMessage(201, "Success").toString());
    }
}
