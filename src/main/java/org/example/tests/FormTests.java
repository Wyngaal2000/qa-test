package org.example.tests;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.time.Duration;

public class FormTests {
    private WebDriver driver;

    @Parameters("browser")
    @BeforeMethod
    public void setUp(@Optional("chrome") String browser) {
        if (browser.equalsIgnoreCase("chrome")) {
            driver = new ChromeDriver();
            driver.get("https://www.google.com");
        } else if (browser.equalsIgnoreCase("edge")) {
            driver = new EdgeDriver();
            driver.get("https://ya.ru");
        }

        String filePath = new File("src/main/resources/qa-test.html").getAbsolutePath();
        driver.get("file:///" + filePath);

        WebElement emailInput = driver.findElement(By.id("loginEmail"));
        WebElement passwordInput = driver.findElement(By.id("loginPassword"));
        WebElement authButton = driver.findElement(By.id("authButton"));

        emailInput.sendKeys("test@protei.ru");
        passwordInput.sendKeys("test");
        authButton.click();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(description = "Позитвка заполенение формы без выборов")
    public void testSubmitFormWithValidData() {
        String email = RandomStringUtils.randomAlphanumeric(8) + "@domain.com";
        String name = RandomStringUtils.randomAlphanumeric(8);
        String gender = "Мужской";

        WebElement emailInput = driver.findElement(By.id("dataEmail"));
        WebElement nameInput = driver.findElement(By.id("dataName"));
        WebElement genderSelect = driver.findElement(By.id("dataGender"));
        WebElement sendButton = driver.findElement(By.id("dataSend"));

        emailInput.sendKeys(email);
        nameInput.sendKeys(name);
        genderSelect.sendKeys(gender);
        sendButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("uk-modal-dialog")));
        Assert.assertTrue(successMessage.isDisplayed(), "Сообщение о добавлении данных не отобразилось.");

        WebElement okButton = driver.findElement(By.cssSelector(".uk-button.uk-button-primary.uk-modal-close"));
        okButton.click();

        WebElement table = driver.findElement(By.id("dataTable"));
        WebElement lastRow = table.findElement(By.xpath(".//tbody/tr[last()]"));

        String emailColumn = lastRow.findElement(By.xpath(".//td[1]")).getText();
        String nameColumn = lastRow.findElement(By.xpath(".//td[2]")).getText();
        String genderColumn = lastRow.findElement(By.xpath(".//td[3]")).getText();
        String firstChoiceColumn = lastRow.findElement(By.xpath(".//td[4]")).getText();
        String secondChoiceColumn = lastRow.findElement(By.xpath(".//td[5]")).getText();

