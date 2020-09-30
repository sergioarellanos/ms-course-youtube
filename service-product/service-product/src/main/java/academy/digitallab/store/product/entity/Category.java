package academy.digitallab.store.product.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tbl_categories")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
