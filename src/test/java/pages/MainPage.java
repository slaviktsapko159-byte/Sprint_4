package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MainPage {
    private WebDriver driver;

    private final By topOrderButton = By.xpath("//button[text()='Заказать']");
    private final By bottomOrderButton = By.xpath("//button[text()='Заказать']/ancestor::div[contains(@class, 'Home_FinishButton')]//button");
<<<<<<< HEAD
=======
    private final By cookieBanner = By.className("App_CookieConsent__1yUIN");
>>>>>>> 65cad67c99c225c12f6962f4db4e393045808817
    private final By accordionButtons = By.cssSelector(".accordion__button");

    private String answerByIndex(int index) {
        return String.format(".//div[@id='accordion__panel-%d']/p", index);
    }

    public MainPage(WebDriver driver) {
        this.driver = driver;
<<<<<<< HEAD
    }

    private void clickElement(By locator) {
=======
        removeCookieBanner();
    }

    private void removeCookieBanner() {
        try {
            if (driver.findElements(cookieBanner).size() > 0) {
                ((JavascriptExecutor) driver).executeScript(
                        "var banner = document.querySelector('.App_CookieConsent__1yUIN'); if(banner) banner.remove();");
                new WebDriverWait(driver, 5).until(ExpectedConditions.invisibilityOfElementLocated(cookieBanner));
            }
        } catch (Exception e) {
            // ignore
        }
    }

    private void clickElement(By locator) {
        removeCookieBanner();
>>>>>>> 65cad67c99c225c12f6962f4db4e393045808817
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
<<<<<<< HEAD
=======
        removeCookieBanner();
>>>>>>> 65cad67c99c225c12f6962f4db4e393045808817
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