        Assert.assertEquals(emailColumn, email, "Email в таблице не соответствует ожидаемому.");
        Assert.assertEquals(nameColumn, name, "Имя в таблице не соответствует ожидаемому.");
        Assert.assertEquals(genderColumn, gender, "Пол в таблице не соответствует ожидаемому.");
        Assert.assertEquals(firstChoiceColumn, "Нет", "Выбор1 в таблице не соответствует ожидаемому.");
        Assert.assertEquals(secondChoiceColumn, "", "Выбор2 в таблице не соответствует ожидаемому.");
    }

    @Test(description = "Позитвка заполенение формы cо всеми выборами")
    public void testSubmitFormWithValidDataAndChoices() {
        String email = RandomStringUtils.randomAlphanumeric(8) + "@domain.com";
        String name = RandomStringUtils.randomAlphanumeric(8);
        String gender = "Мужской";

        WebElement emailInput = driver.findElement(By.id("dataEmail"));
        WebElement nameInput = driver.findElement(By.id("dataName"));
        WebElement genderSelect = driver.findElement(By.id("dataGender"));
        WebElement sendButton = driver.findElement(By.id("dataSend"));
        WebElement checkbox1 = driver.findElement(By.id("dataCheck11"));
        WebElement checkbox2 = driver.findElement(By.id("dataCheck12"));
        WebElement radioButton = driver.findElement(By.id("dataSelect22"));

        emailInput.sendKeys(email);
        nameInput.sendKeys(name);
        genderSelect.sendKeys(gender);
        checkbox1.click();
        checkbox2.click();
        radioButton.click();
        sendButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("uk-modal-dialog")));
        Assert.assertTrue(successMessage.isDisplayed(), "Сообщение о добавлении данных не отобразилось.");

        WebElement okButton = driver.findElement(By.cssSelector(".uk-button.uk-button-primary.uk-modal-close"));
        okButton.click();

        WebElement table = driver.findElement(By.id("dataTable"));
        WebElement lastRow = table.findElement(By.xpath(".//tbody/tr[last()]"));

        String emailColumn = lastRow.findElement(By.xpath(".//td[1]")).getText();
        String nameColumn = lastRow.findElement(By.xpath(".//td[2]")).getText();
        String genderColumn = lastRow.findElement(By.xpath(".//td[3]")).getText();
        String firstChoiceColumn = lastRow.findElement(By.xpath(".//td[4]")).getText();
        String secondChoiceColumn = lastRow.findElement(By.xpath(".//td[5]")).getText();

        Assert.assertEquals(emailColumn, email, "Email в таблице не соответствует ожидаемому.");
        Assert.assertEquals(nameColumn, name, "Имя в таблице не соответствует ожидаемому.");
        Assert.assertEquals(genderColumn, gender, "Пол в таблице не соответствует ожидаемому.");
        Assert.assertEquals(firstChoiceColumn, "1.1, 1.2", "Выбор1 в таблице не соответствует ожидаемому.");
        Assert.assertEquals(secondChoiceColumn, "2.2", "Выбор2 в таблице не соответствует ожидаемому.");
    }

    @Test(description = "Невалидная почта")
    public void testSubmitFormWithInvalidEmail() {
        String email = RandomStringUtils.randomAlphanumeric(8);
        String name = RandomStringUtils.randomAlphanumeric(8);
        String gender = "Мужской";

        WebElement emailInput = driver.findElement(By.id("dataEmail"));
        WebElement nameInput = driver.findElement(By.id("dataName"));
        WebElement genderSelect = driver.findElement(By.id("dataGender"));
        WebElement sendButton = driver.findElement(By.id("dataSend"));

        emailInput.sendKeys(email);
        nameInput.sendKeys(name);
        genderSelect.sendKeys(gender);
        sendButton.click();

        WebElement error = driver.findElement(By.id("emailFormatError"));
        Assert.assertNotNull(error, "Ошибка неверного формата E-Mail не появилась.");
        Assert.assertEquals(error.getText(),
                "Неверный формат E-Mail",
                "текст ошибки не совпадает с ожидаемым");

    }

    //TODO кейс с неверным форматом почты, ошибки не возникает, аналогично в авторизации
    //кейс оствален зеленым
    @Test(description = "Невалидный формат почты, без .*")
    public void testSubmitFormInvalidEmailFormatBag() {
        String email = RandomStringUtils.randomAlphanumeric(8) + "@domain";
        String name = RandomStringUtils.randomAlphanumeric(8);
        String gender = "Мужской";

        WebElement emailInput = driver.findElement(By.id("dataEmail"));
        WebElement nameInput = driver.findElement(By.id("dataName"));
        WebElement genderSelect = driver.findElement(By.id("dataGender"));
        WebElement sendButton = driver.findElement(By.id("dataSend"));

        emailInput.sendKeys(email);
        nameInput.sendKeys(name);
        genderSelect.sendKeys(gender);
        sendButton.click();

        /*WebElement error = driver.findElement(By.id("emailFormatError"));
        Assert.assertNotNull(error, "Ошибка неверного формата E-Mail не появилась.");
        Assert.assertEquals(error.getText(),
                "Неверный формат E-Mail",
                "текст ошибки не совпадает с ожидаемым");*/

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("uk-modal-dialog")));
        Assert.assertTrue(successMessage.isDisplayed(), "Сообщение о добавлении данных не отобразилось.");

        WebElement okButton = driver.findElement(By.cssSelector(".uk-button.uk-button-primary.uk-modal-close"));
        okButton.click();

        WebElement table = driver.findElement(By.id("dataTable"));
        WebElement lastRow = table.findElement(By.xpath(".//tbody/tr[last()]"));

        String emailColumn = lastRow.findElement(By.xpath(".//td[1]")).getText();
        String nameColumn = lastRow.findElement(By.xpath(".//td[2]")).getText();
        String genderColumn = lastRow.findElement(By.xpath(".//td[3]")).getText();
        String firstChoiceColumn = lastRow.findElement(By.xpath(".//td[4]")).getText();
        String secondChoiceColumn = lastRow.findElement(By.xpath(".//td[5]")).getText();

        Assert.assertEquals(emailColumn, email, "Email в таблице не соответствует ожидаемому.");
        Assert.assertEquals(nameColumn, name, "Имя в таблице не соответствует ожидаемому.");
        Assert.assertEquals(genderColumn, gender, "Пол в таблице не соответствует ожидаемому.");
        Assert.assertEquals(firstChoiceColumn, "Нет", "Выбор1 в таблице не соответствует ожидаемому.");
        Assert.assertEquals(secondChoiceColumn, "", "Выбор2 в таблице не соответствует ожидаемому.");
    }

    @Test(description = "Валидная почта, пустое имя")
    public void testSubmitFormWithEmptyName() {
        String email = RandomStringUtils.randomAlphanumeric(8) + "@domain.com";
        String name = "";
        String gender = "Мужской";

        WebElement emailInput = driver.findElement(By.id("dataEmail"));
        WebElement nameInput = driver.findElement(By.id("dataName"));
        WebElement genderSelect = driver.findElement(By.id("dataGender"));
        WebElement sendButton = driver.findElement(By.id("dataSend"));

        emailInput.sendKeys(email);
        nameInput.sendKeys(name);
        genderSelect.sendKeys(gender);
        sendButton.click();

        WebElement error = driver.findElement(By.id("blankNameError"));
        Assert.assertNotNull(error, "Ошибка пустого имени не появилась.");
        Assert.assertEquals(error.getText(),
                "Поле имя не может быть пустым",
                "текст ошибки не совпадает с ожидаемым");

    }

    @Test(description = "Пустая почта, непустое имя")
    public void testSubmitFormWithEmptyEmail() {
        String email = "";
        String name = RandomStringUtils.randomAlphanumeric(8);
        String gender = "Мужской";

        WebElement emailInput = driver.findElement(By.id("dataEmail"));
        WebElement nameInput = driver.findElement(By.id("dataName"));
        WebElement genderSelect = driver.findElement(By.id("dataGender"));
        WebElement sendButton = driver.findElement(By.id("dataSend"));

        emailInput.sendKeys(email);
        nameInput.sendKeys(name);
        genderSelect.sendKeys(gender);
        sendButton.click();

        WebElement error = driver.findElement(By.id("emailFormatError"));
        Assert.assertNotNull(error, "Ошибка неверного формата E-Mail не появилась.");
        Assert.assertEquals(error.getText(),
                "Неверный формат E-Mail",
                "текст ошибки не совпадает с ожидаемым");

    }

    @DataProvider(name = "firstFormData")
    public Object[][] createFirstData() {
        return new Object[][]{
                {"dataCheck11", "1.1"},
                {"dataCheck12", "1.2"}
        };
    }

    @Test(description = "Позитивка 1 отет в первом вопросе", dataProvider = "firstFormData")
    public void testSubmitFormOneChoiceInFirstQuestion(String checkboxId, String expectedFirstChoice) {
        String email = RandomStringUtils.randomAlphanumeric(8) + "@domain.com";
        String name = RandomStringUtils.randomAlphanumeric(8);
        String gender = "Мужской";

        WebElement emailInput = driver.findElement(By.id("dataEmail"));
        WebElement nameInput = driver.findElement(By.id("dataName"));
        WebElement genderSelect = driver.findElement(By.id("dataGender"));
        WebElement sendButton = driver.findElement(By.id("dataSend"));
        WebElement checkbox = driver.findElement(By.id(checkboxId));
        WebElement radioButton = driver.findElement(By.id("dataSelect22"));

        emailInput.sendKeys(email);
        nameInput.sendKeys(name);
        genderSelect.sendKeys(gender);
        checkbox.click();
        radioButton.click();
        sendButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("uk-modal-dialog")));
        Assert.assertTrue(successMessage.isDisplayed(), "Сообщение о добавлении данных не отобразилось.");

        WebElement okButton = driver.findElement(By.cssSelector(".uk-button.uk-button-primary.uk-modal-close"));
        okButton.click();

        WebElement table = driver.findElement(By.id("dataTable"));
        WebElement lastRow = table.findElement(By.xpath(".//tbody/tr[last()]"));

        String emailColumn = lastRow.findElement(By.xpath(".//td[1]")).getText();
        String nameColumn = lastRow.findElement(By.xpath(".//td[2]")).getText();
        String genderColumn = lastRow.findElement(By.xpath(".//td[3]")).getText();
        String firstChoiceColumn = lastRow.findElement(By.xpath(".//td[4]")).getText();
        String secondChoiceColumn = lastRow.findElement(By.xpath(".//td[5]")).getText();

        Assert.assertEquals(emailColumn, email, "Email в таблице не соответствует ожидаемому.");
        Assert.assertEquals(nameColumn, name, "Имя в таблице не соответствует ожидаемому.");
        Assert.assertEquals(genderColumn, gender, "Пол в таблице не соответствует ожидаемому.");
        Assert.assertEquals(firstChoiceColumn, expectedFirstChoice, "Выбор1 в таблице не соответствует ожидаемому.");
        Assert.assertEquals(secondChoiceColumn, "2.2", "Выбор2 в таблице не соответствует ожидаемому.");
    }

    @DataProvider(name = "secondFormData")
    public Object[][] createSecondData() {
        return new Object[][]{
                {"dataSelect21", "2.1"},
                {"dataSelect22", "2.2"},
                {"dataSelect23", "2.3"}
        };
    }

    @Test(description = "Позитивка 1 отет во втором вопросе", dataProvider = "secondFormData")
    public void testSubmitFormSecondChoice(String checkboxId, String expectedSecondChoice) {
        String email = RandomStringUtils.randomAlphanumeric(8) + "@domain.com";
        String name = RandomStringUtils.randomAlphanumeric(8);
        String gender = "Мужской";

        WebElement emailInput = driver.findElement(By.id("dataEmail"));
        WebElement nameInput = driver.findElement(By.id("dataName"));
        WebElement genderSelect = driver.findElement(By.id("dataGender"));
        WebElement sendButton = driver.findElement(By.id("dataSend"));
        WebElement radioButton = driver.findElement(By.id(checkboxId));

        emailInput.sendKeys(email);
        nameInput.sendKeys(name);
        genderSelect.sendKeys(gender);
        radioButton.click();
        sendButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("uk-modal-dialog")));
        Assert.assertTrue(successMessage.isDisplayed(), "Сообщение о добавлении данных не отобразилось.");

        WebElement okButton = driver.findElement(By.cssSelector(".uk-button.uk-button-primary.uk-modal-close"));
        okButton.click();

        WebElement table = driver.findElement(By.id("dataTable"));
        WebElement lastRow = table.findElement(By.xpath(".//tbody/tr[last()]"));

        String emailColumn = lastRow.findElement(By.xpath(".//td[1]")).getText();
        String nameColumn = lastRow.findElement(By.xpath(".//td[2]")).getText();
        String genderColumn = lastRow.findElement(By.xpath(".//td[3]")).getText();
        String firstChoiceColumn = lastRow.findElement(By.xpath(".//td[4]")).getText();
        String secondChoiceColumn = lastRow.findElement(By.xpath(".//td[5]")).getText();

        Assert.assertEquals(emailColumn, email, "Email в таблице не соответствует ожидаемому.");
        Assert.assertEquals(nameColumn, name, "Имя в таблице не соответствует ожидаемому.");
        Assert.assertEquals(genderColumn, gender, "Пол в таблице не соответствует ожидаемому.");
        Assert.assertEquals(firstChoiceColumn, "Нет", "Выбор1 в таблице не соответствует ожидаемому.");
        Assert.assertEquals(secondChoiceColumn, expectedSecondChoice, "Выбор2 в таблице не соответствует ожидаемому.");
    }

    //TODO функционально работает, но отображение ломается
    @Test(description = "Позитвка длинная почта и имя")
    public void testSubmitFormLongEmailAndName() {
        String email = RandomStringUtils.randomAlphanumeric(400) + "@domain.com";
        String name = RandomStringUtils.randomAlphanumeric(400);
        String gender = "Мужской";

        WebElement emailInput = driver.findElement(By.id("dataEmail"));
        WebElement nameInput = driver.findElement(By.id("dataName"));
        WebElement genderSelect = driver.findElement(By.id("dataGender"));
        WebElement sendButton = driver.findElement(By.id("dataSend"));

        emailInput.sendKeys(email);
        nameInput.sendKeys(name);
        genderSelect.sendKeys(gender);
        sendButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("uk-modal-dialog")));
        Assert.assertTrue(successMessage.isDisplayed(), "Сообщение о добавлении данных не отобразилось.");

        WebElement okButton = driver.findElement(By.cssSelector(".uk-button.uk-button-primary.uk-modal-close"));
        okButton.click();

        WebElement table = driver.findElement(By.id("dataTable"));
        WebElement lastRow = table.findElement(By.xpath(".//tbody/tr[last()]"));

        String emailColumn = lastRow.findElement(By.xpath(".//td[1]")).getText();
        String nameColumn = lastRow.findElement(By.xpath(".//td[2]")).getText();
        String genderColumn = lastRow.findElement(By.xpath(".//td[3]")).getText();
        String firstChoiceColumn = lastRow.findElement(By.xpath(".//td[4]")).getText();
        String secondChoiceColumn = lastRow.findElement(By.xpath(".//td[5]")).getText();

        Assert.assertEquals(emailColumn, email, "Email в таблице не соответствует ожидаемому.");
        Assert.assertEquals(nameColumn, name, "Имя в таблице не соответствует ожидаемому.");
        Assert.assertEquals(genderColumn, gender, "Пол в таблице не соответствует ожидаемому.");
        Assert.assertEquals(firstChoiceColumn, "Нет", "Выбор1 в таблице не соответствует ожидаемому.");
        Assert.assertEquals(secondChoiceColumn, "", "Выбор2 в таблице не соответствует ожидаемому.");
    }
}