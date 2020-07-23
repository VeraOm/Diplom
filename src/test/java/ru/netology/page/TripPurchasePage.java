package ru.netology.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import ru.netology.data.BankCard;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class TripPurchasePage {
    private SelenideElement paymentButton = $$("button[type='button'][role='button'] span.button__text").find(exactText("Купить"));
    private SelenideElement creditButton = $$("button[type='button'][role='button'] span.button__text").find(exactText("Купить в кредит"));
    private SelenideElement continueButton = $$("button[type='button'][role='button'] span.button__text").find(text("Продолжить"));

    private SelenideElement paymentTitle = $$("#root>div>h3.heading.heading_size_m.heading_theme_alfa-on-white").find(exactText("Оплата по карте"));
    private SelenideElement creditTitle = $$("#root>div>h3.heading.heading_size_m.heading_theme_alfa-on-white").find(exactText("Кредит по данным карты"));

    private SelenideElement approvedNotification = $("div.notification.notification_status_ok .notification__content");
    private SelenideElement declinedNotification = $("div.notification.notification_status_error .notification__content");

    private SelenideElement cardNumberField;
    private SelenideElement monthField;
    private SelenideElement yearField;
    private SelenideElement holderField;
    private SelenideElement cvcField;

    private SelenideElement cardNumberWarn;
    private SelenideElement monthWarn;
    private SelenideElement yearWarn;
    private SelenideElement holderWarn;
    private SelenideElement cvcWarn;

    public TripPurchasePage() {
        open("http://localhost:8080");
        paymentButton.shouldBe(visible);
        creditButton.shouldBe(visible);
        continueButton.shouldNotBe(visible);

        ElementsCollection formElements = $$("#root>div>form.form.form_size_m.form_theme_alfa-on-white .input__inner>.input__top");

        cardNumberField = formElements.find(exactText("Номер карты")).parent().$(".input__box>input.input__control");
        cardNumberWarn = cardNumberField.parent().parent().$("span.input__sub");
        monthField = formElements.find(exactText("Месяц")).parent().$(".input__box>input.input__control");
        monthWarn = monthField.parent().parent().$("span.input__sub");
        yearField = formElements.find(exactText("Год")).parent().$(".input__box>input.input__control");
        yearWarn = formElements.find(exactText("Год")).parent().$("span.input__sub");
        holderField = formElements.find(exactText("Владелец")).parent().$(".input__box>input.input__control");
        holderWarn = formElements.find(exactText("Владелец")).parent().$("span.input__sub");
        cvcField = formElements.find(exactText("CVC/CVV")).parent().$(".input__box>input.input__control");
        cvcWarn = formElements.find(exactText("CVC/CVV")).parent().$("span.input__sub");
    }

    private void isFormElementsVisible() {
        cardNumberField.shouldBe(visible);
        monthField.shouldBe(visible);
        yearField.shouldBe(visible);
        holderField.shouldBe(visible);
        cvcField.shouldBe(visible);
        continueButton.shouldBe(visible);
    }

    public void pushPaymentButton() {
        paymentButton.click();
        paymentTitle.shouldBe(visible);
        isFormElementsVisible();
    }

    public void pushCreditButton() {
        creditButton.click();
        creditTitle.shouldBe(visible);
        isFormElementsVisible();
    }

    public void clearForm() {
        cardNumberField.sendKeys(Keys.LEFT_CONTROL + "a" + Keys.BACK_SPACE);
        monthField.sendKeys(Keys.LEFT_CONTROL + "a" + Keys.BACK_SPACE);
        yearField.sendKeys(Keys.LEFT_CONTROL + "a" + Keys.BACK_SPACE);
        holderField.sendKeys(Keys.LEFT_CONTROL + "a" + Keys.BACK_SPACE);
        cvcField.sendKeys(Keys.LEFT_CONTROL + "a" + Keys.BACK_SPACE);
    }

    public void fillForm(BankCard card) {
        cardNumberField.setValue(card.getCardNumber());
        monthField.setValue(card.getExpireMonth());
        yearField.setValue(card.getExpireYear());
        holderField.setValue(card.getHolderName());
        cvcField.setValue(card.getCvcCode());
        continueButton.click();
        continueButton.waitUntil(enabled, 15000);
    }

    public void haveCardNumberValueWarn(String warnText) {
        cardNumberWarn.shouldBe(visible);
        cardNumberWarn.shouldHave(text(warnText));
    }

    public void haveMonthValueWarn(String warnText) {
        monthWarn.shouldBe(visible);
        monthWarn.shouldHave(text(warnText));
    }

    public void haveYearValueWarn(String warnText) {
        yearWarn.shouldBe(visible);
        yearWarn.shouldHave(text(warnText));
    }

    public void haveExpiredTermWarn(String warnText) {
        if (yearWarn.isDisplayed()) {
            yearWarn.shouldHave(visible);
            yearWarn.shouldHave(text(warnText));
        } else {
            monthWarn.shouldHave(visible);
            monthWarn.shouldHave(text(warnText));
        }
    }

    public void haveHolderValueWarn(String warnText) {
        holderWarn.shouldBe(visible);
        holderWarn.shouldHave(text(warnText));
    }

    public void haveCvcValueWarn(String warnText) {
        cvcWarn.shouldBe(visible);
        cvcWarn.shouldHave(text(warnText));
    }

    public void isWarnsInvisible() {
        cardNumberWarn.shouldNotBe(visible);
        monthWarn.shouldNotBe(visible);
        yearWarn.shouldNotBe(visible);
        holderWarn.shouldNotBe(visible);
        cvcWarn.shouldNotBe(visible);
    }

    public void haveApprovedNotification() {
        approvedNotification.shouldBe(visible);
    }

    public void haveDeclinedNotification() {
        declinedNotification.shouldBe(visible);
    }

    public void haveNotApprovedNotification() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        approvedNotification.shouldNotBe(visible);
    }
}