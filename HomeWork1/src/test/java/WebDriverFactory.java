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
                switch (pageLoadStrategy) {
                    case "normal":
                        chromeOptions.setCapability("pageLoadStrategy", PageLoadStrategy.NORMAL);
                        break;
                    case "eager":
                        chromeOptions.setCapability("pageLoadStrategy", PageLoadStrategy.EAGER);
                        break;
                    case "none":
                        chromeOptions.setCapability("pageLoadStrategy", PageLoadStrategy.NONE);
                        break;
                    default:
                        chromeOptions.setCapability("pageLoadStrategy", PageLoadStrategy.NORMAL);
                        break;
                }
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver(chromeOptions);
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--kiosk");
                firefoxOptions.addArguments("--private");
                switch (pageLoadStrategy) {
                    case "normal":
                        firefoxOptions.setCapability("pageLoadStrategy", PageLoadStrategy.NORMAL);
                        break;
                    case "eager":
                        firefoxOptions.setCapability("pageLoadStrategy", PageLoadStrategy.EAGER);
                        break;
                    case "none":
                        firefoxOptions.setCapability("pageLoadStrategy", PageLoadStrategy.NONE);
                        break;
                    default:
                        firefoxOptions.setCapability("pageLoadStrategy", PageLoadStrategy.NORMAL);
                        break;
                }
                WebDriverManager.firefoxdriver().setup();
                return new FirefoxDriver(firefoxOptions);
            default:
                throw new RuntimeException("Введено некорректное название браузера");
        }
    }
}
