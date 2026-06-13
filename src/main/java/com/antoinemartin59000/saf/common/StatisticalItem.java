package com.antoinemartin59000.saf.common;

import java.util.HashMap;
import java.util.Map;

public class StatisticalItem {

    private static final StatisticalItem ROOT = new StatisticalItem("ROOT");

    public static StatisticalItem getRoot() {
        return ROOT;
    }

    private Map<String, StatisticalItem> children = new HashMap<>();

    private final String name;
    private long hits = 0;
    private long average = 0;
    private long max = 0;

    public StatisticalItem(String name) {
        this.name = name;
    }

    public void reset() {
        this.hits = 0;
        this.average = 0;
        this.max = 0;
        this.children.values().forEach(StatisticalItem::reset);
    }

    public Map<String, StatisticalItem> getChildren() {
        return children; // FIXME return copy or immutable wrapper
    }

    public StatisticalItem getChild(String name) {
        StatisticalItem child = children.get(name);

        if (child == null) {
            child = new StatisticalItem(name);
            children.put(name, child);
        }

        return child;
    }

    // let's not bother with synchronisation as not sensitive data
    public void addTime(long startMilliseconds) {
        long time = System.currentTimeMillis() - startMilliseconds;
        average = ((average * hits) + time) / (hits + 1);
        hits++;
        if (time > max) {
            max = time;
        }
    }

    public String getName() {
        return name;
    }

    public long getHits() {
        return hits;
    }

    public long getAverage() {
        return average;
    }

    public long getMax() {
        return max;
    }
}
