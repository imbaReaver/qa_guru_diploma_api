package pages;

import com.codeborne.selenide.Condition;

import static com.codeborne.selenide.Selenide.*;

public class ExamplePage {
    public ExamplePage openPage() {
        open();
        return this;
    }

    public ExamplePage checkEmail(String email) {
        $$(".flex div p").filterBy(Condition.text(email)).first().shouldBe(Condition.visible);
        return this;
    }
}
