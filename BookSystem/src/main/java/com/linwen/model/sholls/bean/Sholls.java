
package com.linwen.model.sholls.bean;

import com.linwen.comm.base.BaseBean;
import com.linwen.comm.entity.IdGenerator;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author lin
 * @date 2019-7-27 19:03:51
 */
@Entity
@Table(name = "sholls")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Sholls extends BaseBean {

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
    @Column(name = "info", nullable = false)
    private String info;
    @Column(name = "status", nullable = false)
    private Integer status;
    @Column(name = "price", nullable = false)
    private BigDecimal price;


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return this.info;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return this.price;
    }


}

