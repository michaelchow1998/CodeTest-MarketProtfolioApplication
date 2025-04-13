package com.michael.marketprotfoliosystem.model;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "security_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Security {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String ticker;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SecurityType type;

    public Security() {}

    public Security(String ticker, SecurityType type) {
        this.ticker = ticker;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public String getTicker() {
        return ticker;
    }

    public SecurityType getType() {
        return type;
    }
}
