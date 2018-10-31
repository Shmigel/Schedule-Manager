package com.shmigel.scheduleManager.service;

import io.vavr.Tuple2;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EventDescriptionParser {

    public Map<String, String> split(String text) {
        if (text != null) {
            return Stream.of(text.split("\n")).filter((i) -> i.contains(":")).map(this::extractPair)
                    .collect(Collectors.toMap(Tuple2::_1, Tuple2::_2));
        } else
            return Collections.emptyMap();
    }


    private Tuple2<String, String> extractPair(String text) {
        List<String> collect = Stream.of(text.split(":")).map(String::trim).collect(Collectors.toList());
        return new Tuple2<>(collect.get(0), collect.get(1));
    }

}
