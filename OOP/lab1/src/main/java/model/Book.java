package model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Book {
    private int clientId;
    private int carId;
    private int duration;
    private int cost;
    private boolean allow;
    private boolean paid;
    private String cause;
}
