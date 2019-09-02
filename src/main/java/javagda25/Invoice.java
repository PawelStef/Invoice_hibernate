package javagda25;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Invoice implements IBaseEntity{



    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id;

    @CreationTimestamp
    private LocalDateTime dataDodania;
    @Column (nullable = false)
    private String nazwaKlijenta;
    @Column (nullable = false)
    private Boolean czyOplacony;


    private LocalDateTime dataWydania;
    private LocalDateTime dataIGodzinaOpłacenia;


    @Formula(value = "(SELECT SUM((p.cena + p.podatek)* p.ilosc) from invoicePosition p where p.invoice_id = id)")
    private Double billValue;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "invoice", fetch = FetchType.EAGER)
    private List<InvoicePosition> productList;


}
