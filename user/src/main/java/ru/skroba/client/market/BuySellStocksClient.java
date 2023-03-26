package ru.skroba.client.market;

import com.google.gson.JsonParser;
import com.mongodb.rx.client.Success;
import ru.skroba.client.BaseClient;
import ru.skroba.exceptions.ClientException;
import rx.Observable;

import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public abstract class BuySellStocksClient extends BaseClient {
    private final static String PATH = "/stocks/";
    
    private static final String COMPANY = "company";
    private static final String AMOUNT = "amount";
    
    protected BuySellStocksClient(final String host, final String pathPart) {
        super(host, PATH + pathPart);
    }
    
    public Observable<Success> sendRequest(String company, long count) {
        try {
            var uri = buildUri(Map.of(COMPANY, company, AMOUNT, count));
            var request = HttpRequest.newBuilder()
                    .uri(uri)
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
            
            var result = client.send(request, HttpResponse.BodyHandlers.ofString())
                    .body();
            
            var json = JsonParser.parseString(result)
                    .getAsJsonObject();
            
            if (!json.has("code") || json.get("code")
                    .getAsLong() != 200 || !json.has("message") || !json.get("message")
                    .getAsString()
                    .equals("Success")) {
                return Observable.error(new SerialException("Can't sell stocks"));
            }
            
            return Observable.just(Success.SUCCESS);
        } catch (URISyntaxException e) {
            return Observable.error(new ClientException("Exception while creating URI!"));
        } catch (IOException e) {
            return Observable.error(new ClientException("Can't get message from socket"));
        } catch (InterruptedException e) {
            return Observable.error(new ClientException("Operation with sending message interuppted!"));
        }
    }
}
