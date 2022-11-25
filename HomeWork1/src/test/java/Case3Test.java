import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.locators.RelativeLocator;
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

public class Case3Test {
    protected static WebDriver driver;
    private Logger logger = LogManager.getLogger(Case3Test.class);
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
            ImageIO.write(screenshot.getImage(), "png", new File("screenshots\\Case3\\" + eventName + ".png"));
            logger.info("Скриншот сохранен в файле " + eventName + ".png");
            actions
                    .scrollByAmount(-driver.manage().window().getSize().getHeight() * 10, -driver.manage().window().getSize().getWidth() * 10)
                    .perform();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void case3Test() throws InterruptedException {
        driver.get(site);

        Selenium4Listener listener = new Selenium4Listener();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
        Actions actions = new Actions(driver);
        wait.ignoring(StaleElementReferenceException.class);

        WebDriver eventFiringDriver = new EventFiringDecorator<>(listener).decorate(Case3Test.driver);

        By btnApplyCityXPath = By.xpath("//span[@class='base-ui-button-v2__text' and text() = 'Всё верно']");
        wait.until(ExpectedConditions.elementToBeClickable(btnApplyCityXPath));
        WebElement btnApplyCity = Case3Test.driver.findElement(btnApplyCityXPath);
        takeScreenshot(driver, "afterLoadPage1");
        btnApplyCity.click();

        By linkRootCategoryXPath = By.xpath("//a[@class='ui-link menu-desktop__root-title' and text() = 'ПК, ноутбуки, периферия']");
        wait.until(ExpectedConditions.elementToBeClickable(linkRootCategoryXPath));
        WebElement linkRootCategory = driver.findElement(linkRootCategoryXPath);
        actions
                .moveToElement(linkRootCategory)
                .perform();
        takeScreenshot(driver, "afterOpenMenu");

        //после скриншота теряется элемент (StaleElementException)
        wait.until(ExpectedConditions.elementToBeClickable(linkRootCategoryXPath));
        linkRootCategory = driver.findElement(linkRootCategoryXPath);
        actions
                .moveToElement(linkRootCategory)
                .perform();
        By popUpSubMenuXPath = By.xpath("//div[@class='menu-desktop__submenu menu-desktop__submenu_top']");
        wait.until(ExpectedConditions.attributeToBe(popUpSubMenuXPath, "style", "display: block;"));
        actions
                .moveToElement(linkRootCategory)
                .perform();
        By linkNotebooksXPath = By.xpath("//a[@class='ui-link menu-desktop__second-level' and text() = 'Ноутбуки']");
        wait.until(ExpectedConditions.elementToBeClickable(linkNotebooksXPath));
        WebElement linkNotebooks = driver.findElement(linkNotebooksXPath);
        actions
                .moveToElement(linkRootCategory)
                .perform();
        wait.until(ExpectedConditions.elementToBeClickable(linkNotebooks));
        linkNotebooks.click();
        takeScreenshot(driver, "afterLoadPage2");

        By blockHeaderXPath = By.xpath("//header");
        wait.until(ExpectedConditions.visibilityOfElementLocated(blockHeaderXPath));
        WebElement blockHeader = driver.findElement(blockHeaderXPath);
        ((JavascriptExecutor) driver).executeScript("arguments[0].style.visibility='hidden'", blockHeader);
        takeScreenshot(driver, "afterHideHeader");

        By textManufactureXPath = By.xpath("//div[@data-id='brand']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(textManufactureXPath));
        WebElement textManufacture = driver.findElement(textManufactureXPath);
        actions
                .moveToElement(textManufacture)
                .perform();

        By inputSearchManufactureXPath = By.xpath("//input[@placeholder='Поиск']");
        wait.until(ExpectedConditions.elementToBeClickable(inputSearchManufactureXPath));
        WebElement inputSearchManufacture = driver.findElement(inputSearchManufactureXPath);
        inputSearchManufacture.sendKeys("asus");

        By checkBoxAsusXPath = By.xpath("//label[@class='ui-checkbox ui-checkbox_list']//span[text() = 'ASUS  ']");
        wait.until(ExpectedConditions.elementToBeClickable(checkBoxAsusXPath));
        WebElement checkBoxAsus = driver.findElement(checkBoxAsusXPath);
        actions
                .moveToElement(checkBoxAsus)
                .click()
                .perform();

        By dropDownRamValueXPath = By.xpath("//span[@class='ui-collapse__link-text' and text() = 'Объем оперативной памяти (ГБ)']");
        wait.until(ExpectedConditions.elementToBeClickable(dropDownRamValueXPath));
        WebElement dropDownRamValue = driver.findElement(dropDownRamValueXPath);
        actions
                .moveToElement(dropDownRamValue)
                .click()
                .perform();

        By checkBoxRamValueXPath = By.xpath("//label[@class='ui-checkbox ui-checkbox_list']//span[text() = '32 ГБ  ']");
        wait.until(ExpectedConditions.elementToBeClickable(checkBoxRamValueXPath));
        WebElement checkBoxRamValue = driver.findElement(checkBoxRamValueXPath);
        actions
                .moveToElement(checkBoxRamValue)
                .click()
                .perform();

        By btnApplyFilterXPath = By.xpath("//button[@data-role='filters-submit']");
        wait.until(ExpectedConditions.elementToBeClickable(btnApplyFilterXPath));
        WebElement btnApplyFilter = driver.findElement(btnApplyFilterXPath);
        actions
                .moveToElement(btnApplyFilter)
                .perform();
        //если клик сунуть в actions - фильтры сбрасываются
        btnApplyFilter.click();
        takeScreenshot(driver, "afterApplyFilters");

        By dropDownSortXPath = By.xpath("//span[@class='top-filter__selected' and text() = 'Сначала недорогие']");
        wait.until(ExpectedConditions.elementToBeClickable(dropDownSortXPath));
        WebElement dropDownSort = driver.findElement(dropDownSortXPath);
        actions
                .moveToElement(dropDownSort)
                .perform();
        dropDownSort.click();

        By radioBtnSortXPath = By.xpath("//span[@class='ui-radio__content' and text() = 'Сначала дорогие']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(radioBtnSortXPath));
        WebElement radioBtnSort = driver.findElement(radioBtnSortXPath);
        actions
                .moveToElement(radioBtnSort)
                .perform();
        radioBtnSort.click();
        takeScreenshot(driver, "afterApplySort");

        By listProductsXPath = By.xpath("//a[@class='catalog-product__name ui-link ui-link_black']");
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(listProductsXPath, 2));
        List<WebElement> listProducts = driver.findElements(listProductsXPath);

        String linkFirstNotebook = listProducts.get(0).getAttribute("href");
        String textNameFirstNotebook = listProducts.get(0).getText();
        //на всякий случай
        String oldWindow = driver.getWindowHandle();

        driver.switchTo().newWindow(WindowType.WINDOW);
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));

