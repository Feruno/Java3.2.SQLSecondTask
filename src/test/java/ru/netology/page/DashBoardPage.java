package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashBoardPage {
    private SelenideElement head = $("[data-test-id='dashboard']");
    private ElementsCollection card = $$(".list__item");
    private ElementsCollection cardButton = $$("[data-test-id='action-deposit']");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";
    public DashBoardPage() {
        head.shouldBe(Condition.visible);
    }

    public void verifyDashboadrPage() {
        head.shouldBe(Condition.visible);
    }
}
