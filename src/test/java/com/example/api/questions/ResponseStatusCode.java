package com.example.api.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.rest.questions.TheResponse;

public class ResponseStatusCode implements Question<Integer> {

    public static ResponseStatusCode fromLastResponse() {
        return new ResponseStatusCode();
    }

    @Override
    public Integer answeredBy(Actor actor) {
        return actor.asksFor(TheResponse.statusCode());
    }
}
