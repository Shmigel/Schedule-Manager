package com.shmigel.scheduleManager;

import com.shmigel.scheduleManager.model.SpeechBreakStrength;
import com.shmigel.scheduleManager.service.Speech;
import com.shmigel.scheduleManager.util.DateTimeUtil;
import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.boot.test.context.assertj.ApplicationContextAssert;

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
        DateTimeFormatter formatter = DateTimeFormat.forPattern("EE, MMMM dd");
        System.out.println(time.toString(formatter));
    }

    @Test
    public void t() {
        DateTime dateTime = new DateTime(System.currentTimeMillis());
        DateTimeFormatter monthDay = DateTimeFormat.forPattern("MMMM dd");
        DateTimeFormatter hourMinute = DateTimeFormat.forPattern("HH:mm");
        String build = new Speech().say("Your next event of " + "TEST_SUMMARY")
                .say("starts at")
                .sayAsTime("hm24", dateTime.toString(hourMinute))
                .say("on the")
                .sayAsDate("dd", String.valueOf(dateTime.getDayOfWeek()))
                .pause("300ms", SpeechBreakStrength.STRONG).sayAsDate("mmdd", dateTime.toString(monthDay))
                .build();

        System.out.println(build);
    }

    @Test
    public void dt() {
        System.out.println(new DateTime().plusDays(2));
    }

}
