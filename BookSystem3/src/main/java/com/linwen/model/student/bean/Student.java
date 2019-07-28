
package com.linwen.model.student.bean;

import com.linwen.comm.base.BaseBean;
import com.linwen.comm.entity.IdGenerator;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lin
 * @date 2019-7-27 19:03:52
 */
@Entity
@Table(name = "student")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Student extends BaseBean {

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
    @Column(name = "info_id", nullable = true)
    private Long infoId;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student")
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<StudentInfo> studentInfoList = new ArrayList<>();

    public void setStudentInfoList(List<StudentInfo> studentInfoList) {
        this.studentInfoList = studentInfoList;
    }

    public List<StudentInfo> getStudentInfoList() {
        return this.studentInfoList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setInfoId(Long infoId) {
        this.infoId = infoId;
    }

    public Long getInfoId() {
        return this.infoId;
    }


}

