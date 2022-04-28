package com.ags.menuapi.Menu;

public enum MenuSize {

    TINY, NINE,
    SMALL, ONEEIGHT,
    MEDIUM, TWOSEVEN,
    LARGE, THREESIX,
    HUGE, FOURFIVE,
    GIGANTIC, FIVEFOUR,
    ;

    private int number;
    private int rows;

    static {
        TINY.number = 9;
        NINE.number = 9;
        SMALL.number = 18;
        ONEEIGHT.number = 18;
        MEDIUM.number = 27;
        TWOSEVEN.number = 27;
        LARGE.number = 36;
        THREESIX.number = 36;
        HUGE.number = 45;
        FOURFIVE.number = 45;
        GIGANTIC.number = 54;
        FIVEFOUR.number = 54;

    }

    static {
        TINY.rows = 1;
        NINE.rows = 1;
        SMALL.rows = 2;
        ONEEIGHT.rows = 2;
        MEDIUM.rows = 3;
        TWOSEVEN.rows = 3;
        LARGE.rows = 4;
        THREESIX.rows = 4;
        HUGE.rows = 5;
        FOURFIVE.rows = 5;
        GIGANTIC.rows = 6;
        FIVEFOUR.rows = 6;

    }

    public int toNumber() {
        return number;
    }

    public int toRows() {
        return rows;
    }

    public int toColumns() {
        return 9;
    }
}
