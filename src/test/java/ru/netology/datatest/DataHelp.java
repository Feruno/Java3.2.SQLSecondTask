package ru.netology.datatest;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;

public class DataHelp {
    private static final Faker faker = new Faker(new Locale("en"));

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }
    @Value
    public static class TransferInfo {
        String cardFirst;
        String cardSecond;
        Integer amount;
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
    @Value
    public static class CardInfo {
        private String id;
        private String num;
    }

    public static int generateAmount(int balance){
        return new Random().nextInt(balance)+1;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static AuthInfo getOtherAuthInfo(AuthInfo original) {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static CardInfo getFirstCadr(){
        return new CardInfo("0f3f5c2a-249e-4c3d-8287-09f7a039391d", "5559 0000 0000 0001");
    }

    public static CardInfo getSecondCadr(){
        return new CardInfo("92df3f1c-a033-48e6-8390-206f6b1f56c0", "5559 0000 0000 0002");
    }

    @Value
    public static class VerificationCode {
        private String code;
    }

    private static String generateRandomUser() {
        return faker.name().username();
    }

    private static String generateRandomPassword() {
        return faker.internet().password();
    }

    public static VerificationCode getInvalidVerificationCodeFor() {
        return new VerificationCode(faker.numerify("#####"));
    }

    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();


    public static void sendRequestSpecAuth() {
        given()
                .spec(requestSpec)
                .body("{" +
                        "  login:" + getAuthInfo().getLogin() + "," +
                        "  password:" + getAuthInfo().getPassword() + " " +
                        "}")
                .when()
                .post("/api/auth")
                .then()
                .statusCode(200);

    }

    public static String sendRequestSpecVerification(String code) {
        Response response = given()
                .spec(requestSpec)
                .body("{" +
                        "  login:  " + getAuthInfo().getLogin() + "," +
                        "  code:   " + code +
                        "}")
                .when()
                .post("/api/auth/verification")
                .then()
                .statusCode(200)
                .extract()
                .response();

        String token = response.path("token");

        return token;

    }


    public static Map<String, Integer> sendRequestSpecCards(String token) {
        CardInfoForTransfer[] infoCard = given()
                .spec(requestSpec)
                .headers(
                        "Authorization",
                        "Bearer " +token,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .when()
                .get("/api/cards")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(CardInfoForTransfer[].class);

        Map<String, Integer> cardAmount = new HashMap<>();
        for(CardInfoForTransfer cardInfo : infoCard){
            cardAmount.put(cardInfo.getId(), cardInfo.getBalance());
        }
        return cardAmount;
    }


    public static void sendRequestSpecTransfer(String token, APICardInfoForTransfer transferInfo) {
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
                .statusCode(200);

    }
}
