package test;

import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

public class PracticeFormTests extends TestBase{
    private static final String formUrl = "/automation-practice-form";
    private static final Set<Integer> numFieldsForShortSubmit = new HashSet<>(Set.of(1,3,4,5));
    private static final LinkedHashMap<String, String> correctFormData = new LinkedHashMap<>(){{
        put("Student Name", "firstName lastName");
        put("Student Email", "user@email.com");
        put("Gender", "Other");
        put("Mobile", "1234567890");
        put("Date of Birth", "31 December,1999");
        put("Subjects", "Maths");
        put("Hobbies", "Reading");
        put("Picture", "images.jpg");
        put("Address", "currentAddress");
        put("State and City", "NCR Delhi");
    }};



    @Test
    @Description("Позитивно проверяются только обязательные поля формы")
    void positiveShortSubmitPracticeFormTest(){

        open(formUrl);
        $(byId("firstName")).setValue(correctFormData.get("Student Name").split(" ")[0]);

        $(byId("lastName")).setValue(correctFormData.get("Student Name").split(" ")[1]);

        $(byId("genterWrapper")).$(byValue(correctFormData.get("Gender"))).click();
        $(byId("userNumber")).setValue(correctFormData.get("Mobile"));

        $(byId("dateOfBirthInput")).click();
        $(byClassName("react-datepicker__year-select")).selectOption(correctFormData.get("Date of Birth").split(",")[1]);
        $(byClassName("react-datepicker__month-select")).selectOption(correctFormData.get("Date of Birth").split(",")[0].split(" ")[1]);
        $(byClassName("react-datepicker__month")).$(byText(correctFormData.get("Date of Birth").split(",")[0].split(" ")[0])).click();

        $(byId("submit")).click();

        $(byClassName("table-responsive")).should(appear);

        $(byClassName("table-responsive")).$("tbody").$$("tr").forEach(x->{
            String key = x.$$("td").get(0).text();
            Integer numField = (new ArrayList<>(correctFormData.keySet())).indexOf(key) + 1;
            if (numFieldsForShortSubmit.contains(numField)) {
                x.$$("td").get(1).shouldHave(text(correctFormData.get(key)));
            }
        });
    }


    @Test
    @Description("Позитивно проверяются все поля формы, а не только обязательные")
    void positiveFullSubmitPracticeFormTest(){

        open(formUrl);
        $(byId("firstName")).setValue(correctFormData.get("Student Name").split(" ")[0]);

        $(byId("lastName")).setValue(correctFormData.get("Student Name").split(" ")[1]);

        $(byId("userEmail")).setValue(correctFormData.get("Student Email"));

        $(byId("genterWrapper")).$(byValue(correctFormData.get("Gender"))).click();

        $(byId("userNumber")).setValue(correctFormData.get("Mobile"));

        $(byId("dateOfBirthInput")).click();
        $(byClassName("react-datepicker__year-select")).selectOption(correctFormData.get("Date of Birth").split(",")[1]);
        $(byClassName("react-datepicker__month-select")).selectOption(correctFormData.get("Date of Birth").split(",")[0].split(" ")[1]);
        $(byClassName("react-datepicker__month")).$(byText(correctFormData.get("Date of Birth").split(",")[0].split(" ")[0])).click();

        $(byId("subjectsInput")).sendKeys(correctFormData.get("Subjects"));
        $("[aria-activedescendant=react-select-2-option-0]").should(appear)
                .shouldHave(attribute("value",correctFormData.get("Subjects"))).sendKeys(Keys.ENTER);

        $(byId("hobbiesWrapper")).$(byText(correctFormData.get("Hobbies"))).click();

        $(byId("uploadPicture")).uploadFile(new File("src/test/resources/" + correctFormData.get("Picture")));

        $("textarea[id=currentAddress]").setValue(correctFormData.get("Address"));

        $(byId("react-select-3-input")).sendKeys(correctFormData.get("State and City").split(" ")[0]);
        $(byId("react-select-3-input")).shouldHave(attribute("value",correctFormData.get("State and City").split(" ")[0]))
                .sendKeys(Keys.ENTER);

        $(byId("react-select-4-input")).sendKeys(correctFormData.get("State and City").split(" ")[1]);
        $(byId("react-select-4-input")).shouldHave(attribute("value",correctFormData.get("State and City").split(" ")[1]))
                .sendKeys(Keys.ENTER);

        $(byId("submit")).click();

        $(byClassName("table-responsive")).should(appear);

        $(byClassName("table-responsive")).$("tbody").$$("tr").forEach(x->{
            x.$$("td").get(1).shouldHave(text(correctFormData.get(x.$$("td").get(0).text())));
        });
    }
}
