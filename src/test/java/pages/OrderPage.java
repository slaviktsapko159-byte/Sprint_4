package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OrderPage {
    private WebDriver driver;
    private final WebDriverWait wait;

    // Локаторы первой формы
    private final By nameField = By.xpath(".//input[@placeholder='* Имя']");
    private final By surnameField = By.xpath(".//input[@placeholder='* Фамилия']");
    private final By addressField = By.xpath(".//input[@placeholder='* Адрес: куда привезти заказ']");
    private final By metroStationField = By.xpath(".//input[@placeholder='* Станция метро']");
    private final By phoneField = By.xpath(".//input[@placeholder='* Телефон: на него позвонит курьер']");
    private final By nextButton = By.xpath(".//button[text()='Далее']");

    // Локаторы второй формы
    private final By dateField = By.xpath(".//input[@placeholder='* Когда привезти самокат']");
    private final By rentalPeriodField = By.className("Dropdown-control");
    private final By rentalPeriodOption = By.xpath(".//div[contains(@class, 'Dropdown-option')]");
    private final By colorBlack = By.id("black");
    private final By colorGrey = By.id("grey");
    private final By commentField = By.xpath(".//input[@placeholder='Комментарий для курьера']");
    private final By orderButton = By.xpath("//button[contains(@class, 'Button_Button__ra12g') and contains(@class, 'Button_Middle__1CSJM') and text()='Заказать']");

    // Модальные окна
    private final By confirmModal = By.className("Order_Modal__YZ-d3");
    private final By yesButton = By.xpath(".//button[text()='Да']");
    private final By successText = By.xpath(".//div[contains(text(), 'Заказ оформлен')]");

    // Куки-баннер
    private final By cookieBanner = By.className("App_CookieConsent__1yUIN");

    public OrderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 15);
        removeCookieBanner();
    }

    private void removeCookieBanner() {
        try {
            if (driver.findElements(cookieBanner).size() > 0) {
                ((JavascriptExecutor) driver).executeScript(
                        "var banner = document.querySelector('.App_CookieConsent__1yUIN'); if(banner) banner.remove();");
                wait.until(ExpectedConditions.invisibilityOfElementLocated(cookieBanner));
            }
        } catch (Exception e) {
        }
    }

    private void jsClick(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    // Шаги для первой формы

    public void fillName(String name) {
        driver.findElement(nameField).sendKeys(name);
    }

    public void fillSurname(String surname) {
        driver.findElement(surnameField).sendKeys(surname);
    }

    public void fillAddress(String address) {
        driver.findElement(addressField).sendKeys(address);
    }

    public void selectMetroStation(String station) {
        WebElement metroInput = driver.findElement(metroStationField);
        metroInput.sendKeys(station);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//div[contains(@class, 'select-search__select')]")));
        metroInput.sendKeys(Keys.ARROW_DOWN);
        metroInput.sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.attributeToBe(metroStationField, "value", station));
    }

    public void fillPhone(String phone) {
        driver.findElement(phoneField).sendKeys(phone);
    }

    public void clickNext() {
        jsClick(nextButton);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(phoneField));
        wait.until(ExpectedConditions.visibilityOfElementLocated(dateField));
    }

    public void fillFirstForm(String name, String surname, String address, String metroStation, String phone) {
        fillName(name);
        fillSurname(surname);
        fillAddress(address);
        selectMetroStation(metroStation);
        fillPhone(phone);
        clickNext();
    }

    // Шаги для второй формы

    public void fillDate(String date) {
        driver.findElement(dateField).sendKeys(date);
        driver.findElement(dateField).sendKeys(Keys.ENTER);
    }

    public void selectRentalPeriod(int days) {
        driver.findElement(rentalPeriodField).click();
        driver.findElements(rentalPeriodOption).get(days - 1).click();
    }

    public void selectColor(String color) {
        if (color.equalsIgnoreCase("black")) {
            driver.findElement(colorBlack).click();
        } else if (color.equalsIgnoreCase("grey")) {
            driver.findElement(colorGrey).click();
        }
    }

    public void fillComment(String comment) {
        driver.findElement(commentField).sendKeys(comment);
    }

    public void clickOrder() {
        jsClick(orderButton);
    }

    public void fillSecondForm(String date, int rentalDays, String color, String comment) {
        fillDate(date);
        selectRentalPeriod(rentalDays);
        selectColor(color);
        fillComment(comment);
        clickOrder();
    }

    // Подтверждение заказа

    public void confirmOrder() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(confirmModal));
        jsClick(yesButton);
    }

    public boolean isOrderSuccess() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(successText)).isDisplayed();
    }
}