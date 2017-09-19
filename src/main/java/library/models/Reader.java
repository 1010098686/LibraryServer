package library.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Reader implements Serializable{

    @Id
    private Integer id;

    @NotNull
    private String name;

    @NotNull
    private String password;

    private long birthday;

    @NotNull
    private String sex;

    @NotNull
    private String department;

    @OneToMany(mappedBy = "reader", cascade = CascadeType.ALL)
    private List<ReaderBook> borrowedBooks = new ArrayList<>();

    public Reader(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<ReaderBook> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(List<ReaderBook> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reader reader = (Reader) o;

        return id.equals(reader.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
