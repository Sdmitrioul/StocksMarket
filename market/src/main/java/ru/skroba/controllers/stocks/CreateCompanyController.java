package ru.skroba.controllers.stocks;

import io.netty.handler.codec.http.HttpMethod;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import ru.skroba.exceptions.ControllerException;
import ru.skroba.exceptions.ServiceException;
import ru.skroba.model.BaseMessage;
import ru.skroba.service.CompanyStocksService;
import rx.Observable;

import java.util.List;

import static ru.skroba.model.stocks.CompanyStocksFactory.COMPANY_NAME;

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
        
        String companyName = getQueryParam(request, COMPANY_NAME);
        double rate = getDoubleParam(request, RATE);
        
        try {
            return service.addCompany(companyName, rate)
                    .map(it -> new BaseMessage(201, "Success").toString());
        } catch (ServiceException e) {
            throw new ControllerException(400, e.getMessage(), e);
        }
    }
}
