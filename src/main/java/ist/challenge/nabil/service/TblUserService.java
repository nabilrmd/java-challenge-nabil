package ist.challenge.nabil.service;


import com.google.gson.*;
import ist.challenge.nabil.entity.TblUser;
import ist.challenge.nabil.pojo.EditPojo;
import ist.challenge.nabil.pojo.PojoRespon;
import ist.challenge.nabil.pojo.UserPojo;
import ist.challenge.nabil.repository.TblUserRepository;
import ist.challenge.nabil.utility.MessageModel;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import javax.xml.ws.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TblUserService {

    @Autowired
    private TblUserRepository tblUserRepository;

    public ResponseEntity<MessageModel> registerService(UserPojo pojo) {
        MessageModel msg = new MessageModel();
        try {
            TblUser tblUser = new TblUser();
            tblUser.setUsername(pojo.getUsername());
            tblUser.setPassword(pojo.getPassword());

            TblUser unameCheck = tblUserRepository.findByUsername(pojo.getUsername());
            if (unameCheck != null) {
                msg.setMessage("Username Sudah Digunakan");
                msg.setStatus(false);
                msg.setData(null);
                return ResponseEntity.status(409).body(msg);
            }

            tblUserRepository.save(tblUser);
            msg.setStatus(true);
            msg.setData(tblUser);
            return ResponseEntity.ok(msg);
        } catch (Exception e) {
            msg.setStatus(false);
            msg.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(msg);
        }
    }

    public ResponseEntity<MessageModel> loginService(UserPojo pojo) {
        MessageModel msg = new MessageModel();
        try {
            String username = pojo.getUsername();
            String password = pojo.getPassword();
            if (username == null || password == null) {
                msg.setMessage("Username dan / atau Password Kosong");
                msg.setStatus(false);
                msg.setData(null);
                return ResponseEntity.status(400).body(msg);
            }
            TblUser tblUser = tblUserRepository.getLoginUser(pojo.getUsername(), pojo.getPassword());
            if (tblUser == null) {
                msg.setMessage("Username atau Password Tidak Cocok");
                msg.setStatus(false);
                msg.setData(null);
                return ResponseEntity.status(401).body(msg);
            }
            msg.setMessage("Sukses Login");
            msg.setStatus(true);
            msg.setData(tblUser);
            return ResponseEntity.status(201).body(msg);

        } catch (Exception e) {
            msg.setStatus(false);
            msg.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(msg);
        }

    }

    public ResponseEntity<MessageModel> getAllUser() {
        MessageModel msg = new MessageModel();
        try {
            List<TblUser> data = tblUserRepository.findAll();
            if (data == null) {
                msg.setMessage("Data Tidak Ditemukan");
                msg.setStatus(false);
                msg.setData(null);
                return ResponseEntity.status(404).body(msg);
            }
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            String apiUrl = "https://swapi.py4e.com/api/people/?search=&page=1";

            headers.set("Content-Type", "application/json");
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity, String.class);
            String responseData = response.getBody();

            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(responseData).getAsJsonObject();

            JsonArray resultsArray = jsonObject.getAsJsonArray("results");

            int i = 0;
            Map<String, Object> finalResult = new HashMap<>();
            for (TblUser tblUser : data) {
                if (i <resultsArray.size()) {
                    JsonElement swData = resultsArray.get(i);
                    String characterName = swData.getAsJsonObject().get("name").getAsString();
                    finalResult.put("id", tblUser.getId());
                    finalResult.put("Username", tblUser.getUsername());
                    finalResult.put("Password", tblUser.getPassword());
                    finalResult.put("Name", characterName);

                    System.out.println(finalResult);
                }

            }


            msg.setMessage("Sukses");
            msg.setStatus(true);
            msg.setData(finalResult);
            return ResponseEntity.ok(msg);
        } catch (Exception e) {
            msg.setStatus(false);
            msg.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(msg);
        }
    }

    @Transactional
    public ResponseEntity<MessageModel> editUser(EditPojo pojo) {
        MessageModel msg = new MessageModel();
        try {
            String username = pojo.getUsername();
            String password = pojo.getPassword();

            PojoRespon pojoRespon = new PojoRespon();
            TblUser data = tblUserRepository.getById(pojo.getId());

            if (data.getUsername().matches(username)) {
                msg.setMessage("Username Sudah Digunakan");
                msg.setStatus(false);
                msg.setData(null);
                return ResponseEntity.status(409).body(msg);
            }
            if (data.getPassword().matches(password)) {
                msg.setMessage("Password Tidak Boleh Sama Dengan Password Sebelumnya");
                msg.setStatus(false);
                msg.setData(null);
                return ResponseEntity.status(400).body(msg);
            }
            if (!data.getPassword().matches(password) && !data.getUsername().matches(username)) {
                TblUser edit = new TblUser();
                edit.setUsername(username);
                edit.setPassword(password);
                edit.setId(pojo.getId());
                tblUserRepository.save(edit);
            }


            msg.setMessage("Success");
            msg.setStatus(true);
            return ResponseEntity.status(200).body(msg);

        } catch (Exception e) {
            e.printStackTrace();
            msg.setStatus(false);
            msg.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(msg);
        }
    }

}
