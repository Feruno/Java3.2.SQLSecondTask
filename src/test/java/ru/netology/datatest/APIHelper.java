package ru.netology.datatest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static ru.netology.datatest.DataHelp.getAuthInfo;

public class APIHelper {

    @Value
    public static class VerificationLoginAndCode {
        String login;
        String code;
    }
    @Value
    public static class CardInfoForTransfer {
        String id;
        String number;
        Integer balance;
    }
    @Value
    public static class APICardInfoForTransfer {
        String from;
        String to;
        int amount;
    }

    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();


    public static void sendRequestSpecAuth(DataHelp.AuthInfo authInfo, Integer statusCode) {
        given()
                .spec(requestSpec)
                .body(authInfo)
                .when()
                .post("/api/auth")
                .then()
                .statusCode(statusCode);

    }

    public static String sendRequestSpecVerification(VerificationLoginAndCode verifCode, Integer statusCode) {
        Response response = given()
                .spec(requestSpec)
                .body(verifCode)
                .when()
                .post("/api/auth/verification")
                .then()
                .statusCode(statusCode)
                .extract()
                .response();

        String token = response.path("token");

        return token;

    }


    public static Map<String, Integer> sendRequestSpecCards(String token, Integer statusCode) {
        CardInfoForTransfer[] infoCard = given()
                .spec(requestSpec)
                .headers(
                        "Authorization",
                        "Bearer " + token,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .when()
                .get("/api/cards")
                .then()
                .statusCode(statusCode)
                .extract()
                .body()
                .as(CardInfoForTransfer[].class);

        Map<String, Integer> cardAmount = new HashMap<>();
        for(CardInfoForTransfer cardInfo : infoCard){
            cardAmount.put(cardInfo.getId(), cardInfo.getBalance());
        }
        return cardAmount;
    }


    public static void sendRequestSpecTransfer(String token, APICardInfoForTransfer transferInfo, Integer statusCode) {
        given()
                .spec(requestSpec)
                .headers(
                        "Authorization",
                        "Bearer " + token,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .body(transferInfo)
                .when()
                .post("/api/transfer")
                .then()
                .statusCode(statusCode);

    }
}
