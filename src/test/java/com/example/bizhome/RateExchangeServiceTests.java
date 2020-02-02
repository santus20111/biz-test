package com.example.bizhome;

import com.example.bizhome.service.impl.exchange.RateExchangeService;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.mockserver.MockServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@RunWith(JUnit4.class)
public class RateExchangeServiceTests {

    private static MockServerClient mockServer;

    @BeforeClass
    public static void startServer() {
        mockServer = startClientAndServer(8080);
    }

    @AfterClass
    public static void stopServer() {
        mockServer.stop();
    }

    @Test
    public void testExchange() {
        mockServer.when(
                HttpRequest.request()
                        .withMethod("GET")
        ).respond(HttpResponse.response()
                .withStatusCode(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"rates\":{\"RUB\":70.3375},\"base\":\"EUR\",\"date\":\"2020-01-31\"}"));

        WebClient webClient = WebClient.create("http://localhost:8080/");
        RateExchangeService service = new RateExchangeService(webClient);
        Assert.assertEquals(70.3375F, service.getValue("RUB", LocalDate.now()).block(), 0.0);
    }
}
