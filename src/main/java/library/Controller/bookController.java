package library.Controller;

import library.models.Book;
import library.models.Reader;
import library.models.ReaderBook;
import library.repository.BookRepository;
import library.repository.ReaderBookRepository;
import library.repository.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.awt.image.IntegerComponentRaster;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/book")
public class bookController {

    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private ReaderBookRepository readerBookRepository;

    @Autowired
    private BookRepository bookRepository;

    public static Map convertBookToMap(Book book){
        Map<String,Object> map = new HashMap<>();
        map.put("isbn",book.getIsbn());
        map.put("name",book.getName());
        map.put("author",book.getAuthor());
        map.put("publisher",book.getPublisher());
        map.put("publishTime",book.getPublishTime());
        map.put("state",book.getState());
        map.put("position",book.getPosition());
        map.put("remainingNum",book.getRemainingNum());
        map.put("borrowTime",0);
        map.put("returnTime",0);
        map.put("penalty",0);
        return map;
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public List<Map> queryBook(@RequestParam(value = "queryString")String queryString){
        List<Map> ret = new ArrayList<>();
        Iterable<Book> it = bookRepository.findAll();
        int isbn;
        try{
            isbn = Integer.parseInt(queryString);
        }catch (Exception e){
            isbn = -1;
        }
        for(Book book:it){
            if(isbn != -1 && book.getIsbn() == isbn){
                ret.add(convertBookToMap(book));
            }else if(book.getName().contains(queryString) || book.getAuthor().contains(queryString) || book.getPublisher().contains(queryString)){
                ret.add(convertBookToMap(book));
            }
        }
        return ret;
    }

    @RequestMapping(value = "/borrow", method = RequestMethod.GET)
    public ResponseEntity<Integer> borrow(@RequestParam(value = "id")String id, @RequestParam(value = "isbn")String isbn){
        Iterable<Reader> readerIt = readerRepository.findAll();
        Reader reader = null;
        for(Reader r:readerIt){
            if(r.getId() == Integer.parseInt(id)){
                reader = r;
                break;
            }
        }
        Iterable<Book> bookIt = bookRepository.findAll();
        Book book = null;
        for(Book b:bookIt){
            if(b.getIsbn() == Integer.parseInt(isbn)){
                book = b;
                break;
            }
        }
        Iterable<ReaderBook> it = readerBookRepository.findAll();
        List<ReaderBook> list = new ArrayList<>();
        for(ReaderBook i:it){
            if(i.getReader().getId() == Integer.parseInt(id) && i.getReturnTime() == 0){
                list.add(i);
            }
        }
        ReaderBook rb = new ReaderBook();
        rb.setReader(reader);
        rb.setBook(book);
        rb.setBorrowTime(System.currentTimeMillis());
        rb.setReturnTime(0);
        rb.setPenalty(0);
        if(list.contains(rb) || book.getRemainingNum() <= 0){
            return new ResponseEntity<Integer>(401,HttpStatus.UNAUTHORIZED);
        }
        else{
            book.setRemainingNum(book.getRemainingNum() - 1);
            readerBookRepository.save(rb);
            return new ResponseEntity<Integer>(401,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/return", method = RequestMethod.GET)
    public ResponseEntity<Double> returnBook(@RequestParam(value = "id")String id, @RequestParam(value = "isbn")String isbn){
        Iterable<ReaderBook> it = readerBookRepository.findAll();
        ReaderBook rb = null;
        for(ReaderBook i:it){
            if(i.getReader().getId() == Integer.parseInt(id) && i.getBook().getIsbn() == Integer.parseInt(isbn) && i.getReturnTime() == 0){
                rb = i;
                break;
            }
        }
        long date = System.currentTimeMillis();
        long borrowTime = rb.getBorrowTime();
        rb.setReturnTime(date);
        Book book = rb.getBook();
        book.setRemainingNum(book.getRemainingNum() + 1);
        long delta = date - borrowTime;
        long twoM = 2 * 30 * 24 * 3600 * 1000;
        if(delta <= twoM){
            rb.setPenalty(0);
            readerBookRepository.save(rb);
            return new ResponseEntity<>(0.0, HttpStatus.OK);
        }
        else{
            delta -= twoM;
            delta /= (24 * 3600 * 1000);
            double penalty = delta * 0.1;
            rb.setPenalty(penalty);
            readerBookRepository.save(rb);
            return new ResponseEntity<>(penalty, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/reborrow", method = RequestMethod.GET)
    public ResponseEntity<Integer> reborrow(@RequestParam(value = "id")String id, @RequestParam(value = "isbn")String isbn){
        Iterable<ReaderBook> it = readerBookRepository.findAll();
        ReaderBook rb = null;
        for(ReaderBook i:it){
            if(i.getReader().getId() == Integer.parseInt(id) && i.getBook().getIsbn() == Integer.parseInt(isbn)){
                rb = i;
                break;
            }
        }
        long date = System.currentTimeMillis();
        long borrowTime = rb.getBorrowTime();
        long delta = date - borrowTime;
        long twoM = 2 * 30 * 24 * 3600 * 1000;
        if(delta > twoM){
            delta -= twoM;
            delta /= (24 * 3600 * 1000);
            rb.setPenalty(delta * 0.1);
            rb.setReturnTime(date);
            readerBookRepository.save(rb);
            return new ResponseEntity<Integer>(401,HttpStatus.UNAUTHORIZED);
        }else{
            rb.setBorrowTime(date);
            rb.setReturnTime(0);
            readerBookRepository.save(rb);
            return new ResponseEntity<Integer>(401,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/modify/{isbn}", method = RequestMethod.PUT)
    public ResponseEntity<Integer> modify(@RequestBody Book book,@PathVariable("isbn") String isbn){
        book.setIsbn(Integer.parseInt(isbn));
        bookRepository.save(book);
        return new ResponseEntity<>(401,HttpStatus.OK);
    }


    @RequestMapping(value = "/delete/{isbn}", method = RequestMethod.DELETE)
    public ResponseEntity<Integer> delete(@PathVariable("isbn")String isbn){
        Iterable<Book> it = bookRepository.findAll();
        Book book = null;
        for(Book b:it){
            if(b.getIsbn() == Integer.parseInt(isbn)){
                book = b;
                break;
            }
        }
	if(book == null){
		return new ResponseEntity<Integer>(401,HttpStatus.UNAUTHORIZED);
	}
        bookRepository.delete(book);
        return new ResponseEntity<Integer>(401,HttpStatus.OK);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<Integer> add(@RequestBody Book book){
        Iterable<Book> it = bookRepository.findAll();
        for(Book b:it){
            if(b.getIsbn() == book.getIsbn()){
                return new ResponseEntity<Integer>(401,HttpStatus.UNAUTHORIZED);
            }
        }
        bookRepository.save(book);
        return new ResponseEntity<Integer>(401,HttpStatus.OK);
    }
}
