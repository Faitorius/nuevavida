package nl.elsci.nuevavida;

import lombok.Data;

import java.util.List;

@Data
public class Action {
    private String name;
    private String desc;
    private String text;
    private List<String> next;
}
