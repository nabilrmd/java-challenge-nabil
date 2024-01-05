package ist.challenge.nabil.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditPojo {

    private Long id;
    private String username;
    private String password;
}
