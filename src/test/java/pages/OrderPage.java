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
    private final By orderButton = By.xpath(".//button[text()='Заказать']");

    // Модальные окна
    private final By confirmModal = By.className("Order_Modal__YZ-d3");
    private final By yesButton = By.xpath(".//button[text()='Да']");
    private final By successText = By.xpath(".//div[contains(text(), 'Заказ оформлен')]");

    public OrderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 15);
    }

    private void jsClick(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    // ======================== Этапы заполнения первой формы ========================

    private void fillPersonalData(String name, String surname, String address) {
        driver.findElement(nameField).sendKeys(name);
        driver.findElement(surnameField).sendKeys(surname);
        driver.findElement(addressField).sendKeys(address);
    }

    private void selectMetroStation(String metroStation) {
        WebElement metroInput = driver.findElement(metroStationField);
        metroInput.sendKeys(metroStation);
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(".//div[contains(@class, 'select-search__select')]")));
        metroInput.sendKeys(Keys.ARROW_DOWN);
        metroInput.sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.attributeToBe(metroStationField, "value", metroStation));
    }

    private void submitFirstForm(String phone) {
        driver.findElement(phoneField).sendKeys(phone);
        jsClick(nextButton);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(phoneField));
        wait.until(ExpectedConditions.visibilityOfElementLocated(dateField));
    }

    // ======================== Общедоступный метод ========================

    public void fillFirstForm(String name, String surname, String address, String metroStation, String phone) {
        fillPersonalData(name, surname, address);
        selectMetroStation(metroStation);
        submitFirstForm(phone);
    }

    // ======================== Вторая форма ========================

    public void fillSecondForm(String date, int rentalDays, String color, String comment) {
        // Поле даты
        jsClick(dateField);
        WebElement dateInput = driver.findElement(dateField);
        dateInput.sendKeys(date);
        dateInput.sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.attributeToBe(dateField, "value", date));

        // Срок аренды
        WebElement period = wait.until(ExpectedConditions.presenceOfElementLocated(rentalPeriodField));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", period);
        wait.until(ExpectedConditions.elementToBeClickable(period)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(rentalPeriodOption));
        var options = driver.findElements(rentalPeriodOption);
        int index = rentalDays - 1;
        if (index >= 0 && index < options.size()) {
            WebElement option = options.get(index);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", option);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
        } else {
            throw new IllegalArgumentException("Некорректное количество дней аренды: " + rentalDays);
        }

        // Цвет самоката
        if (color.equalsIgnoreCase("black")) {
            WebElement blackCheckbox = wait.until(ExpectedConditions.elementToBeClickable(colorBlack));
            blackCheckbox.click();
        } else if (color.equalsIgnoreCase("grey")) {
            WebElement greyCheckbox = wait.until(ExpectedConditions.elementToBeClickable(colorGrey));
            greyCheckbox.click();
        }

        // Комментарий
        driver.findElement(commentField).sendKeys(comment);

        // Кнопка "Заказать"
        WebElement orderBtn = wait.until(ExpectedConditions.elementToBeClickable(orderButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", orderBtn);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", orderBtn);
    }

    public void confirmOrder() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(confirmModal));
        jsClick(yesButton);
    }

    public boolean isOrderSuccess() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(successText)).isDisplayed();
    }
}