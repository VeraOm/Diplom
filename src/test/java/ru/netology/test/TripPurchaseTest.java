package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.data.*;
import ru.netology.page.TripPurchasePage;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

public class TripPurchaseTest {

    private static final String approvedStatus = "APPROVED";
    private static final String declinedStatus = "DECLINED";

    private TripPurchasePage page = new TripPurchasePage();

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    void successPaymentTest() throws SQLException {
        OrderEntity lastOrder = DataHelper.getLastOrder();
        BankCard card = DataHelper.getValidApprovedCard();
        page.pushPaymentButton();
        page.fillForm(card);
        page.haveApprovedNotification();

        OrderEntity newOrder = DataHelper.getLastOrder();
        assertNotEquals(lastOrder.getId(), newOrder.getId());
        PaymentEntity payment = DataHelper.getOrderPayment(newOrder);
        assertEquals(approvedStatus, payment.getStatus());
        assertEquals(4500000, payment.getAmount());
    }

    @Test
    void declinedCardPaymentTest() throws SQLException {
        OrderEntity lastOrder = DataHelper.getLastOrder();
        BankCard card = DataHelper.getValidDeclinedCard();
        page.pushPaymentButton();
        page.fillForm(card);
        page.haveNotApprovedNotification();

        OrderEntity newOrder = DataHelper.getLastOrder();
        assertNotEquals(lastOrder.getId(), newOrder.getId());
        PaymentEntity payment = DataHelper.getOrderPayment(newOrder);
        assertEquals(declinedStatus, payment.getStatus());
    }

    @Test
    void invalidHolderTest() throws SQLException {
        OrderEntity lastOrder = DataHelper.getLastOrder();
        BankCard card = DataHelper.getInvalidHolderCard();
        page.pushPaymentButton();
        page.fillForm(card);
        page.haveNotApprovedNotification();

        OrderEntity newOrder = DataHelper.getLastOrder();
        assertEquals(lastOrder.getId(), newOrder.getId());
    }

    @Test
    void emptyHolderTest() throws SQLException {
        OrderEntity lastOrder = DataHelper.getLastOrder();
        BankCard card = DataHelper.getEmptyHolderCard();
        page.pushPaymentButton();
        page.fillForm(card);
        page.haveNotApprovedNotification();
        page.haveHolderValueWarn("обязательно");

        OrderEntity newOrder = DataHelper.getLastOrder();
        assertEquals(lastOrder.getId(), newOrder.getId());
    }

    @Test
    void invalidCardNumberTest() throws SQLException {
        OrderEntity lastOrder = DataHelper.getLastOrder();
        BankCard card = DataHelper.getInvalidNumberCard();
        page.pushPaymentButton();
        page.fillForm(card);
        page.haveNotApprovedNotification();
        page.haveCardNumberValueWarn("формат");

        OrderEntity newOrder = DataHelper.getLastOrder();
        assertEquals(lastOrder.getId(), newOrder.getId());
    }

    @Test
    void emptyCardNumberTest() throws SQLException {
        OrderEntity lastOrder = DataHelper.getLastOrder();
        BankCard card = DataHelper.getEmptyNumberCard();
        page.pushPaymentButton();
        page.fillForm(card);
        page.haveNotApprovedNotification();
        page.haveCardNumberValueWarn("обязательно");

        OrderEntity newOrder = DataHelper.getLastOrder();
        assertEquals(lastOrder.getId(), newOrder.getId());
    }

    @Test
    void invalidMonthTest() throws SQLException {
        OrderEntity lastOrder = DataHelper.getLastOrder();
        BankCard card = DataHelper.getInvalidMonthCard();
        page.pushPaymentButton();
        page.fillForm(card);
        page.haveNotApprovedNotification();
        page.haveMonthValueWarn("срок");
        BankCard card2 = DataHelper.getInvalidFormatMonthCard();
        page.pushPaymentButton();
        page.clearForm();
        page.fillForm(card2);
        page.haveNotApprovedNotification();
        page.haveMonthValueWarn("формат");

        OrderEntity newOrder = DataHelper.getLastOrder();
        assertEquals(lastOrder.getId(), newOrder.getId());
    }

    @Test
    void emptyMonthTest() throws SQLException {
        OrderEntity lastOrder = DataHelper.getLastOrder();
        BankCard card = DataHelper.getEmptyMonthCard();
        page.pushPaymentButton();
        page.fillForm(card);
        page.haveNotApprovedNotification();
        page.haveMonthValueWarn("обязательно");

        OrderEntity newOrder = DataHelper.getLastOrder();
        assertEquals(lastOrder.getId(), newOrder.getId());
    }

