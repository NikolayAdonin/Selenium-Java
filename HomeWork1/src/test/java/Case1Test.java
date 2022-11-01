import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
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
    public void case1Test() throws InterruptedException {

        driver.get(site);
        driver.manage().window().maximize();
        logger.info("Открыта страница - " + site);

        logger.info("Title - " + driver.getTitle());
        logger.info("URL - " + driver.getCurrentUrl());
        logger.info(String.format("Ширина окна: %d", driver.manage().window().getSize().getWidth()));
        logger.info(String.format("Высота окна: %d", driver.manage().window().getSize().getHeight()));

        Thread.sleep(2000);

        WebElement element = driver.findElement(By
                .xpath("//span[@class='menu-mobile__root-title' and normalize-space() = 'Бытовая техника']"));
        if (!element.isDisplayed())
            element = driver.findElement(By
                    .xpath("//a[@class='ui-link menu-desktop__root-title' and normalize-space() = 'Бытовая техника']"));

        element.click();

        WebElement checkTitle = driver.findElement(By
                .className("subcategory__page-title"));
        logger.info("Subcategory - Бытовая техника = " + checkTitle.getText().equals("Бытовая техника"));

        WebElement elementKitchen = driver.findElement(By
                .xpath("//span[@class='subcategory__title' and normalize-space() = 'Техника для кухни']"));
        elementKitchen.click();

        WebElement checkTitleKitchen = driver.findElement(By
                .className("subcategory__page-title"));

        logger.info("Subcategory - Техника для кухни = " + checkTitleKitchen.getText().equals("Техника для кухни"));

        Thread.sleep(2000);

        WebElement checkRefCustomKitchen = driver.findElement(By
                .xpath("//a[@class='button-ui button-ui_white configurator-links-block__links-link' and normalize-space() = 'Собрать свою кухню']"));

        logger.info("Видимость ссылки 'Собрать свою кухню' = " + checkRefCustomKitchen.isDisplayed());

        List<WebElement> subCategorys = driver.findElements(By
                .xpath("//span[@class='subcategory__title']"));

        logger.info("Названия категорий = ");
        String tempString = "";
        for (WebElement tempElement : subCategorys) {
            logger.info(tempElement.getText());
        }
        Thread.sleep(2000);

        logger.info("Количество категорий = " + subCategorys.stream().count());
        logger.info("Количество категорий > 5 = " + (subCategorys.stream().count() > 5));

        //String sdad = "192 товара";
        //logger.info(Integer.parseInt(sdad.replaceAll("\\D","")));

    }

}
