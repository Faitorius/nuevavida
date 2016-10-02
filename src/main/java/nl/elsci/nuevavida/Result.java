package nl.elsci.nuevavida;

import lombok.Getter;

import java.util.Collection;
import java.util.HashSet;

public class Result {

    @Getter
    private final Collection<String> flags = new HashSet<>();

    public boolean hasFlag(String flag) {
        return flags.contains(flag);
    }

    public void addFlag(String flag) {
        flags.add(flag);
    }

    public void add(Result result) {

    }

    public void append(String s) {

    }
}
