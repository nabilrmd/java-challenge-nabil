package ist.challenge.nabil.repository;

import ist.challenge.nabil.entity.TblUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TblUserRepository extends JpaRepository<TblUser, Long> {

    @Query(value = "SELECT * FROM sample.tbl_user WHERE username = :username", nativeQuery = true)
    TblUser findByUsername(@Param("username") String username);

    @Query(value = "SELECT * FROM sample.tbl_user WHERE username = :username AND password = :password", nativeQuery = true)
    TblUser getLoginUser(@Param("username") String username, @Param("password") String password);



}
