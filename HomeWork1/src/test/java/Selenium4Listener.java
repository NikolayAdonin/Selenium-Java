import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.events.WebDriverListener;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Selenium4Listener implements WebDriverListener {
    private Logger logger = LogManager.getLogger(Selenium4Listener.class);

    public void takeScreenshot(WebDriver driver, String eventName, WebElement element) {
        try {
            Actions actions = new Actions(driver);
            Screenshot screenshot = new AShot()
                    .shootingStrategy(ShootingStrategies.viewportPasting(100))
                    .takeScreenshot(driver);
            ImageIO.write(screenshot.getImage(), "png", new File("screenshots\\ListenerScreenshots\\" + eventName + ".png"));
            logger.info("Скриншот сохранен в файле " + eventName + ".png");
            actions
                    .scrollToElement(element)
                    .perform();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String elementDescription(WebElement element) {
        String description = "tag:" + element.getTagName();
        if (element.getAttribute("id") != null) {
            description += " id: " + element.getAttribute("id");
        } else if (element.getAttribute("name") != null) {
            description += " name: " + element.getAttribute("name");
        }
        description += " ('" + element.getText() + "')";
        return description;
    }

    @Override
    public void afterClick(WebElement element) {
        logger.info("Произведено нажатие: " + elementDescription(element));
    }

    @Override
    public void afterFindElement(WebDriver driver, By locator, WebElement result) {
        logger.info("Найдет элемент: " + elementDescription(result));
        takeScreenshot(driver, "afterFindElement_" + result.getTagName() + "_" + result.getText(), result);
    }

    @Override
    public void afterFindElements(WebDriver driver, By locator, List<WebElement> result) {
        logger.info("Найдены элементы:");
        for (WebElement tempElement : result)
            logger.info(elementDescription(tempElement));
        takeScreenshot(driver, "afterFindElements" + result.get(0).getTagName(), result.get(0));
    }

    @Override
    public void afterGetText(WebElement element, String result) {
        logger.info("Получен текст: " + result);
    }
}