    @Test
    void invalidYearTest() throws SQLException {
        OrderEntity lastOrder = DataHelper.getLastOrder();
        BankCard card = DataHelper.getInvalidYearCard();
        page.pushPaymentButton();
        page.fillForm(card);
        page.haveNotApprovedNotification();
        page.haveYearValueWarn("срок");
        BankCard card2 = DataHelper.getInvalidFormatYearCard();
        page.pushPaymentButton();
        page.clearForm();
        page.fillForm(card2);
        page.haveNotApprovedNotification();
        page.haveYearValueWarn("формат");

        OrderEntity newOrder = DataHelper.getLastOrder();
        assertEquals(lastOrder.getId(), newOrder.getId());
    }

    @Test
    void emptyYearTest() throws SQLException {
        OrderEntity lastOrder = DataHelper.getLastOrder();
        BankCard card = DataHelper.getEmptyYearCard();
        page.pushPaymentButton();
        page.fillForm(card);
        page.haveNotApprovedNotification();
        page.haveYearValueWarn("обязательно");

        OrderEntity newOrder = DataHelper.getLastOrder();
        assertEquals(lastOrder.getId(), newOrder.getId());
    }

    @Test
    void expiredTermTest() throws SQLException {
        OrderEntity lastOrder = DataHelper.getLastOrder();
        BankCard card = DataHelper.getExpiredCard();
        page.pushPaymentButton();
        page.fillForm(card);
        page.haveNotApprovedNotification();
        page.haveExpiredTermWarn("срок");

        OrderEntity newOrder = DataHelper.getLastOrder();
        assertEquals(lastOrder.getId(), newOrder.getId());
    }

    @Test
    void invalidCvcTest() throws SQLException {
        OrderEntity lastOrder = DataHelper.getLastOrder();
        BankCard card = DataHelper.getInvalidCvcCard();
        page.pushPaymentButton();
        page.fillForm(card);
        page.haveNotApprovedNotification();
        page.haveCvcValueWarn("формат");

        OrderEntity newOrder = DataHelper.getLastOrder();
        assertEquals(lastOrder.getId(), newOrder.getId());
    }

    @Test
    void emptyCvcTest() throws SQLException {
        OrderEntity lastOrder = DataHelper.getLastOrder();
        BankCard card = DataHelper.getEmptyCvcCard();
        page.pushPaymentButton();
        page.fillForm(card);
        page.haveNotApprovedNotification();
        page.haveCvcValueWarn("обязательно");

        OrderEntity newOrder = DataHelper.getLastOrder();
        assertEquals(lastOrder.getId(), newOrder.getId());
    }

    @Test
    void notExistCardPaymentTest() throws SQLException {
        OrderEntity lastOrder = DataHelper.getLastOrder();
        BankCard card = DataHelper.getNotExistNumberCard();
        page.pushPaymentButton();
        page.fillForm(card);
        page.haveDeclinedNotification();

        OrderEntity newOrder = DataHelper.getLastOrder();
        assertEquals(lastOrder.getId(), newOrder.getId());
    }

    @Test
    void clearFieldsErrorsTest() {
        BankCard card = DataHelper.getEmptyCard();
        page.pushPaymentButton();
        page.fillForm(card);
        BankCard card2 = DataHelper.getNotExistNumberCard();
        page.clearForm();
        page.fillForm(card2);

        page.isWarnsInvisible();
    }

    @Test
    void successCreditRequestTest() throws SQLException {
        OrderEntity lastOrder = DataHelper.getLastOrder();
        BankCard card = DataHelper.getValidApprovedCard();
        page.pushCreditButton();
        page.fillForm(card);
        page.haveApprovedNotification();

        OrderEntity newOrder = DataHelper.getLastOrder();
        assertNotEquals(lastOrder.getId(), newOrder.getId());
        CreditRequestEntity credit = DataHelper.getOrderCreditRegistry(newOrder);
        assertEquals(approvedStatus, credit.getStatus());
    }

    @Test
    void declinedCardCreditRequestTest() throws SQLException {
        OrderEntity lastOrder = DataHelper.getLastOrder();
        BankCard card = DataHelper.getValidDeclinedCard();
        page.pushCreditButton();
        page.fillForm(card);
        page.haveNotApprovedNotification();

        OrderEntity newOrder = DataHelper.getLastOrder();
        assertNotEquals(lastOrder.getId(), newOrder.getId());
        CreditRequestEntity credit = DataHelper.getOrderCreditRegistry(newOrder);
        assertEquals(declinedStatus, credit.getStatus());
    }
}
