package com.example.bizhome;

import com.example.bizhome.web.ConvertController;
import com.example.bizhome.web.request.ConvertRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConvertControllerTests {

    @LocalServerPort
    private int port;

    private WebTestClient getWebTestClient() {
        return WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:" + port + "/")
                .build();
    }

    @Test
    void testValidRequest() {
        getWebTestClient()
                .post()
                .uri("/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{\n" +
                        "\t\"baseCurrency\": \"RUB\",\n" +
                        "\t\"convertCurrency\": \"USD\",\n" +
                        "\t\"sum\": 100000,\n" +
                        "\t\"date\": \"2019-12-13\"\n" +
                        "}"))
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    void testInvalidBaseCurrencyRequest() {
        getWebTestClient()
                .post()
                .uri("/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{\n" +
                        "\t\"baseCurrency\": \"RUL\",\n" +
                        "\t\"convertCurrency\": \"USD\",\n" +
                        "\t\"sum\": 100000,\n" +
                        "\t\"date\": \"2019-12-13\"\n" +
                        "}"))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody().jsonPath("errors[0].field").isEqualTo("baseCurrency");
    }

    @Test
    void testInvalidConvertCurrencyRequest() {
        getWebTestClient()
                .post()
                .uri("/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{\n" +
                        "\t\"baseCurrency\": \"RUB\",\n" +
                        "\t\"convertCurrency\": \"USL\",\n" +
                        "\t\"sum\": 100000,\n" +
                        "\t\"date\": \"2019-12-13\"\n" +
                        "}"))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody().jsonPath("errors[0].field").isEqualTo("convertCurrency");
    }

    @Test
    void testConvertCurrencyRequiredRequest() {
        getWebTestClient()
                .post()
                .uri("/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{\n" +
                        "\t\"baseCurrency\": \"RUB\",\n" +
                        "\t\"sum\": 100000,\n" +
                        "\t\"date\": \"2019-12-13\"\n" +
                        "}"))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody().jsonPath("errors[0].field").isEqualTo("convertCurrency");
    }


    @Test
    void testSumRequiredRequest() {
        getWebTestClient()
                .post()
                .uri("/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{\n" +
                        "\t\"baseCurrency\": \"RUB\",\n" +
                        "\t\"convertCurrency\": \"USD\",\n" +
                        "\t\"date\": \"2019-12-13\"\n" +
                        "}"))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody().jsonPath("errors[0].field").isEqualTo("sum");;
    }

    @Test
    void testSumOnlyPositiveRequest() {
        getWebTestClient()
                .post()
                .uri("/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{\n" +
                        "\t\"baseCurrency\": \"RUB\",\n" +
                        "\t\"convertCurrency\": \"USD\",\n" +
                        "\t\"sum\": -100000,\n" +
                        "\t\"date\": \"2019-12-13\"\n" +
                        "}"))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody().jsonPath("errors[0].field").isEqualTo("sum");
    }

    
    @Test
    void testDateInvalidFormatRequest() {
        getWebTestClient()
                .post()
                .uri("/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{\n" +
                        "\t\"baseCurrency\": \"RUB\",\n" +
                        "\t\"convertCurrency\": \"USD\",\n" +
                        "\t\"sum\": 100000,\n" +
                        "\t\"date\": \"201-12-13\"\n" +
                        "}"))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody().jsonPath("errors[0].field").isEqualTo("date");
    }

    @Test
    void testDateLaterNowRequest() {
        getWebTestClient()
                .post()
                .uri("/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{\n" +
                        "\t\"baseCurrency\": \"RUB\",\n" +
                        "\t\"convertCurrency\": \"USD\",\n" +
                        "\t\"sum\": 100000,\n" +
                        "\t\"date\": \"" + LocalDate.now().plusDays(1).toString() + "\"\n" +
                        "}"))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody().jsonPath("errors[0].field").isEqualTo("date");
    }
}
