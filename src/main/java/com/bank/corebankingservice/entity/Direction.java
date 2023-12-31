package com.bank.corebankingservice.entity;

import lombok.Getter;

@Getter
public enum Direction {
    IN(1), OUT(2);
    public final int value;

    Direction(int value) {
        this.value = value;
    }

}
