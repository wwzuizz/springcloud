
package com.linwen.service.student;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linwen.comm.base.BaseCondition;
import com.linwen.comm.base.BaseQueryService;
import com.linwen.comm.base.QueryExe;
import com.linwen.model.student.bean.Student;
import com.linwen.model.student.bean.StudentInfo;
import com.linwen.model.student.condition.StudentCondition;
import com.linwen.model.student.condition.StudentInfoCondition;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import java.util.List;

/**
 * @author lin
 * @date 2019-7-27 19:03:52
 */
@Service
public class StudentService extends BaseQueryService<Student, StudentCondition, Long> {
    public final static String Student_TABLE = "student";

    @Transactional(rollbackFor = Exception.class)
    public int installStudent(Student student) {
        try {
            return install(student);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public int installStudent(List<Student> studentList) {
        try {
            return installs(studentList);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateStudent(Student student) {
        return updateStudent(student, null, true);
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateStudent(Student student, boolean isNull) {
        try {
            return update(student, null, isNull);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateStudent(Student student, StudentCondition condition, boolean isNull) {
        try {
            return update(student, condition, isNull);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateStudent(List<Student> studentList) {
        return updateStudent(studentList, null, true);
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateStudent(List<Student> studentList, boolean isNull) {
        return updateStudent(studentList, null, isNull);
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateStudent(List<Student> studentList, StudentCondition condition, boolean isNull) {
        try {
            return updates(studentList, condition, isNull);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deletes(String[] str) throws Exception {
        Student student = null;
        for (int i = 0; i < str.length; i++) {
            Long id = Long.valueOf(str[i]);
            student = getEMStudentOne(id);
            if (student == null) {
                throw new Exception(id + "找不到");
            }
            try {
                entityManager.createNativeQuery("DELETE FROM `student_info` WHERE (`student_id`='" + id + "');").executeUpdate();
                entityManager.createNativeQuery("DELETE FROM `student` WHERE (`id`='" + id + "');").executeUpdate();
            } catch (Exception e) {
                throw new Exception("数据库报错");
            }
        }
    }

    public Student getEMStudentOne(Long id) {
        return getEMOne(id);
    }

    public Student getEMStudent(StudentCondition studentCondition) {
        return getEMOne(studentCondition);
    }

    public List<Student> getEMStudentList(StudentCondition studentCondition) {
        return getEmList(studentCondition);
    }

    public Page<Student> getEMPageStudentList(StudentCondition studentCondition) {
        return getEMPageList(studentCondition);
    }


    public JSONObject getJsonObjectStudentOne(Long id) {
        JSONObject retObject = new JSONObject();
        Student student = getEMStudentOne(id);
        if (student != null) {
            getStudentItem(retObject, student);
        }
        return retObject;
    }

    public JSONObject getJsonObjectStudent(StudentCondition studentCondition) {
        JSONObject retObject = new JSONObject();
        Student student = getEMStudent(studentCondition);
        if (student != null) {
            getStudentItem(retObject, student);
        }
        return retObject;
    }

    public JSONArray getStudentJsonArray(StudentCondition studentCondition) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = null;
        List<Student> studentList = getEMStudentList(studentCondition);
        for (int i = 0; i < studentList.size(); i++) {
            jsonObject = new JSONObject();
            getStudentItem(jsonObject, studentList.get(i));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public JSONObject getStudentPageInfoJsonArray(StudentCondition studentCondition) throws IllegalAccessException {
        Page<Student> studentPage = getEMPageStudentList(studentCondition);
        JSONObject jsonRetObject = new JSONObject();
        getPageInfo(jsonRetObject, studentPage);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonItem = null;
        if (studentPage.getTotalElements() > 0) {
            List<Student> studentList = studentPage.getContent();
            for (int i = 0; i < studentList.size(); i++) {
                jsonItem = new JSONObject();
                getStudentItem(jsonItem, studentList.get(i));
                jsonArray.add(jsonItem);
            }
        }
        jsonRetObject.put("list", jsonArray);
        return jsonRetObject;
    }


    public Long countEmStudent(StudentCondition conditon) {
        return countPageList(conditon);
    }

    @Override
    public QueryExe getJoinList(From root, BaseCondition baseCondition) {
        if (baseCondition instanceof StudentCondition) {
            StudentCondition studentCondition = (StudentCondition) baseCondition;
            if (studentCondition != null) {
                return super.getJoinList(root, baseCondition);
            }
        }
        if (baseCondition instanceof StudentInfoCondition) {
            StudentInfoCondition studentInfoCondition = (StudentInfoCondition) baseCondition;
            if (studentInfoCondition != null) {
                Join<Student, StudentInfo> studentInfoJoin = root.joinList("studentInfoList");
                QueryExe queryExe = new QueryExe();
                queryExe.setTable(baseCondition.getMysqlTable());
                queryExe.setFrom(studentInfoJoin);
                return queryExe;
            }
        }
        return null;
    }


    @Override
    public Class<Student> getMysqlBindClass() {
        return Student.class;
    }

    public static void getStudentItem(JSONObject jsonObject, Student student) {
        settingJsonObject(jsonObject, "id", student.getId());
        settingJsonObject(jsonObject, "name", student.getName());
        settingJsonObject(jsonObject, "infoId", student.getInfoId());
        JSONArray jsonArray = null;
        JSONObject object = null;
        jsonArray = new JSONArray();
        if (student.getStudentInfoList() != null) {
            for (int i = 0; i < student.getStudentInfoList().size(); i++) {
                object = new JSONObject();
                StudentInfoService.getStudentInfoItem(object, student.getStudentInfoList().get(i));
                jsonArray.add(object);
            }
        }
        settingJsonObject(jsonObject, "studentInfos", jsonArray);
    }

}
