package tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pages.MainPage;
import pages.OrderPage;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class OrderTest extends TestBase {

    private final String buttonLocation;
    private final String name;
    private final String surname;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final String date;
    private final int rentalDays;
    private final String color;
    private final String comment;

    public OrderTest(String buttonLocation, String name, String surname, String address,
                     String metroStation, String phone, String date, int rentalDays,
                     String color, String comment) {
        this.buttonLocation = buttonLocation;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.date = date;
        this.rentalDays = rentalDays;
        this.color = color;
        this.comment = comment;
    }

    @Parameterized.Parameters(name = "Кнопка {0}, данные: {1} {2}")
    public static Object[][] data() {
        return new Object[][]{
                {"top", "Иван", "Петров", "Ленина 1", "Сокольники", "+79998887766", "01.06.2026", 1, "black", "Позвоните за 10 минут"},
                {"bottom", "Анна", "Смирнова", "Мира 10", "Черкизовская", "+71112223344", "15.06.2026", 2, "grey", "Домофон 123"}
        };
    }

    @Test
    public void createOrder() {
        MainPage mainPage = new MainPage(driver);
        if (buttonLocation.equals("top")) {
            mainPage.clickTopOrderButton();
        } else {
            mainPage.clickBottomOrderButton();
        }

        OrderPage orderPage = new OrderPage(driver);
        orderPage.fillFirstForm(name, surname, address, metroStation, phone);
        orderPage.fillSecondForm(date, rentalDays, color, comment);
        orderPage.confirmOrder();

        assertTrue("Заказ не был создан", orderPage.isOrderSuccess());
    }
}