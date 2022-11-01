import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

public class Case2Test {
    protected static WebDriver driver;
    private Logger logger = LogManager.getLogger(Case2Test.class);
    String envBrowserName = System.getProperty("browser", "chrome");
    String envPageLoadStrategy = System.getProperty("loadstrategy", "normal");

    String site = "https://www.dns-shop.ru/";

    @BeforeEach
    public void setUp() {
        logger.info("env = " + envBrowserName + " + " + envPageLoadStrategy);
        driver = WebDriverFactory.getDriver(envBrowserName.toLowerCase());
        logger.info("Драйвер стартовал!");
    }

    @AfterEach
    public void setDown() {
        if (driver != null) {
            driver.quit();
            logger.info("Драйвер остановлен");
        }
    }

    @Test
    public void case2Test() throws InterruptedException {
        driver.get(site);
        driver.manage().window().maximize();

        Thread.sleep(2000);

        WebElement element = driver.findElement(By
                .xpath("//a[@class='ui-link menu-desktop__root-title' and normalize-space() = 'Бытовая техника']"));

        Actions actions = new Actions(driver);
        actions
                .moveToElement(element)
                .perform();
        Thread.sleep(2000);
        List<WebElement> elements = driver.findElements(By
                .xpath("//a[@class='ui-link menu-desktop__first-level']"));
        logger.info("Подкатегории Бытовая техника: ");

        for (WebElement tempElement : elements)
            logger.info(tempElement.getText());

        List<WebElement> elementsSecondLevel = driver.findElements(By
                .xpath("//a[@class='ui-link menu-desktop__second-level']"));

        actions
                .moveToElement(elementsSecondLevel.get(0))
                .perform();

        Thread.sleep(3000);

        List<WebElement> popUpElementsSecondLevel = driver.findElements(By.cssSelector(".menu-desktop__popup-link"));
                //.xpath("//a[@class='ui-link menu-desktop__popup-link']"));

        logger.info("Количество ссылок в подменю = " + popUpElementsSecondLevel.stream().count());

        actions
                .moveToElement(popUpElementsSecondLevel.get(0))
                .click()
                .perform();

        WebElement elementElectricFurnace = driver.findElement(By
                .xpath("//span[@class='subcategory__title' and normalize-space() = 'Плиты электрические']"));
        elementElectricFurnace.click();

        WebElement elementItemCount = driver.findElement(By.xpath("//span[@data-role='items-count']"));
        logger.info("Количество товаров = "+elementItemCount.getText().replaceAll("\\D",""));

        Thread.sleep(3000);
    }
}
