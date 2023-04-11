package model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Car {
    private int id;
    private String name;
    private Boolean availabel;
    private Integer costPerDay;
}
