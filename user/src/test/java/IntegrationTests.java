import org.junit.ClassRule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.PullPolicy;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntegrationTests {
    @ClassRule
    public static final GenericContainer<?> market = new FixedHostPortGenericContainer(
            "market:0.1.0").withFixedExposedPort(8080, 8080)
            .withExposedPorts(8080)
            .withImagePullPolicy(PullPolicy.defaultPolicy());
    
    @BeforeAll
    static void beforeAll() {
        market.start();
    }
    
    @AfterAll
    static void afterAll() {
        market.stop();
    }
    
    @Test
    void name() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/"))
                .GET()
                .build();
        
        var res = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        
        assertEquals("{\"code\":200,\"stocks\":[{\"companyName\":\"JB\",\"price\":10.0,\"count\":100}]}", res.body());
    }
    
    
}
