package GraphService;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

public class TradingViewScreenshotService {

    public InputFile captureChart(String baseCurrency) {
        String symbol = baseCurrency.toUpperCase() + "RUB";
        String url = "https://ru.tradingview.com/chart/?symbol=FX_IDC%3A" + symbol;

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu", "--window-size=1280,800");
        WebDriver driver = new ChromeDriver(options);

        try {
            driver.get(url);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("canvas[data-name='pane-top-canvas']")));

            WebElement areaButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@id='header-toolbar-intervals']")));
            areaButton.click();

            WebElement graphTypeButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(@class, 'label') and text()='1 час']")));
            graphTypeButton.click();

            WebElement fullScreen = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@data-name='header-toolbar-fullscreen']")));
            fullScreen.click();

            WebElement canvas = driver.findElement(By.cssSelector("canvas[data-name='pane-top-canvas']"));

            wait.until(driver1 -> canvas.getSize().getWidth() > 1000);

            //div[contains(text(), 'Неправильный инструмент')]

            List<WebElement> errorElements = driver.findElements(By.xpath("//div[contains(text(), 'Неправильный инструмент')]"));
            if (!errorElements.isEmpty()) {
                System.out.println("❌ Неправильный инструмент — прерываем выполнение без скриншота.");
                return null;
            }


            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path tempFile = Files.createTempFile("chart-" + UUID.randomUUID(), ".png");
            Files.copy(screenshot.toPath(), tempFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            return new InputFile(tempFile.toFile());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            driver.quit();
        }
    }
}
