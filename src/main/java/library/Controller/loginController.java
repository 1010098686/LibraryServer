package library.Controller;


import library.models.Administrator;
import library.models.Reader;
import library.repository.AdminRepository;
import library.repository.ReaderRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class loginController {

    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private AdminRepository adminRepository;

    @RequestMapping(value = "/admin",method = RequestMethod.POST)
    public ResponseEntity<Administrator> loginAsAdmin(@RequestBody Map<String,String> body){
        int queryId = Integer.parseInt(body.get("id"));
        Iterable<Administrator> it = adminRepository.findAll();
        for(Administrator admin:it){
            if(admin.getId() == queryId && admin.getPassword().equals(body.get("password"))){
                return new ResponseEntity<>(admin, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/reader", method = RequestMethod.POST)
    public ResponseEntity<Map> loginAsReader(@RequestBody Map<String,String> body){
        int queryId = Integer.parseInt(body.get("id"));
        Iterable<Reader> it = readerRepository.findAll();
        for(Reader reader:it){
            if(reader.getId() == queryId && reader.getPassword().equals(body.get("password"))){
                return new ResponseEntity<>(convertReaderToMap(reader), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    private Map<String,Object> convertReaderToMap(Reader reader){
        Map<String,Object> map = new HashMap<>();
        map.put("id",reader.getId());
        map.put("name",reader.getName());
        map.put("password",reader.getPassword());
        map.put("birthday",reader.getBirthday());
        map.put("sex",reader.getSex());
        map.put("department",reader.getDepartment());
        return map;
    }
}
