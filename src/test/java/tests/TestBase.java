package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestBase {
    protected WebDriver driver;

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
        // Для отладки закомментируйте следующую строку, чтобы увидеть браузер
        options.addArguments("--headless");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("https://qa-scooter.praktikum-services.ru/");

        removeCookieBanner();
    }

    private void removeCookieBanner() {
        try {
            By cookieBanner = By.className("App_CookieConsent__1yUIN");
            if (driver.findElements(cookieBanner).size() > 0) {
                ((JavascriptExecutor) driver).executeScript(
                        "var banner = document.querySelector('.App_CookieConsent__1yUIN'); if(banner) banner.remove();");
                new WebDriverWait(driver, 5).until(ExpectedConditions.invisibilityOfElementLocated(cookieBanner));
            }
        } catch (Exception e) {
            // ignore
        }
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}