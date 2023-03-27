package ru.skroba;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.server.HttpServer;
import ru.skroba.configuration.UserConfiguration;
import ru.skroba.controllers.Controller;

public class UserWorker {
    
    public static void main(String[] args) {
        UserConfiguration configuration = new UserConfiguration();
        
        new UserWorker().run(configuration)
                .awaitShutdown();
    }
    
    public HttpServer<ByteBuf, ByteBuf> run(UserConfiguration configuration) {
        Controller controller = configuration.getController();
        return HttpServer.newServer(configuration.getServerPort())
                .start(((request, response) -> response.writeString(controller.processRequest(request))));
    }
}
