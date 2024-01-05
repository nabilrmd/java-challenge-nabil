package ist.challenge.nabil.controller;


import ist.challenge.nabil.entity.TblUser;
import ist.challenge.nabil.pojo.EditPojo;
import ist.challenge.nabil.pojo.UserPojo;
import ist.challenge.nabil.service.TblUserService;
import ist.challenge.nabil.utility.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private TblUserService userService;

    @PostMapping("/register")
    public ResponseEntity<MessageModel> register(UserPojo pojo){
        return userService.registerService(pojo);
    }

    @PostMapping("/login")
    public ResponseEntity<MessageModel> login(UserPojo pojo){
        return userService.loginService(pojo);
    }

    @PostMapping("/edit")
    public ResponseEntity<MessageModel> edit(EditPojo pojo){
        return userService.editUser(pojo);
    }

    @GetMapping("/gettAll")
    public ResponseEntity<MessageModel> getAll(){
        return userService.getAllUser();
    }

}
