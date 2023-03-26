package ru.skroba;

import io.reactivex.netty.protocol.http.server.HttpServer;
import ru.skroba.configuration.UserConfiguration;
import ru.skroba.controllers.Controller;

public class UserWorker {
    
    public static void main(String[] args) {
        UserConfiguration configuration = new UserConfiguration();
    }
    
    public void run(UserConfiguration configuration) {
        Controller controller = configuration.getController();
        HttpServer.newServer(configuration.getServerPort())
                .start(((request, response) -> response.writeString(controller.processRequest(request))))
                .awaitShutdown();
    }
}