        driver.manage().window().maximize();
        driver.get(linkFirstNotebook);
        takeScreenshot(driver, "afterLoadPage3");

        By textTitleNotebookNewWindowXPath = By.xpath("//h1[@class='product-card-top__title']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(textTitleNotebookNewWindowXPath));
        WebElement textTitleNotebookNewWindow = driver.findElement(textTitleNotebookNewWindowXPath);
        Assertions.assertTrue(textNameFirstNotebook.contains(textTitleNotebookNewWindow.getText()), "Название ноутбука не соответсвует названию на прошлой странице");
        Assertions.assertTrue(driver.getTitle().contains(textTitleNotebookNewWindow.getText()), "Название ноутбука не соответсвует названию в заголовке страницы");

        By btnCharacteristicsXPath = By.xpath("//a[@class='product-card-tabs__title ui-link ui-link_black' and text() = 'Характеристики']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(btnCharacteristicsXPath));
        WebElement btnCharacteristics = driver.findElement(btnCharacteristicsXPath);
        actions
                .moveToElement(btnCharacteristics)
                .perform();
        btnCharacteristics.click();

        By btnDropDownCharacteristicsXPath = By.xpath("//button[@class='button-ui button-ui_white product-characteristics__expand' and text() = 'Развернуть все']");
        wait.until(ExpectedConditions.elementToBeClickable(btnDropDownCharacteristicsXPath));
        WebElement btnDropDownCharacteristics = driver.findElement(btnDropDownCharacteristicsXPath);
        btnDropDownCharacteristics.click();

        By textTitleNotebookXPath = By.xpath("//div[@class='product-card-description__title']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(textTitleNotebookXPath));
        WebElement textTitleNotebook = driver.findElement(textTitleNotebookXPath);
        Assertions.assertTrue(textTitleNotebook.getText().contains("ASUS"), "Заголовок не содержит 'ASUS'");

        By textCharacteristicNameXPath = By.xpath("//div[@class='product-characteristics__spec-title' and text() = ' Объем оперативной памяти ']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(textCharacteristicNameXPath));
        WebElement textCharacteristicName = driver.findElement(textCharacteristicNameXPath);
        actions
                .moveToElement(textCharacteristicName)
                .perform();
        By textNotebookRamValueXPath = RelativeLocator.with(By.className("product-characteristics__spec-value")).toRightOf(textCharacteristicNameXPath);
        WebElement textNotebookRamValue = driver.findElement(textNotebookRamValueXPath);
        Assertions.assertEquals(textNotebookRamValue.getText().replaceAll("\\s+", ""), "32ГБ", "Количество оперативной памяти != 32 Гб");
    }
}
