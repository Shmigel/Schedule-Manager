package com.shmigel.scheduleManager;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Tuple<A, B> {

    private A first;

    private B second;

    public Tuple() {
    }
}
