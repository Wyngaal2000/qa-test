package org.example.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;

public class AuthorizationTests {
    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.get("https://www.google.com");
        String filePath = new File("src/main/resources/qa-test.html").getAbsolutePath();
        driver.get("file:///" + filePath);
    }

    @Test(description = "Невалидная почта")
    public void testInvalidEmailFormat() {
        WebElement emailInput = driver.findElement(By.id("loginEmail"));
        WebElement passwordInput = driver.findElement(By.id("loginPassword"));
        WebElement authButton = driver.findElement(By.id("authButton"));

        emailInput.sendKeys("invalidemail");
        passwordInput.sendKeys("test");
        authButton.click();

        WebElement error = driver.findElement(By.id("emailFormatError"));
        Assert.assertNotNull(error, "Ошибка в формате E-Mail не появилась.");
    }

    @Test(description = "Невалидная пароль")
    public void testInvalidCredentials() {
        WebElement emailInput = driver.findElement(By.id("loginEmail"));
        WebElement passwordInput = driver.findElement(By.id("loginPassword"));
        WebElement authButton = driver.findElement(By.id("authButton"));

        emailInput.sendKeys("test@protei.ru");
        passwordInput.sendKeys("wrongpassword");
        authButton.click();

        WebElement error = driver.findElement(By.id("invalidEmailPassword"));
        Assert.assertNotNull(error, "Ошибка неверного E-Mail или пароля не появилась.");
    }

    @Test(description = "Успешная авторизация")
    public void testSuccessfulLogin() {
        WebElement emailInput = driver.findElement(By.id("loginEmail"));
        WebElement passwordInput = driver.findElement(By.id("loginPassword"));
        WebElement authButton = driver.findElement(By.id("authButton"));

        emailInput.sendKeys("test@protei.ru");
        passwordInput.sendKeys("test");
        authButton.click();

        WebElement inputsPage = driver.findElement(By.id("inputsPage"));
        Assert.assertTrue(inputsPage.isDisplayed(), "Страница анкеты не отобразилась после успешного входа.");
    }

    @Test
    public void testSubmitFormWithValidData() {
        testSuccessfulLogin(); // Сначала проходим авторизацию

        WebElement emailInput = driver.findElement(By.id("dataEmail"));
        WebElement nameInput = driver.findElement(By.id("dataName"));
        WebElement genderSelect = driver.findElement(By.id("dataGender"));
        WebElement sendButton = driver.findElement(By.id("dataSend"));

        emailInput.sendKeys("validemail@domain.com");
        nameInput.sendKeys("John Doe");
        genderSelect.sendKeys("Женский");
        sendButton.click();

        WebElement successMessage = driver.findElement(By.className("uk-modal-body"));
        Assert.assertTrue(successMessage.isDisplayed(), "Сообщение о добавлении данных не отобразилось.");
    }

    @Test
    public void testInvalidFormData() {
        testSuccessfulLogin(); // Сначала проходим авторизацию

        WebElement emailInput = driver.findElement(By.id("dataEmail"));
        WebElement nameInput = driver.findElement(By.id("dataName"));
        WebElement sendButton = driver.findElement(By.id("dataSend"));

        emailInput.sendKeys("invalidemail");
        nameInput.sendKeys("");  // Пустое имя
        sendButton.click();

        WebElement emailError = driver.findElement(By.id("emailFormatError"));
        WebElement nameError = driver.findElement(By.id("blankNameError"));

        Assert.assertNotNull(emailError, "Ошибка неверного формата E-Mail не появилась.");
        Assert.assertNotNull(nameError, "Ошибка пустого имени не появилась.");
    }

    @Test
    public void testFormSubmissionWithSelections() {
        testSuccessfulLogin(); // Сначала проходим авторизацию

        WebElement emailInput = driver.findElement(By.id("dataEmail"));
        WebElement nameInput = driver.findElement(By.id("dataName"));
        WebElement genderSelect = driver.findElement(By.id("dataGender"));
        WebElement checkbox1 = driver.findElement(By.id("dataCheck11"));
        WebElement radioButton = driver.findElement(By.id("dataSelect22"));
        WebElement sendButton = driver.findElement(By.id("dataSend"));

        emailInput.sendKeys("validemail@domain.com");
        nameInput.sendKeys("Jane Doe");
        genderSelect.sendKeys("Мужской");
        checkbox1.click();
        radioButton.click();
        sendButton.click();

        WebElement successMessage = driver.findElement(By.className("uk-modal-body"));
        Assert.assertTrue(successMessage.isDisplayed(), "Сообщение о добавлении данных не отобразилось.");
    }

    /*@AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }*/
}
