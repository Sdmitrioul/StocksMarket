import org.junit.ClassRule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.PullPolicy;
import ru.skroba.UserWorker;
import ru.skroba.configuration.TestUserConfiguration;

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
    private static final String DEFAULT_HOST = "http://localhost:8080";
    private static final String ACCOUNT_HOST = "http://localhost:8090";
    private final static String USER = "DmitriSkroba";
    private final static int USER_INITIAL_MONEY = 9999;
    private static UserWorker worker;
    
    @BeforeAll
    static void beforeAll() throws Exception {
        market.start();
        var conf = new TestUserConfiguration();
        worker = new UserWorker();
        worker.run(conf);
        
        addCompany();
        addUser();
    }
    
    static void addCompany() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(DEFAULT_HOST + "/stocks/company/add?company=JB&rate=10"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        
        HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
    }
    
    static void addUser() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(ACCOUNT_HOST + "/user/register?user=" + USER + "&balance=" + USER_INITIAL_MONEY))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        
        var res = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
    }
    
    @AfterAll
    static void afterAll() {
        market.stop();
    }
    
    @Test
    void getCompany() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/"))
                .GET()
                .build();
        
        var res = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        
        assertEquals("{\"code\":200,\"stocks\":[{\"companyName\":\"JB\",\"price\":10.0,\"count\":100}]}", res.body());
    }
    
    @Test
    void getUser() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(ACCOUNT_HOST + "/user/savings?uid=" + USER.hashCode()))
                .GET()
                .build();
        
        var res = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        
        assertEquals("{\"code\": 200, \"message\": \"9999.0\"}", res.body());
    }
}
