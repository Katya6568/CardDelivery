package ru.netology.delivery.data;

import com.github.javafaker.Faker;
import lombok.Data;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

@Data
public class DataGenerator {
    private DataGenerator() {
    }

    public static String generateDate(int shift) {
        return LocalDate.now().plusDays(shift).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

    }

    public static String generateCity() {
        String[] cities = {
                "Архангельск",
                "Астрахань",
                "Благовещенск",
                "Владивосток",
                "Горно-Алтайск",
                "Краснодар",
                "Красноярск",
                "Магас",
                "Мурманск",
                "Саранск",
                "Петропавловск-Камчатский",
                "Хабаровск",
                "Ханты-Мансийск",
                "Южно-Сахалинск",
                "Москва",
                "Санкт-Петербург"
        };

        Random random = new Random();
        String city = cities[random.nextInt(cities.length)];
        return city;
    }

    public static String generateName(String locale) {
        Faker faker = new Faker(new Locale(locale));
        return faker.name().fullName();
    }

    public static String generatePhone(String locale) {
        Faker faker = new Faker(new Locale(locale));
        return faker.phoneNumber().phoneNumber();
    }

    public static class Registration {
        private Registration() {
        }

     public static UserInfo generateUser(String locale) {
        return new UserInfo(generateCity(), generateName(locale), generatePhone(locale));
     }
    }

    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
    }
}
