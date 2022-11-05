import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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

    @Test
    public void case1Test() throws InterruptedException {

        driver.get(site);
        logger.info("Открыта страница - " + site);

        logger.info("Title - " + driver.getTitle());
        logger.info("URL - " + driver.getCurrentUrl());
        logger.info(String.format("Ширина окна: %d", driver.manage().window().getSize().getWidth()));
        logger.info(String.format("Высота окна: %d", driver.manage().window().getSize().getHeight()));

        Thread.sleep(2000);

        WebElement elementApplyCity = driver.findElement(By
                .xpath("//span[@class='base-ui-button-v2__text' and text() = 'Всё верно']"));
        elementApplyCity.click();

        Thread.sleep(2000);

        WebElement elementRootCategory = driver.findElement(By
                .xpath("//a[@class='ui-link menu-desktop__root-title' and text() = 'Бытовая техника']"));
        elementRootCategory.click();

        WebElement checkTitle = driver.findElement(By
                .className("subcategory__page-title"));
        logger.info("Subcategory - Бытовая техника = " + checkTitle.getText().equals("Бытовая техника"));
        Assertions.assertTrue(checkTitle.getText().equals("Бытовая техника"), "Текст 'Бытовая техника' не отображается");

        WebElement elementSubCategory = driver.findElement(By
                .xpath("//span[@class='subcategory__title' and text() = 'Техника для кухни']"));
        elementSubCategory.click();

        WebElement checkTitleSubcategory= driver.findElement(By
                .className("subcategory__page-title"));

        logger.info("Subcategory - Техника для кухни = " + checkTitleSubcategory.getText().equals("Техника для кухни"));
        Assertions.assertTrue(checkTitleSubcategory.getText().equals("Техника для кухни"), "Текст 'Техника для кухни' не отображается");

        Thread.sleep(2000);

        WebElement checkRefCustomKitchen = driver.findElement(By
                .xpath("//a[@class='button-ui button-ui_white configurator-links-block__links-link' and text() = 'Собрать свою кухню']"));

        logger.info("Видимость ссылки 'Собрать свою кухню' = " + checkRefCustomKitchen.isDisplayed());
        Assertions.assertTrue(checkRefCustomKitchen.isDisplayed(), "Ссылка 'Собрать свою кухню' не отображается");

        List<WebElement> elementsSubCategory = driver.findElements(By
                .xpath("//span[@class='subcategory__title']"));

        logger.info("Названия категорий = ");
        String tempString = "";
        for (WebElement tempElement : elementsSubCategory) {
            logger.info(tempElement.getText());
        }
        Thread.sleep(2000);

        logger.info("Количество категорий = " + elementsSubCategory.stream().count());
        Assertions.assertTrue(elementsSubCategory.stream().count() > 5, "Количество категорий > 5");
    }
}
