package com.shmigel.scheduleManager.service;

import com.shmigel.scheduleManager.Tuple;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EventDescriptionParser {

    public Map<String, String> split(String text) {
        if (text != null) {
            return Stream.of(text.split("\n")).filter((i) -> i.contains(":")).map(this::extractPair)
                    .collect(Collectors.toMap(Tuple::getFirst, Tuple::getSecond));
        } else
            return Collections.emptyMap();
    }


    private Tuple<String, String> extractPair(String text) {
        List<String> collect = Stream.of(text.split(":")).map(String::trim).collect(Collectors.toList());
        return  new Tuple<>(collect.get(0), collect.get(1));
    }

}
