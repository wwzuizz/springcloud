
package com.linwen.model.book.bean;

import com.linwen.comm.base.BaseBean;
import com.linwen.comm.entity.IdGenerator;
import com.linwen.model.category.bean.CategoryType;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lin
 * @date 2019-7-27 19:03:47
 */
@Entity
@Table(name = "book")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Book extends BaseBean {

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

    @Column(name = "name", nullable = false)
    private String name;


    @OneToMany(fetch = FetchType.LAZY)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(
            name = "bc_mapped",
            inverseJoinColumns = {@JoinColumn(name = "class_id", referencedColumnName = "id", table = "category_type", insertable = false, updatable = false)}
            ,
            joinColumns = {@JoinColumn(name = "book_id", referencedColumnName = "id", table = "book", insertable = false, updatable = false)}


    )
    private List<CategoryType> categoryTypeList = new ArrayList<>();

    public void setCategoryTypeList(List<CategoryType> categoryTypeList) {
        this.categoryTypeList = categoryTypeList;
    }

    public List<CategoryType> getCategoryTypeList() {
        return this.categoryTypeList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }


}

