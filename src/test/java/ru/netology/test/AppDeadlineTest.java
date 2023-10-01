package ru.netology.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.netology.datatest.APIHelper;
import ru.netology.datatest.DataHelp;
import ru.netology.datatest.SQLHelp;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppDeadlineTest {

    @AfterAll
    static void teardown() {
        //cleanDatabase();
    }

    @Test
    void requestTransfer() {
        var authInfo = DataHelp.getAuthInfo();

        APIHelper.sendRequestSpecAuth(authInfo, 200);

        var verifCode = SQLHelp.getVerifCode().getCode();

        var verifCodeAndLogin = new APIHelper.VerificationLoginAndCode(authInfo.getLogin(), verifCode);

        var token = APIHelper.sendRequestSpecVerification(verifCodeAndLogin, 200);

        var cards = APIHelper.sendRequestSpecCards(token, 200);

        var cardBalance = APIHelper.sendRequestSpecCards(token, 200);

        var firstCardBalance = cardBalance.get(DataHelp.getFirstCadr().getId());

        var secondCardBalance = cardBalance.get(DataHelp.getSecondCadr().getId());

        var amount = DataHelp.generateAmount(firstCardBalance);

        var infoTransfer = new APIHelper.APICardInfoForTransfer( DataHelp.getSecondCadr().getNum(), DataHelp.getFirstCadr().getNum(), amount ); //err

        APIHelper.sendRequestSpecTransfer(token, infoTransfer, 200);

        cardBalance = APIHelper.sendRequestSpecCards(token, 200);

        var actualFirstCardBalance = cardBalance.get(DataHelp.getFirstCadr().getId());

        var actualSecondCardBalance = cardBalance.get(DataHelp.getSecondCadr().getId());

        assertAll(() -> assertEquals(firstCardBalance - amount, actualFirstCardBalance),
                () -> assertEquals(secondCardBalance + amount, actualSecondCardBalance));
    }

}
