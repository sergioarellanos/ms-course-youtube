package academy.digitallab.store.shopping.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@Entity
@Data
@Table(name = "tbl_invoce_items")
public class InvoiceItem  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoice_id;
    @Column(name = "product_id")
    private Long productId;

    @Positive(message = "El stock debe ser mayor que cero")
    private Double quantity;
    private Double  price;




}
