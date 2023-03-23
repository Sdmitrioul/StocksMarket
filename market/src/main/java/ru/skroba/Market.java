package ru.skroba;

import io.reactivex.netty.protocol.http.server.HttpServer;
import ru.skroba.configuration.MarketConfiguration;
import ru.skroba.controllers.Controller;

public class Market {
    public static void main(String[] args) {
        MarketConfiguration configuration = new MarketConfiguration();
        
        new Market().run(configuration);
    }
    
    public void run(MarketConfiguration configuration) {
        Controller controller = configuration.getController();
        HttpServer.newServer(configuration.getServerPort())
                .start(((request, response) -> response.writeString(controller.processRequest(request))))
                .awaitShutdown();
    }
}