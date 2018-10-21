package com.shmigel.scheduleManager;

import com.shmigel.scheduleManager.service.SimpleResponseBuilder;
import com.shmigel.scheduleManager.service.DateTimeFormatters;
import com.shmigel.scheduleManager.service.EventDescriptionParser;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Map;


import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class NonSpringBasedTest {

    @Test
    public void SSMLBuilderTest() {
        String speech = new SimpleResponseBuilder()
                .say("Hi")
                .pause("700ms")
                .sayAs("verbatim", "You").pause("400ms")
                .sayAs("verbatim", "are").pause("400ms")
                .sayAs("verbatim", "in").pause("400ms")
                .sayAs("verbatim", "my").pause("400ms")
                .sayAs("verbatim", "test").pause("700ms")
                .sayAs("expletive", "secret textToSpeech")
                .fullfulmentText();
        String prepareSpeech = "<speak> Hi <break time=\"700ms\"/><say-as interpret-as=\"verbatim\">You</say-as> <break time=\"400ms\"/><say-as interpret-as=\"verbatim\">are</say-as> <break time=\"400ms\"/><say-as interpret-as=\"verbatim\">in</say-as> <break time=\"400ms\"/><say-as interpret-as=\"verbatim\">my</say-as> <break time=\"400ms\"/><say-as interpret-as=\"verbatim\">test</say-as> <break time=\"700ms\"/><say-as interpret-as=\"expletive\">secret textToSpeech</say-as> </speak>";
        assertEquals(speech, prepareSpeech);
    }

    @Test
    public void test() {
        DateTime time = new DateTime();
        System.out.println(time.toString(DateTimeFormatters.hourMinute.formatter()));
        System.out.println(time.toString(DateTimeFormatters.monthDay.formatter()));
        System.out.println(time.toString(DateTimeFormatters.dayOfWeak.formatter()));
    }

    @Test
    public void scalaDescriptionParser() {
        String preparedDescription = "author: Shmigel \n place: kc-2 \n Leson on something";
        Map<String, String> split = new EventDescriptionParser().split(preparedDescription);
        assertEquals(split.toString(), "{author=Shmigel, place=kc-2}");

        Map<String, String> split1 = new EventDescriptionParser().split(null);
        assertEquals(split1.toString(), "{}");
    }

}
