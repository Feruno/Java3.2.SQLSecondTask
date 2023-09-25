package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.datatest.DataHelp;

import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private SelenideElement codeVerif = $("[data-test-id='code'] input");
    private SelenideElement buttonVerif = $("[data-test-id='action-verify'] span");
    private SelenideElement errorVerif = $("[data-test-id='error-notification']");

    public void verificationPageVisible() {
        codeVerif.shouldBe(Condition.visible);
    }

    public void verifyCodeErrorVisible() {
        errorVerif.shouldBe(Condition.visible);
    }

    public DashBoardPage validVerify(String verificationCode) {
        //codeVerif.setValue(verificationCode);
        //buttonVerif.click();
        verify(verificationCode);
        return new DashBoardPage();
    }
    public void verify(String verificationCode) {
        codeVerif.setValue(verificationCode);
        buttonVerif.click();
    }
}