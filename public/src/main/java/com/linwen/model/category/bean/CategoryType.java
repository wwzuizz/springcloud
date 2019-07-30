
package com.linwen.model.category.bean;

import com.linwen.comm.base.BaseBean;
import com.linwen.comm.entity.IdGenerator;
import com.linwen.model.book.bean.Book;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lin
 * @date 2019-7-27 19:03:49
 */
@Entity
@Table(name = "category_type")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CategoryType extends BaseBean {

    @Id
    @GeneratedValue(generator = IdGenerator.IDGENERATOR)
    @GenericGenerator(name = IdGenerator.IDGENERATOR, strategy = IdGenerator.IDGENERATORCLASS)
    @Column(name = "id")
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    @Column(name = "name", nullable = true)
    private String name;


    @OneToMany(fetch = FetchType.LAZY)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(
            name = "bc_mapped",
            joinColumns = {@JoinColumn(name = "class_id", referencedColumnName = "id", table = "category_type", insertable = false, updatable = false)}
            ,
            inverseJoinColumns = {@JoinColumn(name = "book_id", referencedColumnName = "id", table = "book", insertable = false, updatable = false)}


    )
    private List<Book> bookList = new ArrayList<>();

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public List<Book> getBookList() {
        return this.bookList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }


}

