package com.dferrandizmont.sezamo.model.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Min(0)
    private int stock;

    @Min(0)
    private double price;

    public Boolean hasEnoughStock(int quantity) {
        return stock >= quantity;
    }

    public void decreaseStock(int number) {
        stock -= number;
    }
}
