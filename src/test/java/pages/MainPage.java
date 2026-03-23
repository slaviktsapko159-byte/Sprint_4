package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MainPage {
    private WebDriver driver;

    private final By topOrderButton = By.xpath("//button[text()='Заказать']");
    private final By bottomOrderButton = By.xpath("//button[text()='Заказать']/ancestor::div[contains(@class, 'Home_FinishButton')]//button");
    private final By accordionButtons = By.cssSelector(".accordion__button");

    private String answerByIndex(int index) {
        return String.format(".//div[@id='accordion__panel-%d']/p", index);
    }

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    private void clickElement(By locator) {
        WebElement element = new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public void clickTopOrderButton() {
        clickElement(topOrderButton);
    }

    public void clickBottomOrderButton() {
        clickElement(bottomOrderButton);
    }

    public void clickQuestion(int index) {
        WebElement question = driver.findElements(accordionButtons).get(index);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", question);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", question);
        new WebDriverWait(driver, 5)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(answerByIndex(index))));
    }

    public String getAnswerText(int index) {
        return driver.findElement(By.xpath(answerByIndex(index))).getText();
    }
}