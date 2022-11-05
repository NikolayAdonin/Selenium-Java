import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
        driver = WebDriverFactory.getDriver(envBrowserName.toLowerCase(), envPageLoadStrategy.toLowerCase());
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
        //driver.manage().window().maximize();

        Thread.sleep(2000);

        WebElement elementCityApply = driver.findElement(By
                .xpath("//span[@class='base-ui-button-v2__text' and text() = 'Всё верно']"));
        elementCityApply.click();

        Thread.sleep(2000);

        WebElement elementRootCategory = driver.findElement(By
                .xpath("//a[@class='ui-link menu-desktop__root-title' and text() = 'Бытовая техника']"));

        Actions actions = new Actions(driver);
        actions
                .moveToElement(elementRootCategory)
                .perform();
        Thread.sleep(2000);
        List<WebElement> elementsFirstLevel = driver.findElements(By
                .xpath("//a[@class='ui-link menu-desktop__first-level']"));
        logger.info("Подкатегории Бытовая техника: ");

        for (WebElement tempElement : elementsFirstLevel)
            logger.info(tempElement.getText());

        List<WebElement> elementsSecondLevel = driver.findElements(By
                .xpath("//a[@class='ui-link menu-desktop__second-level']"));

        actions
                .moveToElement(elementsSecondLevel.get(0))
                .perform();

        Thread.sleep(2000);

        List<WebElement> popUpElementsSecondLevel = driver.findElements(By.cssSelector(".menu-desktop__popup-link"));

        logger.info("Количество ссылок в подменю = " + popUpElementsSecondLevel.stream().count());

        actions
                .moveToElement(popUpElementsSecondLevel.get(0))
                .click()
                .perform();

        Thread.sleep(2000);

        WebElement elementElectricCookers = driver.findElement(By
                .xpath("//span[@class='subcategory__title' and text() = 'Плиты электрические']"));
        elementElectricCookers.click();

        Thread.sleep(3000);

        WebElement elementItemCount = driver.findElement(By.xpath("//span[@data-role='items-count']"));

        Thread.sleep(2000);

        String itemCountInt = elementItemCount.getText().replaceAll("\\D", "");
        logger.info("Количество товаров = " + itemCountInt);
        Assertions.assertTrue(Integer.parseInt(itemCountInt) > 100, "Количество товаров > 100");
    }
}
