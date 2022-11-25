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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
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

    public void takeScreenshot(WebDriver driver, String eventName) {
        try {
            Actions actions = new Actions(driver);
            Screenshot screenshot = new AShot()
                    .shootingStrategy(ShootingStrategies.viewportPasting(100))
                    .takeScreenshot(driver);
            ImageIO.write(screenshot.getImage(), "png", new File("screenshots\\Case2\\" + eventName + ".png"));
            logger.info("Скриншот сохранен в файле " + eventName + ".png");
            actions
                    .scrollByAmount(-driver.manage().window().getSize().getHeight() * 10, -driver.manage().window().getSize().getWidth() * 10)
                    .perform();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void case2Test() {
        driver.get(site);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
        Actions actions = new Actions(driver);

        takeScreenshot(driver, "afterLoadPage1");

        By btnApplyCityXPath = By.xpath("//span[@class='base-ui-button-v2__text' and text() = 'Всё верно']");
        wait.until(ExpectedConditions.elementToBeClickable(btnApplyCityXPath));
        WebElement btnApplyCity = driver.findElement(btnApplyCityXPath);
        btnApplyCity.click();

        takeScreenshot(driver, "afterApplyCity");

        By linkRootCategoryXPath = By.xpath("//a[@class='ui-link menu-desktop__root-title' and text() = 'Бытовая техника']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(linkRootCategoryXPath));
        WebElement linkRootCategory = driver.findElement(linkRootCategoryXPath);
        actions
                .moveToElement(linkRootCategory)
                .perform();

        takeScreenshot(driver, "afterMoveToLinkRootCategory");

        By linksFirstLevelXPath = By.xpath("//a[@class='ui-link menu-desktop__first-level']");
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(linksFirstLevelXPath));
        List<WebElement> elementsFirstLevel = driver.findElements(linksFirstLevelXPath);
        logger.info("Подкатегории Бытовая техника: ");
        for (WebElement tempElement : elementsFirstLevel)
            logger.info(tempElement.getText());

        By linkMakeDrinksXPath = By.xpath("//a[@class='ui-link menu-desktop__second-level' and text() = 'Приготовление напитков']");
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(linkMakeDrinksXPath));
        WebElement linkMakeDrinks = driver.findElement(linkMakeDrinksXPath);
        actions
                .moveToElement(linkMakeDrinks)
                .perform();

        takeScreenshot(driver, "afterMoveToLinkDrinks");

        actions
                .moveToElement(linkRootCategory)
                .perform();

        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(linkMakeDrinksXPath));
        actions
                .moveToElement(linkMakeDrinks)
                .perform();

        By popUpElementsSecondLevelCssSelector = By.cssSelector(".menu-desktop__popup-link");
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(popUpElementsSecondLevelCssSelector));
        List<WebElement> popUpElementsSecondLevel = driver.findElements(popUpElementsSecondLevelCssSelector);
        logger.info("Количество ссылок в подменю = " + (long) popUpElementsSecondLevel.size());

        By linkFurnaceXPath = By.xpath("//a[@class='ui-link menu-desktop__second-level' and text() = 'Плиты и печи']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(linkFurnaceXPath));
        WebElement linkFurnace = driver.findElement(linkFurnaceXPath);
        actions
                .moveToElement(linkFurnace)
                .perform();

        takeScreenshot(driver, "afterMoveToLinkFurnace");

        actions
                .moveToElement(linkRootCategory)
                .perform();
        wait.until(ExpectedConditions.visibilityOfElementLocated(linkFurnaceXPath));
        actions
                .moveToElement(linkFurnace)
                .perform();

        By linkElectricFurnaceXPath = By.xpath("//a[@class='ui-link menu-desktop__popup-link' and text() = 'Плиты электрические']");
        wait.until(ExpectedConditions.elementToBeClickable(linkElectricFurnaceXPath));
        WebElement linkElectricCookers = driver.findElement(linkElectricFurnaceXPath);
        linkElectricCookers.click();

        takeScreenshot(driver, "afterClickLinkElectricCookers");

        By textItemCountXPath = By.xpath("//span[@data-role='items-count']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(textItemCountXPath));
        WebElement textItemCount = driver.findElement(textItemCountXPath);
        String itemCountInt = textItemCount.getText().replaceAll("\\D", "");
        logger.info("Количество товаров = " + itemCountInt);
        Assertions.assertTrue(Integer.parseInt(itemCountInt) > 100, "Количество товаров > 100");
    }
}
