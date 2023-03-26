package ru.skroba.client.market;

import com.google.gson.JsonParser;
import ru.skroba.client.BaseClient;
import ru.skroba.exceptions.ClientException;
import ru.skroba.model.stocks.CompanyStocks;
import rx.Observable;

import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StocksClient extends BaseClient {
    private final static String PATH = "/";
    
    public StocksClient(final String host) {
        super(host, PATH);
    }
    
    public Observable<List<CompanyStocks>> sendRequest() {
        try {
            var uri = buildUri(Map.of());
            var request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();
            
            var response = client.send(request, HttpResponse.BodyHandlers.ofString())
                    .body();
            
            var json = JsonParser.parseString(response)
                    .getAsJsonObject();
            
            if (!json.has("code") || json.get("code")
                    .getAsLong() != 200 || !json.has("stocks")) {
                return Observable.error(new SerialException("Can't get stocks"));
            }
            
            var result = new ArrayList<CompanyStocks>();
            
            json.get("stocks")
                    .getAsJsonArray()
                    .forEach(it -> {
                        var obj = it.getAsJsonObject();
                        
                        result.add(new CompanyStocks(obj.get("companyName")
                                .getAsString(), obj.get("price")
                                .getAsDouble(), obj.get("count")
                                .getAsLong()));
                    });
            
            return Observable.just(result);
        } catch (URISyntaxException e) {
            return Observable.error(new ClientException("Exception while creating URI!"));
        } catch (IOException e) {
            return Observable.error(new ClientException("Can't get message from socket"));
        } catch (InterruptedException e) {
            return Observable.error(new ClientException("Operation with sending message interuppted!"));
        }
    }
}
