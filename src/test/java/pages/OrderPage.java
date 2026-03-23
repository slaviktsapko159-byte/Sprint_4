package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OrderPage {
    private WebDriver driver;
    private final WebDriverWait wait;

    // Первая форма
    private final By nameField = By.xpath(".//input[@placeholder='* Имя']");
    private final By surnameField = By.xpath(".//input[@placeholder='* Фамилия']");
    private final By addressField = By.xpath(".//input[@placeholder='* Адрес: куда привезти заказ']");
    private final By metroStationField = By.xpath(".//input[@placeholder='* Станция метро']");
    private final By phoneField = By.xpath(".//input[@placeholder='* Телефон: на него позвонит курьер']");
    private final By nextButton = By.xpath(".//button[text()='Далее']");

    // Вторая форма
    private final By dateField = By.xpath(".//input[@placeholder='* Когда привезти самокат']");
    private final By rentalPeriodField = By.className("Dropdown-control");
    private final By rentalPeriodOption = By.xpath(".//div[contains(@class, 'Dropdown-option')]");
    private final By colorBlack = By.id("black");
    private final By colorGrey = By.id("grey");
    private final By commentField = By.xpath(".//input[@placeholder='Комментарий для курьера']");
    // Кнопка "Заказать" внизу формы – используем комбинацию классов
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
            // ignore
        }
    }

    private void jsClick(By locator) {
        removeCookieBanner();
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public void fillFirstForm(String name, String surname, String address, String metroStation, String phone) {
        removeCookieBanner();

        driver.findElement(nameField).sendKeys(name);
        driver.findElement(surnameField).sendKeys(surname);
        driver.findElement(addressField).sendKeys(address);

        WebElement metroInput = driver.findElement(metroStationField);
        metroInput.sendKeys(metroStation);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//div[contains(@class, 'select-search__select')]")));
        metroInput.sendKeys(Keys.ARROW_DOWN);
        metroInput.sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.attributeToBe(metroStationField, "value", metroStation));

        driver.findElement(phoneField).sendKeys(phone);
        jsClick(nextButton);

        wait.until(ExpectedConditions.invisibilityOfElementLocated(phoneField));
        wait.until(ExpectedConditions.visibilityOfElementLocated(dateField));
    }

    public void fillSecondForm(String date, int rentalDays, String color, String comment) {
        removeCookieBanner();

        // Поле даты
        driver.findElement(dateField).sendKeys(date);
        driver.findElement(dateField).sendKeys(Keys.ENTER);

        // Срок аренды
        driver.findElement(rentalPeriodField).click();
        driver.findElements(rentalPeriodOption).get(rentalDays - 1).click();

        // Цвет самоката
        if (color.equalsIgnoreCase("black")) {
            driver.findElement(colorBlack).click();
        } else if (color.equalsIgnoreCase("grey")) {
            driver.findElement(colorGrey).click();
        }

        // Комментарий
        driver.findElement(commentField).sendKeys(comment);

        // Нажимаем кнопку "Заказать"
        jsClick(orderButton);
    }

    public void confirmOrder() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(confirmModal));
        jsClick(yesButton);
    }

    public boolean isOrderSuccess() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(successText)).isDisplayed();
    }
}