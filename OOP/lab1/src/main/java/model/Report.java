package model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Report {
    private int clientId;
    private int carId;
    private boolean hasInjuries;
    private String message;
    private Integer cost;
}
