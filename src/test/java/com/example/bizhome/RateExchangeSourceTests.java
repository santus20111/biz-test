package com.example.bizhome;

import com.example.bizhome.service.exception.CurrencyValueNotFoundException;
import com.example.bizhome.service.impl.exchange.RateExchangeSource;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@RunWith(JUnit4.class)
public class RateExchangeSourceTests {

    private MockServerClient mockServer;

    @Before
    public void startServer() {
        mockServer = startClientAndServer(8080);
    }

    @After
    public void stopServer() {
        mockServer.stop();
    }

    @Test
    public void testExchangeRequest() {
        mockServer.when(
                HttpRequest.request()
                        .withMethod("GET")
        ).respond(HttpResponse.response()
                .withStatusCode(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"rates\":{\"RUB\":70.3375},\"base\":\"EUR\",\"date\":\"2020-01-31\"}"));

        WebClient webClient = WebClient.create("http://localhost:8080/");
        RateExchangeSource service = new RateExchangeSource(webClient);
        Assert.assertEquals(70.3375F, service.getValue("RUB", LocalDate.now()).getAnswer().block(), 0.0);
    }


    @Test
    public void testFailedExchangeRequest() {
        mockServer.when(
                HttpRequest.request()
                        .withMethod("GET")
        ).respond(HttpResponse.response()
                .withStatusCode(400)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"error\":\"Symbols 'RUL' are invalid for date 2020-02-03.\"}"));

        WebClient webClient = WebClient.create("http://localhost:8080/");
        RateExchangeSource service = new RateExchangeSource(webClient);

        try {
            service.getValue("RUL", LocalDate.now()).getAnswer().block();
            Assert.fail();
        } catch (Throwable e) {
            Assert.assertTrue(true);
        }
    }
}
