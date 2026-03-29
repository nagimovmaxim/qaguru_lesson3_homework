package test;

import jdk.jfr.Description;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import static com.codeborne.selenide.Condition.cssValue;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class TextBoxTests extends TestBase{
    private static final String formUrl = "/text-box";
    private static final LinkedHashMap<String, String> correctFormData = new LinkedHashMap<>(){{
        put("name", "Name");
        put("email", "user@email.com");
        put("currentAddress", "Current Address");
        put("permanentAddress", "Permananet Address");
    }};

    @Test
    @Description("Негативная проверка на неправильнео заполнение почты")
    void negativeMailErrorTextBoxTest(){
        open(formUrl);
        $(byId("userEmail")).setValue("qwert");
        $(byId("submit")).click();
        $(byId("userEmail")).shouldHave(cssValue("border-color", "rgb(255, 0, 0)"));
    }

    @Test
    @Description("ПОзитивная проверка заполнения всех полей")
    void successfulFullSubmitTextBoxTest(){
        open(formUrl);
        $(byId("userName")).setValue(correctFormData.get("name"));
        $(byId("userEmail")).setValue(correctFormData.get("email"));
        $(byId("currentAddress")).setValue(correctFormData.get("currentAddress"));
        $(byId("permanentAddress")).setValue(correctFormData.get("permanentAddress"));
        $(byId("submit")).click();

        correctFormData.keySet().forEach(x->{
            $(byId("output")).$(byId(x)).shouldHave(text(correctFormData.get(x)));
        });
    }
}
