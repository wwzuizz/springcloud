

package com.linwen.controller.admin.service;

import com.linwen.comm.base.BaseService;
import com.linwen.model.student.bean.Student;
import com.linwen.model.student.bean.StudentInfo;
import com.linwen.model.student.condition.StudentCondition;
import com.linwen.model.student.condition.StudentInfoCondition;
import com.linwen.service.student.StudentInfoService;
import com.linwen.service.student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by linwen on 19-7-6.
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class StudentAndService extends BaseService {
    @Autowired
    StudentInfoService studentInfoService;
    @Autowired
    StudentService studentService;

    public StudentInfo addStudentInfo(StudentInfoCondition studentInfoCondition) {
        StudentInfo studentInfo = studentInfoCondition.getBeseBean();
        if (studentInfo.getId() != null) {
            StudentInfo studentInfoOld = studentInfoService.getEMStudentInfoOne(studentInfo.getId());
            studentInfoService.updateStudentInfo(studentInfo);
        } else {
            studentInfoService.installStudentInfo(studentInfo);
        }
        return studentInfo;
    }

    public Student addStudent(StudentCondition studentCondition) {
        Student student = studentCondition.getBeseBean();
        if (student.getId() != null) {
            Student studentOld = studentService.getEMStudentOne(student.getId());
            studentService.updateStudent(student);
        } else {
            studentService.installStudent(student);
        }
        return student;
    }

    public void addStudentInfos(StudentCondition studentCondition, List<StudentInfoCondition> studentInfoConditions) {
        List<StudentInfo> studentInfoList = new ArrayList<>();
        for (int i = 0; i < studentInfoConditions.size(); i++) {
            studentInfoConditions.get(i).setStudentId(studentCondition.getId());

            studentInfoList.add(studentInfoConditions.get(i).getBeseBean());
        }
        studentInfoService.installStudentInfo(studentInfoList);
    }

    public void addStudentAndStudentInfo(StudentCondition vo, StudentInfoCondition studentInfoCondition) {
        Student student = addStudent(vo);
        studentInfoCondition.setStudentId(student.getId());
        addStudentInfo(studentInfoCondition);
    }

    public void addStudentInfo(StudentInfoCondition studentInfoCondition, String studentIds) {
        StudentInfo studentInfo = null;
        String[] ids = studentIds.split(",");
        List<StudentInfo> studentInfos = new ArrayList<>();
        Long id = 0l;
        for (int i = 0; i < ids.length; i++) {
            id = Long.valueOf(ids[i]);
            studentInfo = studentInfoCondition.getBeseBean();
            studentInfo.setStudentId(id);
            studentInfos.add(studentInfo);
        }
        studentInfoService.installStudentInfo(studentInfos);
    }


    public StudentService getStudentService() {
        return this.studentService;
    }

    public StudentInfoService getStudentInfoService() {
        return this.studentInfoService;
    }


}
