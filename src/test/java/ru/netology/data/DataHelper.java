package ru.netology.data;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;

public class DataHelper {

    private static Connection connection;
    private static final Faker faker = new Faker();
    private static QueryRunner runner = new QueryRunner();

    private static final String approvedCardNumber = "4444 4444 4444 4441";
    private static final String declinedCardNumber = "4444 4444 4444 4442";
    private static final String notExistCardNumber = "4444 4444 4444 4443";

    static {
        try (FileInputStream appPropertiesFile = new FileInputStream("./application.properties")) {
            Properties appProperties = new Properties();
            appProperties.load(appPropertiesFile);

            connection = DriverManager.getConnection(appProperties.getProperty("spring.datasource.url"),
                    appProperties.getProperty("spring.datasource.username"),
                    appProperties.getProperty("spring.datasource.password")
            );
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
    }

    private static BankCard getBaseCard(String cardNumber) {
        String month = String.format("%2d", faker.number().numberBetween(1, 12)).replace(" ", "0");
        int numberYear = Calendar.getInstance().get(Calendar.YEAR);
        String year = Integer.toString(faker.number().numberBetween(numberYear + 1, numberYear + 5)).substring(2);
        String holder = faker.name().name();
        String cvc = faker.number().digits(3);
        return new BankCard(cardNumber, month, year, holder, cvc);
    }

    public static BankCard getValidApprovedCard() {
        return getBaseCard(approvedCardNumber);
    }

    public static BankCard getValidDeclinedCard() {
        return getBaseCard(declinedCardNumber);
    }

    public static BankCard getInvalidNumberCard() {
        return getBaseCard("q" + approvedCardNumber.substring(1));
    }

    public static BankCard getNotExistNumberCard() {
        return getBaseCard(notExistCardNumber);
    }

    public static BankCard getEmptyNumberCard() {
        return getBaseCard("");
    }

    public static BankCard getEmptyCard() {
        return new BankCard();
    }

    public static BankCard getInvalidMonthCard() {
        BankCard card = getBaseCard(approvedCardNumber);
        card.setExpireMonth(Integer.toString(faker.number().numberBetween(13, 99)));
        return card;
    }

    public static BankCard getInvalidFormatMonthCard() {
        BankCard card = getBaseCard(approvedCardNumber);
        card.setExpireMonth(Integer.toString(faker.number().numberBetween(1, 9)));
        return card;
    }

    public static BankCard getEmptyMonthCard() {
        BankCard card = getBaseCard(approvedCardNumber);
        card.setExpireMonth("");
        return card;
    }

    public static BankCard getInvalidYearCard() {
        BankCard card = getBaseCard(approvedCardNumber);
        int numberYear = Calendar.getInstance().get(Calendar.YEAR) % 1000;
        card.setExpireYear(Integer.toString(faker.number().numberBetween(numberYear + 6, 99)));
        return card;
    }

    public static BankCard getInvalidFormatYearCard() {
        BankCard card = getBaseCard(approvedCardNumber);
        card.setExpireYear(Integer.toString(faker.number().numberBetween(1, 9)));
        return card;
    }

    public static BankCard getEmptyYearCard() {
        BankCard card = getBaseCard(approvedCardNumber);
        card.setExpireYear("");
        return card;
    }

    public static BankCard getExpiredCard() {
        BankCard card = getBaseCard(approvedCardNumber);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        int numberYear = calendar.get(Calendar.YEAR) % 1000;
        card.setExpireYear(Integer.toString(numberYear));
        int numberMonth = calendar.get(Calendar.MONTH);
        card.setExpireMonth(String.format("%2d", numberMonth).replace(" ", "0"));
        return card;
    }

    public static BankCard getInvalidHolderCard() {
        BankCard card = getBaseCard(approvedCardNumber);
        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-US"), new RandomService());
        card.setHolderName(fakeValuesService.regexify("[\\-\\=\\+\\<\\>\\!\\@\\#\\$\\%\\^\\&\\*\\{\\}]{8,15}"));
        return card;
    }

    public static BankCard getEmptyHolderCard() {
        BankCard card = getBaseCard(approvedCardNumber);
        card.setHolderName("");
        return card;
    }

    public static BankCard getInvalidCvcCard() {
        BankCard card = getBaseCard(approvedCardNumber);
        card.setCvcCode(faker.number().digits(2) + "w");
        return card;
    }

    public static BankCard getEmptyCvcCard() {
        BankCard card = getBaseCard(approvedCardNumber);
        card.setCvcCode("");
        return card;
    }

    public static OrderEntity getLastOrder() throws SQLException {
        String orderSQL = "select * from order_entity o " +
                "where o.created = (" +
                "select max(created) from order_entity);";
        OrderEntity order = runner.query(connection, orderSQL, new BeanHandler<>(OrderEntity.class));
        if (order == null) {
            order = new OrderEntity();
        }
        return order;
    }

    public static PaymentEntity getOrderPayment(OrderEntity order) throws SQLException {
        String paymentSQL = "select * from payment_entity p " +
                "where p.transaction_id = ?;";
        PaymentEntity payment = runner.query(connection, paymentSQL, new BeanHandler<>(PaymentEntity.class), order.getPayment_id());
        if (payment == null) {
            payment = new PaymentEntity();
        }
        return payment;
    }

    public static CreditRequestEntity getOrderCreditRegistry(OrderEntity order) throws SQLException {
        String creditSQL = "select * from credit_request_entity c " +
                "where c.bank_id = ?;";
        CreditRequestEntity credit = runner.query(connection, creditSQL, new BeanHandler<>(CreditRequestEntity.class), order.getCredit_id());
        if (credit == null) {
            credit = new CreditRequestEntity();
        }
        return credit;
    }
}
