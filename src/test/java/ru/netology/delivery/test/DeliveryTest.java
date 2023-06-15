package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.github.javafaker.Faker;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import javax.xml.crypto.Data;
import java.time.Duration;
import java.util.Locale;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;


class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        // TODO: добавить логику теста в рамках которого будет выполнено планирование и перепланирование встречи.
        // Для заполнения полей формы можно использовать пользователя validUser и строки с датами в переменных
        // firstMeetingDate и secondMeetingDate. Можно также вызывать методы generateCity(locale),
        // generateName(locale), generatePhone(locale) для генерации и получения в тесте соответственно города,
        // имени и номера телефона без создания пользователя в методе generateUser(String locale) в датагенераторе
        open("http://localhost:9999/");
        $("[data-test-id= 'city'] input").setValue(validUser.getCity());
        $("[data-test-id= 'date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id= 'date'] input").setValue(firstMeetingDate);
        $("[data-test-id= 'name'] input").setValue(validUser.getName());
        $("[data-test-id= 'phone'] input").setValue(validUser.getPhone());
        $("[data-test-id= 'agreement']").click();
        $$("[type= 'button']").find(Condition.text("Запланировать")).click();
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
        $$("[type= 'button']").filter(Condition.visible).first().click();
        $("[data-test-id= 'date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id= 'date'] input").setValue(secondMeetingDate);
        $$("[type= 'button']").find(Condition.text("Запланировать")).click();
        $("[data-test-id= 'replan-notification']")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"), Duration.ofSeconds(20));
        $$("[type= 'button']").filter(Condition.visible).first().click();
        //$("[data-test-id= 'success-notification']").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + secondMeetingDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should not accept phone of 10 numbers")
    void shouldNotAcceptPhone10Numbers() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        open("http://localhost:9999/");
        $("[data-test-id= 'city'] input").setValue(validUser.getCity());
        $("[data-test-id= 'date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id= 'date'] input").setValue(DataGenerator.generateDate(5));
        $("[data-test-id= 'name'] input").setValue(validUser.getName());
        $("[data-test-id= 'phone'] input").setValue("+7951256789");
        $("[data-test-id= 'agreement']").click();
        $$("[type= 'button']").find(Condition.text("Запланировать")).click();
        $("[data-test-id= 'phone'].input_invalid .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    @DisplayName("Should not accept phone of 1 number")
    void shouldNotAcceptPhoneOf1Number() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        open("http://localhost:9999/");
        $("[data-test-id= 'city'] input").setValue(validUser.getCity());
        $("[data-test-id= 'date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id= 'date'] input").setValue(DataGenerator.generateDate(5));
        $("[data-test-id= 'name'] input").setValue(validUser.getName());
        $("[data-test-id= 'phone'] input").setValue("+7");
        $("[data-test-id= 'agreement']").click();
        $$("[type= 'button']").find(Condition.text("Запланировать")).click();
        $("[data-test-id= 'phone'].input_invalid .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    @DisplayName("Should not accept phone of plus")
    void shouldNotAcceptPhoneOfPlus() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        open("http://localhost:9999/");
        $("[data-test-id= 'city'] input").setValue(validUser.getCity());
        $("[data-test-id= 'date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id= 'date'] input").setValue(DataGenerator.generateDate(5));
        $("[data-test-id= 'name'] input").setValue(validUser.getName());
        $("[data-test-id= 'phone'] input").setValue("+");
        $("[data-test-id= 'agreement']").click();
        $$("[type= 'button']").find(Condition.text("Запланировать")).click();
        $("[data-test-id= 'phone'].input_invalid .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    @DisplayName("Should not replan if phone changed")
    void shouldNotReplanIfPhoneChanged() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 5;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);

        open("http://localhost:9999/");
        $("[data-test-id= 'city'] input").setValue(validUser.getCity());
        $("[data-test-id= 'date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id= 'date'] input").setValue(firstMeetingDate);
        $("[data-test-id= 'name'] input").setValue(validUser.getName());
        $("[data-test-id= 'phone'] input").setValue(validUser.getPhone());
        $("[data-test-id= 'agreement']").click();
        $$("[type= 'button']").find(Condition.text("Запланировать")).click();
        $(".notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(15));
        $$("[type= 'button']").filter(Condition.visible).first().click();
        $("[data-test-id= 'phone'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id= 'phone'] input").setValue(validUser.getPhone());
        $$("[type= 'button']").find(Condition.text("Запланировать")).click();
        $("[data-test-id= 'replan-notification']")
                .shouldBe(Condition.hidden)
                .shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"), Duration.ofSeconds(20));

    }

    @Test
    @DisplayName("Should not replan if same date")
    void shouldNotReplanIfSameDate() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);

        open("http://localhost:9999/");
        $("[data-test-id= 'city'] input").setValue(validUser.getCity());
        $("[data-test-id= 'date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id= 'date'] input").setValue(firstMeetingDate);
        $("[data-test-id= 'name'] input").setValue(validUser.getName());
        $("[data-test-id= 'phone'] input").setValue(validUser.getPhone());
        $("[data-test-id= 'agreement']").click();
        $$("[type= 'button']").find(Condition.text("Запланировать")).click();
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
        $$("[type= 'button']").filter(Condition.visible).first().click();
        $("[data-test-id= 'date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id= 'date'] input").setValue(firstMeetingDate);
        $$("[type= 'button']").find(Condition.text("Запланировать")).click();
        $("[data-test-id= 'replan-notification']")
                .shouldBe(Condition.hidden)
                .shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"), Duration.ofSeconds(20));

    }

    @Test
    @DisplayName("Should not accept name of hyphens")
    void shouldNotAcceptNameOfHyphens() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        open("http://localhost:9999/");
        $("[data-test-id= 'city'] input").setValue(validUser.getCity());
        $("[data-test-id= 'date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id= 'date'] input").setValue(DataGenerator.generateDate(5));
        $("[data-test-id= 'name'] input").setValue("------");
        $("[data-test-id= 'phone'] input").setValue(validUser.getPhone());
        $("[data-test-id= 'agreement']").click();
        $$("[type= 'button']").find(Condition.text("Запланировать")).click();
        $("[data-test-id= 'name'].input_invalid .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    @DisplayName("Should accept name of Cyrillic")
    void shouldAcceptNameOfCyrillic() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        open("http://localhost:9999/");
        $("[data-test-id= 'city'] input").setValue(validUser.getCity());
        $("[data-test-id= 'date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id= 'date'] input").setValue(DataGenerator.generateDate(5));
        $("[data-test-id= 'name'] input").setValue("Пётр Иванов");
        $("[data-test-id= 'phone'] input").setValue("+79512567891");
        $("[data-test-id= 'agreement']").click();
        $$("[type= 'button']").find(Condition.text("Запланировать")).click();
        $(".notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("Встреча успешно запланирована на " + DataGenerator.generateDate(5)), Duration.ofSeconds(15));
    }

}
