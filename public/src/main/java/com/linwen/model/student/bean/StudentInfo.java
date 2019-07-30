
package com.linwen.model.student.bean;

import com.linwen.comm.base.BaseBean;
import com.linwen.comm.entity.IdGenerator;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author lin
 * @date 2019-7-27 19:03:53
 */
@Entity
@Table(name = "student_info")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StudentInfo extends BaseBean {

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

    @Column(name = "info", nullable = true)
    private String info;
    @Column(name = "student_id", nullable = true)
    private Long studentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    private Student student;

    public void setStudent(Student student) {
        this.student = student;
    }

    public Student getstudent() {
        return this.student;
    }


    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return this.info;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getStudentId() {
        return this.studentId;
    }


}

