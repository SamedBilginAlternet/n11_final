package payment.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "currencies")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    public Currency() {}

    public Currency(String code) {
        this.code = code;
    }

    public Long getId() { return id; }
    public String getCode() { return code; }
}
