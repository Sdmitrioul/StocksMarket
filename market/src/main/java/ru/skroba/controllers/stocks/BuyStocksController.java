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

public class BuyStocksController extends BaseStockController {
    private static final String COMPANY = "company";
    private static final String AMOUNT = "amount";
    
    public BuyStocksController(final CompanyStocksService service) {
        super("/stocks/buy", HttpMethod.POST, service);
    }
    
    @Override
    protected <T> Observable<String> process(final HttpServerRequest<T> request) throws ControllerException {
        checkRequestParameters(request, List.of(COMPANY, AMOUNT)).ifPresent(res -> {
            throw new ControllerException(400, res);
        });
        
        String companyName = getQueryParam(request, COMPANY_NAME);
        long amount = getLongParam(request, AMOUNT);
        
        try {
            return service.buyStocks(companyName, amount)
                    .map(it -> new BaseMessage(200, "Success").toString());
        } catch (ServiceException e) {
            throw new ControllerException(400, e.getMessage(), e);
        }
    }
}
