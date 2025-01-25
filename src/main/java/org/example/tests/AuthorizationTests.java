package org.example.tests;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import java.io.File;

public class AuthorizationTests {
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
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(description = "Успешная авторизация")
    public void testSuccessAuth() {
        String email = "test@protei.ru";
        String password = "test";

        WebElement emailInput = driver.findElement(By.id("loginEmail"));
        WebElement passwordInput = driver.findElement(By.id("loginPassword"));
        WebElement authButton = driver.findElement(By.id("authButton"));

        emailInput.sendKeys(email);
        passwordInput.sendKeys(password);
        authButton.click();

        WebElement inputsPage = driver.findElement(By.id("inputsPage"));
        Assert.assertTrue(inputsPage.isDisplayed(), "Страница анкеты не отобразилась после успешного входа.");
    }

    @Test(description = "Невалидный формат почты")
    public void testInvalidEmailFormat() {
        String email = RandomStringUtils.randomAlphanumeric(10);
        String password = "test";

        WebElement emailInput = driver.findElement(By.id("loginEmail"));
        WebElement passwordInput = driver.findElement(By.id("loginPassword"));
        WebElement authButton = driver.findElement(By.id("authButton"));

        emailInput.sendKeys(email);
        passwordInput.sendKeys(password);
        authButton.click();

        WebElement error = driver.findElement(By.id("emailFormatError"));
        Assert.assertNotNull(error, "Ошибка в формате E-Mail не появилась.");
        Assert.assertEquals(
                error.getText(),
                "Неверный формат E-Mail",
                "текст ошибки не совпадает с ожидаемым");
    }

    //TODO кейс с неверным форматом, но при этом другая ошибка - баг
    //по логике должна быть "Неверный формат E-Mail"
    //но получаем "Неверный E-Mail или пароль"
    @Test(description = "Невалидный формат почты, без .*")
    public void testInvalidEmailFormatBag() {
        String email = RandomStringUtils.randomAlphanumeric(10) + "@domain";
        String password = "test";

        WebElement emailInput = driver.findElement(By.id("loginEmail"));
        WebElement passwordInput = driver.findElement(By.id("loginPassword"));
        WebElement authButton = driver.findElement(By.id("authButton"));

        emailInput.sendKeys(email);
        passwordInput.sendKeys(password);
        authButton.click();

        WebElement error = driver.findElement(By.id("invalidEmailPassword"));
        Assert.assertNotNull(error, "Ошибка неверного E-Mail или пароля не появилась.");
        Assert.assertEquals(error.getText(),
                "Неверный E-Mail или пароль",
                "текст ошибки не совпадает с ожидаемым");

        /*WebElement error = driver.findElement(By.id("emailFormatError"));
        Assert.assertNotNull(error, "Ошибка в формате E-Mail не появилась.");
        Assert.assertEquals(
                error.getText(),
                "Неверный формат E-Mail",
                "текст ошибки не совпадает с ожидаемым");*/
    }

    @Test(description = "Невалидный пароль")
    public void testInvalidPass() {
        String email = "test@protei.ru";
        String password = RandomStringUtils.randomAlphanumeric(10);

        WebElement emailInput = driver.findElement(By.id("loginEmail"));
        WebElement passwordInput = driver.findElement(By.id("loginPassword"));
        WebElement authButton = driver.findElement(By.id("authButton"));

        emailInput.sendKeys(email);
        passwordInput.sendKeys(password);
        authButton.click();

        WebElement error = driver.findElement(By.id("invalidEmailPassword"));
        Assert.assertNotNull(error, "Ошибка неверного E-Mail или пароля не появилась.");
        Assert.assertEquals(error.getText(),
                "Неверный E-Mail или пароль",
                "текст ошибки не совпадает с ожидаемым");
    }

    @Test(description = "Валидный формат почты, но нет данных с такой учеткой")
    public void testInvalidCredentials() {
        String email = RandomStringUtils.randomAlphanumeric(8) + "@domain.com";
        String password = RandomStringUtils.randomAlphanumeric(10);

        WebElement emailInput = driver.findElement(By.id("loginEmail"));
        WebElement passwordInput = driver.findElement(By.id("loginPassword"));
        WebElement authButton = driver.findElement(By.id("authButton"));

        emailInput.sendKeys(email);
        passwordInput.sendKeys(password);
        authButton.click();

        WebElement error = driver.findElement(By.id("invalidEmailPassword"));
        Assert.assertNotNull(error, "Ошибка неверного E-Mail или пароля не появилась.");
        Assert.assertEquals(error.getText(),
                "Неверный E-Mail или пароль",
                "текст ошибки не совпадает с ожидаемым");
    }

