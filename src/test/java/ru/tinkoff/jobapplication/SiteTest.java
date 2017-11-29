package ru.tinkoff.jobapplication;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class SiteTest {
    private static ChromeDriver driver;

    @BeforeClass
    public static void openBrowser(){
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
    }

    @Before
    public void openSite(){
        driver.get("https://www.tinkoff.ru/");
    }

    @Test
    public void payment() throws InterruptedException {
        //первый тест с п.2
        openPaymentsMenu();
        openUtilities();
        openRegion("Москве", "г. Москва");

        List<WebElement> utilities = driver.findElements(By.xpath("//li[@data-qa-file='UIMenuItemProvider']//span[2]/a/span")); //формирование списка поставщиков услуг
        String wanted = utilities.get(0).getText(); // присвоение значения "ЖКУ-Москва" к "искомый"
        utilities.get(0).click();
        Thread.sleep(5000);
        String urlMsk = driver.getCurrentUrl(); // забираем текущий адрес

        WebElement billPaymentTab = driver.findElement(By.xpath("//*[text()='Оплатить ЖКУ в Москве']"));
        billPaymentTab.click();

        //проверка валидации обязательных полей
        sendKeysToField("\n", "//input[@name='provider-payerCode']");
        List<WebElement> validationErrors = driver.findElements(By.xpath("//*[text()= 'Поле обязательное']"));
        assertEquals(3, validationErrors.size());

        driver.navigate().refresh();
        sendKeysToField("1234", "//input[@name='provider-payerCode']");
        assertEquals(1, driver.findElements(By.xpath("//*[text()= 'Поле неправильно заполнено']")).size());

        driver.navigate().refresh();
        sendKeysToField("999999","//input[@name='provider-period']");
        assertEquals(1, driver.findElements(By.xpath("//*[text()= 'Поле заполнено некорректно']")).size());

        driver.navigate().refresh();
        sendKeysToField("15001","//*[@data-qa-file='FormFieldSet']//input");
        assertEquals(1, driver.findElements(By.xpath("//*[text()='Максимальная сумма перевода - 15 000\u00a0\u20BD']")).size());

        driver.navigate().refresh();
        sendKeysToField("2","//*[@data-qa-file='FormFieldSet']//input");
        assertEquals(1, driver.findElements(By.xpath("//*[text()='Минимальная сумма перевода - 10\u00a0\u20BD']")).size());

        //второй тест (п.8)
        openPaymentsMenu();
        sendKeysToFieldMass(wanted, "//input[contains(@class, 'Input__field_')]"); //вводим наименование искомого в строку поиска
        List<WebElement> resultSearch = driver.findElements(By.xpath("//div[contains(@class, 'SearchSuggest__entryText')]")); //формируем список результатов
        resultSearch.get(0).findElement(By.xpath("//*[text()='" + wanted + "']")); //проверка что 0-й элемент искомый
        resultSearch.get(0).click();
        new WebDriverWait(driver, 10).until(ExpectedConditions.urlToBe(urlMsk));

        //третий тест (п.12)
        openPaymentsMenu();
        openUtilities();
        openRegion("Санкт-Петербурге", "г. Санкт-Петербург");
        assertEquals(0, driver.findElements(By.xpath("//*[text()='" + wanted + "']")).size()); // нет искомого
    }

    //метод, осуществляющий надежное заполнение текстового поля
    private void sendKeysToField (String value, String xpath){
        WebElement field = driver.findElement(By.xpath(xpath));
        try {
            field.sendKeys("");
        } catch (StaleElementReferenceException e) {
        }
        field = driver.findElement(By.xpath(xpath));
        field.sendKeys(value);
    }

    //метод посимвольного заполнения текстового поля
    private void sendKeysToFieldMass (String value, String xpath) throws InterruptedException {
        for (char c : value.toCharArray()) {
            sendKeysToField(new String(new char[]{c}), xpath);
            Thread.sleep(500);
        }
    }

    //метод открытия меню "Платежи"
    private void openPaymentsMenu () throws InterruptedException {
        WebElement paymentsMenuItem = driver.findElement(By.xpath("//ul[@id='mainMenu']//*[text()='Платежи']"));
        paymentsMenuItem.click();
        Thread.sleep(1000);
    }

    // метод открытия п. "Коммунальные платежи"
    private void openUtilities (){
        WebElement utilityBillPaymentsItem = driver.findElement(By.xpath("//*[text()='Коммунальные платежи']"));
        utilityBillPaymentsItem.click();
    }


    //метод выбора региона, на вход принимает два параметра - отображаемый требуемый регион и выбираемый регион (в случае немовпадения текущего с требуемым)
    private void openRegion(String regionName, String regionName2) throws InterruptedException {
        WebElement region = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[1]/div[1]/div[1]/div[2]/div/div[1]/div/div/div/div[2]/div/div/div/span[2]"));
        String currentRegion = region.getText();
        if (!currentRegion.equals(regionName)) {
            Thread.sleep(1000);
            driver.findElement(By.xpath("//*[text()='" + currentRegion + "']")).click();
            WebElement requiredRegion = driver.findElement(By.xpath("//*[text()='" + regionName2 + "']"));
            requiredRegion.click();
        }
        Thread.sleep(1000);
    }

    @AfterClass
    public static void closeBrowser(){
        driver.quit();
    }
}
