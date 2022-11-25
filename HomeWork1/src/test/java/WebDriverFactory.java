import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class WebDriverFactory {
    private static Logger logger = LogManager.getLogger(WebDriverFactory.class);

    public static WebDriver getDriver(String browserName, String pageLoadStrategy) {
        switch (browserName) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--incognito");
                chromeOptions.setPageLoadStrategy(PageLoadStrategy.valueOf(pageLoadStrategy.toUpperCase()));
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver(chromeOptions);
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--kiosk");
                firefoxOptions.setPageLoadStrategy(PageLoadStrategy.valueOf(pageLoadStrategy.toUpperCase()));
                WebDriverManager.firefoxdriver().setup();
                return new FirefoxDriver(firefoxOptions);
            default:
                throw new RuntimeException("Введено некорректное название браузера");
        }
    }
}
