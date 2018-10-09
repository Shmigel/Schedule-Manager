package com.shmigel.scheduleManager;

import com.shmigel.scheduleManager.service.Speech;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class NonSpringBasedTest {

    @Test
    public void SSMLBuilderTest() {
        String speech = new Speech()
                .say("Hi")
                .pause("700ms")
                .sayAs("verbatim", "You").pause("400ms")
                .sayAs("verbatim", "are").pause("400ms")
                .sayAs("verbatim", "in").pause("400ms")
                .sayAs("verbatim", "my").pause("400ms")
                .sayAs("verbatim", "test").pause("700ms")
                .sayAs("expletive", "secret message")
                .build();
        String prepareSpeech = "<speak>Hi<break time=\"700ms\"/><say-as interpret-as=\"verbatim\">You</say-as><break time=\"400ms\"/><say-as interpret-as=\"verbatim\">are</say-as><break time=\"400ms\"/><say-as interpret-as=\"verbatim\">in</say-as><break time=\"400ms\"/><say-as interpret-as=\"verbatim\">my</say-as><break time=\"400ms\"/><say-as interpret-as=\"verbatim\">test</say-as><break time=\"700ms\"/><say-as interpret-as=\"expletive\">secret message</say-as></speak>";
		assertEquals(speech, prepareSpeech);
    }

    @Test
    public void test() {
        DateTime time = new DateTime();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM");
        System.out.println(time.toString(formatter));
    }

}
