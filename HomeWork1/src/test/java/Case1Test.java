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

public class Case1Test {
    protected static WebDriver driver;
    private Logger logger = LogManager.getLogger(Case1Test.class);

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
            ImageIO.write(screenshot.getImage(), "png", new File("screenshots\\Case1\\" + eventName + ".png"));
            logger.info("Скриншот сохранен в файле " + eventName + ".png");
            actions
                    .scrollByAmount(-driver.manage().window().getSize().getHeight() * 10, -driver.manage().window().getSize().getWidth() * 10)
                    .perform();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void case1Test() {

        driver.get(site);
        logger.info("Открыта страница - " + site);

        logger.info("Title - " + driver.getTitle());
        logger.info("URL - " + driver.getCurrentUrl());
        logger.info(String.format("Ширина окна: %d", driver.manage().window().getSize().getWidth()));
        logger.info(String.format("Высота окна: %d", driver.manage().window().getSize().getHeight()));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));

        takeScreenshot(driver, "afterLoadPage1");

        By btnApplyCityXPath = By.xpath("//span[@class='base-ui-button-v2__text' and text() = 'Всё верно']");
        wait.until(ExpectedConditions.elementToBeClickable(btnApplyCityXPath));
        WebElement btnApplyCity = driver.findElement(btnApplyCityXPath);
        btnApplyCity.click();

        takeScreenshot(driver, "afterApplyCity");

        By linkRootCategoryXPath = By.xpath("//a[@class='ui-link menu-desktop__root-title' and text() = 'Бытовая техника']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(linkRootCategoryXPath));
        WebElement elementRootCategory = driver.findElement(linkRootCategoryXPath);
        elementRootCategory.click();

        takeScreenshot(driver, "afterClickLinkRootCategory");

        By textTitleClassName = By.className("subcategory__page-title");
        wait.until(ExpectedConditions.visibilityOfElementLocated(textTitleClassName));
        WebElement textTitle = driver.findElement(textTitleClassName);
        logger.info("Subcategory - Бытовая техника = " + textTitle.getText().equals("Бытовая техника"));
        Assertions.assertEquals("Бытовая техника", textTitle.getText(), "Текст 'Бытовая техника' не отображается");

        By linkSubCategoryXPath = By.xpath("//span[@class='subcategory__title' and text() = 'Техника для кухни']");
        wait.until(ExpectedConditions.elementToBeClickable(linkSubCategoryXPath));
        WebElement linkSubCategory = driver.findElement(linkSubCategoryXPath);
        linkSubCategory.click();

        takeScreenshot(driver, "afterClickLinkSubCategory");

        By textTitleSubcategoryClassName = By.className("subcategory__page-title");
        wait.until(ExpectedConditions.visibilityOfElementLocated(textTitleSubcategoryClassName));
        WebElement checkTitleSubcategory = driver.findElement(textTitleSubcategoryClassName);
        logger.info("Subcategory - Техника для кухни = " + checkTitleSubcategory.getText().equals("Техника для кухни"));
        Assertions.assertEquals("Техника для кухни", checkTitleSubcategory.getText(), "Текст 'Техника для кухни' не отображается");

        By linkCustomKitchenXPath = By.xpath("//a[@class='button-ui button-ui_white configurator-links-block__links-link' and text() = 'Собрать свою кухню']");
        wait.until(ExpectedConditions.elementToBeClickable(linkCustomKitchenXPath));
        WebElement linkCustomKitchen = driver.findElement(linkCustomKitchenXPath);
        logger.info("Видимость ссылки 'Собрать свою кухню' = " + linkCustomKitchen.isDisplayed());
        Assertions.assertTrue(linkCustomKitchen.isDisplayed(), "Ссылка 'Собрать свою кухню' не отображается");

        By linksSubCategoryXPath = By.xpath("//span[@class='subcategory__title']");
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(linksSubCategoryXPath));
        List<WebElement> linksSubCategory = driver.findElements(linksSubCategoryXPath);

        logger.info("Названия категорий = ");
        for (WebElement tempElement : linksSubCategory) {
            logger.info(tempElement.getText());
        }
        logger.info("Количество категорий = " + (long) linksSubCategory.size());
        Assertions.assertTrue((long) linksSubCategory.size() > 5, "Количество категорий > 5");
    }
}
