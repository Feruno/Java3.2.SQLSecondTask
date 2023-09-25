package ru.netology.page;

import ru.netology.datatest.DataHelp;
import ru.netology.datatest.DataHelp;

import static com.codeborne.selenide.Selenide.$;

public class LoginPageV1 {
    public VerificationPage validLogin(DataHelp.AuthInfo info) {
        $("[data-test-id=login] input").setValue(info.getLogin());
        $("[data-test-id=password] input").setValue(info.getPassword());
        $("[data-test-id='action-login'] span").click();

        return new VerificationPage();
    }
}
