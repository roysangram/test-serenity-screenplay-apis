package com.example.api.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.rest.questions.LastResponse;

public class ResponseBody implements Question<String> {

    public static ResponseBody fromLastResponse() {
        return new ResponseBody();
    }

    @Override
    public String answeredBy(Actor actor) {
        return actor.asksFor(LastResponse.received()).getBody().asString();
    }
}
