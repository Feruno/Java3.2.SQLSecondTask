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
}
