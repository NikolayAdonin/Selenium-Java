import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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

    @Test
    public void case1Test() throws InterruptedException {

        driver.get(site);
        logger.info("Открыта страница - " + site);

        logger.info("Title - " + driver.getTitle());
        logger.info("URL - " + driver.getCurrentUrl());
        logger.info(String.format("Ширина окна: %d", driver.manage().window().getSize().getWidth()));
        logger.info(String.format("Высота окна: %d", driver.manage().window().getSize().getHeight()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
        
        Thread.sleep(2000);

        WebElement btnApplyCity = driver.findElement(By
                .xpath("//span[@class='base-ui-button-v2__text' and text() = 'Всё верно']"));
        btnApplyCity.click();

        Thread.sleep(2000);

        WebElement linkRootCategory = driver.findElement(By
                .xpath("//a[@class='ui-link menu-desktop__root-title' and text() = 'Бытовая техника']"));
        linkRootCategory.click();

        Thread.sleep(2000);

        WebElement textTitle = driver.findElement(By
                .className("subcategory__page-title"));
        logger.info("Subcategory - Бытовая техника = " + textTitle.getText().equals("Бытовая техника"));
        Assertions.assertEquals("Бытовая техника", textTitle.getText(), "Текст 'Бытовая техника' не отображается");

        WebElement linkSubCategory = driver.findElement(By
                .xpath("//span[@class='subcategory__title' and text() = 'Техника для кухни']"));
        linkSubCategory.click();

        Thread.sleep(2000);

        WebElement textTitleSubcategory= driver.findElement(By
                .className("subcategory__page-title"));

        logger.info("Subcategory - Техника для кухни = " + textTitleSubcategory.getText().equals("Техника для кухни"));
        Assertions.assertEquals("Техника для кухни", textTitleSubcategory.getText(), "Текст 'Техника для кухни' не отображается");

        Thread.sleep(2000);

        WebElement linkCustomKitchen = driver.findElement(By
                .xpath("//a[@class='button-ui button-ui_white configurator-links-block__links-link' and text() = 'Собрать свою кухню']"));

        logger.info("Видимость ссылки 'Собрать свою кухню' = " + linkCustomKitchen.isDisplayed());
        Assertions.assertTrue(linkCustomKitchen.isDisplayed(), "Ссылка 'Собрать свою кухню' не отображается");

        List<WebElement> linksSubCategory = driver.findElements(By
                .xpath("//span[@class='subcategory__title']"));

        logger.info("Названия категорий = ");
        String tempString = "";
        for (WebElement tempElement : linksSubCategory) {
            logger.info(tempElement.getText());
        }
        Thread.sleep(2000);

        logger.info("Количество категорий = " + linksSubCategory.stream().count());
        Assertions.assertTrue(linksSubCategory.stream().count() > 5, "Количество категорий <= 5");
    }
}
