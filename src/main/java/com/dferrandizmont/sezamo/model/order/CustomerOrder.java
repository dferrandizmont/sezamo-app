package com.dferrandizmont.sezamo.model.order;

import com.dferrandizmont.sezamo.model.orderproduct.OrderProduct;
import com.dferrandizmont.sezamo.model.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "customer_order") // Had to rename it to avoid conflicts with reserved word "order"
public class CustomerOrder implements Serializable {

    private static final long serialVersionUID = -6731942158219678334L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull()
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @NotNull()
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status = OrderStatus.CREATED;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customerOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = {"customerOrder"})
    private List<OrderProduct> orderProducts;
}
