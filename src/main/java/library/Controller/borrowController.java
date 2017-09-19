package library.Controller;

import library.models.Book;
import library.models.ReaderBook;
import library.repository.BookRepository;
import library.repository.ReaderBookRepository;
import library.repository.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reader")
public class borrowController {
    @Autowired
    private ReaderBookRepository readerBookRepository;

    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private BookRepository bookRepository;

    @RequestMapping(value = "/{id}/borrowbooks", method = RequestMethod.GET)
    public List<Map> borrowBooks(@PathVariable("id")int id){
        Iterable<ReaderBook> it = readerBookRepository.findAll();
        List<Map> list = new ArrayList<>();
        for(ReaderBook rb:it){
            if(rb.getReader().getId() == id && rb.getReturnTime() == 0){
                Map<String,Object> map = new HashMap<>();
                putBookInfo(map,rb);
                list.add(map);
            }
        }
        return list;
    }

    @RequestMapping(value = "/{id}/borrowedbooks", method = RequestMethod.GET)
    public List<Map> borrowedBooks(@PathVariable("id")int id){
        Iterable<ReaderBook> it = readerBookRepository.findAll();
        List<Map> list = new ArrayList<>();
        for(ReaderBook rb:it){
            if(rb.getReader().getId() == id && rb.getReturnTime() != 0){
                Map<String,Object> map = new HashMap<>();
                putBookInfo(map,rb);
                list.add(map);
            }
        }
        return list;
    }

    private void putBookInfo(Map<String,Object> map, ReaderBook rb){
        map.put("isbn",rb.getBook().getIsbn());
        map.put("name",rb.getBook().getName());
        map.put("author",rb.getBook().getAuthor());
        map.put("publisher",rb.getBook().getPublisher());
        map.put("publishTime",rb.getBook().getPublishTime());
        map.put("state",rb.getBook().getState());
        map.put("position",rb.getBook().getPosition());
        map.put("remainingNum",rb.getBook().getRemainingNum());
        map.put("borrowTime",rb.getBorrowTime());
        map.put("returnTime",rb.getReturnTime());
        map.put("penalty",rb.getPenalty());
    }
}
