package ru.netology.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.netology.datatest.DataHelp;
import ru.netology.datatest.SQLHelp;
import ru.netology.page.LoginPageV1;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.datatest.SQLHelp.cleanDatabase;

public class appDeadlineTest {

    @AfterAll
    static void teardown() {
        //cleanDatabase();
    }

    @Test
    void succesLogin() {
        var login = open("http://localhost:9999", LoginPageV1.class);
        var authInfo = DataHelp.getAuthInfo();
        var verification = login.validLogin(authInfo);
        verification.verificationPageVisible();
        var verifCode = SQLHelp.getVerifCode();
        verification.validVerify(verifCode.getCode());
    }

    @Test
    void invalidVerificationCode() {
        var login = open("http://localhost:9999", LoginPageV1.class);
        var authInfo = DataHelp.getAuthInfo();
        var verification = login.validLogin(authInfo);
        verification.verificationPageVisible();
        var verifCode = DataHelp.getInvalidVerificationCodeFor();
        verification.verify(verifCode.getCode());
        verification.verifyCodeErrorVisible();
    }

    @Test
    void requestTransfer() {
        DataHelp.sendRequestSpecAuth();

        var verifCode = SQLHelp.getVerifCode().getCode();

        System.out.println("ОТВЕТ verifCode =!=!=!=!= " + verifCode);

        var token = DataHelp.sendRequestSpecVerification(verifCode);

        System.out.println("ОТВЕТ T_O_K_E_N =!=!=!=!= " + token);

        var cards = DataHelp.sendRequestSpecCards(token);

        System.out.println("ОТВЕТ cards =!=!=!=!= " + cards);

        var cardBalance = DataHelp.sendRequestSpecCards(token);

        var firstCardBalance = cardBalance.get(DataHelp.getFirstCadr().getId());

        var secondCardBalance = cardBalance.get(DataHelp.getSecondCadr().getId());

        var amount = DataHelp.generateAmount(firstCardBalance);

        var infoTransfer = new DataHelp.APICardInfoForTransfer(  DataHelp.getSecondCadr().getNum(), DataHelp.getFirstCadr().getNum(), amount); //err

        DataHelp.sendRequestSpecTransfer(token, infoTransfer);

        cardBalance = DataHelp.sendRequestSpecCards(token);

        var actualFirstCardBalance = cardBalance.get(DataHelp.getFirstCadr().getId());

        var actualSecondCardBalance = cardBalance.get(DataHelp.getSecondCadr().getId());

        System.out.println("сумма на первой карте "+ (firstCardBalance - amount) +" сумма на первой карте (actual) "+ actualFirstCardBalance);

        System.out.println("сумма на второй карте "+ (secondCardBalance + amount) +" сумма на второй карте (actual) "+ actualSecondCardBalance);

        assertAll(() -> assertEquals(firstCardBalance - amount, actualFirstCardBalance),
                () -> assertEquals(secondCardBalance + amount, actualSecondCardBalance));
    }

}
