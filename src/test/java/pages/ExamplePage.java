package pages;

import com.codeborne.selenide.Condition;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class ExamplePage {
    public ExamplePage openPage() {
        open("https://j17lt.csb.app/");
        return this;
    }

    public ExamplePage checkEmail(String email) {
        $$(".flex div p").filterBy(Condition.text(email)).first().shouldBe(Condition.visible);
        return this;
    }
}