    @Test(description = "Пустая почта и пароль")
    public void testZeroLenthEmailAndPass() {
        String email = "";
        String password = "";

        WebElement emailInput = driver.findElement(By.id("loginEmail"));
        WebElement passwordInput = driver.findElement(By.id("loginPassword"));
        WebElement authButton = driver.findElement(By.id("authButton"));

        emailInput.sendKeys(email);
        passwordInput.sendKeys(password);
        authButton.click();

        WebElement error = driver.findElement(By.id("emailFormatError"));
        Assert.assertNotNull(error, "Ошибка в формате E-Mail не появилась.");
        Assert.assertEquals(
                error.getText(),
                "Неверный формат E-Mail",
                "текст ошибки не совпадает с ожидаемым");
    }

    @Test(description = "Валидная почта, пустой пароль")
    public void testZeroLenthPass() {
        String email = "test@protei.ru";
        String password = "";

        WebElement emailInput = driver.findElement(By.id("loginEmail"));
        WebElement passwordInput = driver.findElement(By.id("loginPassword"));
        WebElement authButton = driver.findElement(By.id("authButton"));

        emailInput.sendKeys(email);
        passwordInput.sendKeys(password);
        authButton.click();

        WebElement error = driver.findElement(By.id("invalidEmailPassword"));
        Assert.assertNotNull(error, "Ошибка неверного E-Mail или пароля не появилась.");
        Assert.assertEquals(error.getText(),
                "Неверный E-Mail или пароль",
                "текст ошибки не совпадает с ожидаемым");
    }

    @Test(description = "Пустая почта, не пустой пароль")
    public void testZeroLenthEmail() {
        String email = "";
        String password = "test";

        WebElement emailInput = driver.findElement(By.id("loginEmail"));
        WebElement passwordInput = driver.findElement(By.id("loginPassword"));
        WebElement authButton = driver.findElement(By.id("authButton"));

        emailInput.sendKeys(email);
        passwordInput.sendKeys(password);
        authButton.click();

        WebElement error = driver.findElement(By.id("emailFormatError"));
        Assert.assertNotNull(error, "Ошибка в формате E-Mail не появилась.");
        Assert.assertEquals(
                error.getText(),
                "Неверный формат E-Mail",
                "текст ошибки не совпадает с ожидаемым");
    }

    @Test(description = "Очень длинная почта и пароль")
    public void testLongCredentials() {
        String email = RandomStringUtils.randomAlphanumeric(300) + "@domain.com";
        String password = RandomStringUtils.randomAlphanumeric(300);

        WebElement emailInput = driver.findElement(By.id("loginEmail"));
        WebElement passwordInput = driver.findElement(By.id("loginPassword"));
        WebElement authButton = driver.findElement(By.id("authButton"));

        emailInput.sendKeys(email);
        passwordInput.sendKeys(password);
        authButton.click();

        WebElement error = driver.findElement(By.id("invalidEmailPassword"));
        Assert.assertNotNull(error, "Ошибка неверного E-Mail или пароля не появилась.");
        Assert.assertEquals(error.getText(),
                "Неверный E-Mail или пароль",
                "текст ошибки не совпадает с ожидаемым");
    }

    @Test(description = "Sql-инъекция если это был бы полноценный веб сервис")
    public void testSqlInjectionInLogin() {
        String email = "test@protei.ru' OR '1'='1";
        String password = "test' OR '1'='1";

        WebElement emailInput = driver.findElement(By.id("loginEmail"));
        WebElement passwordInput = driver.findElement(By.id("loginPassword"));
        WebElement authButton = driver.findElement(By.id("authButton"));

        emailInput.sendKeys(email);
        passwordInput.sendKeys(password);
        authButton.click();

        WebElement error = driver.findElement(By.id("invalidEmailPassword"));
        Assert.assertNotNull(error, "Ошибка неверного E-Mail или пароля не появилась.");
        Assert.assertEquals(error.getText(),
                "Неверный E-Mail или пароль",
                "текст ошибки не совпадает с ожидаемым");
        Assert.assertFalse(driver.findElement(By.id("inputsPage")).isDisplayed(),
                "Анкета не должна быть доступна");
    }
}