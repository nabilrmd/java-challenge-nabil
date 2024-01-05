package ist.challenge.nabil.entity;



import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tbl_user", schema = "sample")
public class TblUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", unique = true, length = 25, nullable = false)
    private String username;

    @Column(name = "password", nullable = false, length = 25)
    private String password;


}
