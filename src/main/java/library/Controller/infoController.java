package library.Controller;

import library.models.Administrator;
import library.models.Book;
import library.models.Reader;
import library.repository.AdminRepository;
import library.repository.BookRepository;
import library.repository.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/info")
public class infoController {

    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AdminRepository adminRepository;

    @RequestMapping(value = "/reader/changepassword/{id}", method = RequestMethod.POST)
    public ResponseEntity<Integer> changeReaderPassword(@RequestBody Map<String,String> body, @PathVariable("id")int id){
        Iterable<Reader> it = readerRepository.findAll();
        Reader reader = null;
        for(Reader r:it){
            if(r.getId() == id){
                reader = r;
                break;
            }
        }
        if(!reader.getPassword().equals(body.get("oldpassword"))){
            return new ResponseEntity<Integer>(401,HttpStatus.UNAUTHORIZED);
        }
        //readerRepository.delete(reader);
        reader.setPassword(body.get("password"));
        readerRepository.save(reader);
        return new ResponseEntity<Integer>(401,HttpStatus.OK);
    }

    @RequestMapping(value = "/reader/changeinfo/{id}", method = RequestMethod.POST)
    public ResponseEntity<Integer> changeInfo(@RequestBody Reader reader, @PathVariable("id")int id){
        Iterable<Reader> it = readerRepository.findAll();
        Reader r = null;
        for(Reader i:it){
            if(i.getId() == id){
                r = i;
                break;
            }
        }
        //readerRepository.delete(r);
        reader.setId(id);
        readerRepository.save(reader);
        return new ResponseEntity<Integer>(401,HttpStatus.OK);
    }

    @RequestMapping(value = "/admin/changepassword/{id}", method = RequestMethod.POST)
    public ResponseEntity<Integer> changeAdminPassword(@RequestBody Map<String, String> body, @PathVariable("id")int id){
        Iterable<Administrator> it = adminRepository.findAll();
        Administrator admin = null;
        for(Administrator ad:it){
            if(ad.getId() == id){
                admin = ad;
                break;
            }
        }
        if(!admin.getPassword().equals(body.get("oldpassword"))){
            return new ResponseEntity<Integer>(401,HttpStatus.UNAUTHORIZED);
        }
        //adminRepository.delete(admin);
        admin.setPassword(body.get("password"));
        adminRepository.save(admin);
        return new ResponseEntity<Integer>(401,HttpStatus.OK);
    }

    @RequestMapping(value = "/admin/changeinfo/{id}", method = RequestMethod.POST)
    public ResponseEntity<Integer> changeInfo(@RequestBody Administrator administrator, @PathVariable("id")int id){
        Iterable<Administrator> it = adminRepository.findAll();
        Administrator a = null;
        for(Administrator i:it){
            if(i.getId() == id){
                a = i;
                break;
            }
        }
        //adminRepository.delete(a);
        administrator.setId(id);
        adminRepository.save(administrator);
        return new ResponseEntity<Integer>(401,HttpStatus.OK);
    }

    @RequestMapping(value = "/query/reader/{id}", method = RequestMethod.GET)
    public ResponseEntity<Reader> queryReaderInfo(@PathVariable("id")int id){
        Iterable<Reader> it = readerRepository.findAll();
        for(Reader reader : it){
            if(reader.getId() == id){
                return new ResponseEntity<Reader>(reader, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/query/book/{isbn}", method = RequestMethod.GET)
    public ResponseEntity<Map<String,Object>> queryAdminInfo(@PathVariable("isbn")int isbn){
        Iterable<Book> it = bookRepository.findAll();
        for(Book book: it){
            if(book.getIsbn() == isbn){
                return new ResponseEntity<>(bookController.convertBookToMap(book), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
