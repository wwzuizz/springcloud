
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
 * @date 2019-7-27 19:03:53
 */
@Service
public class StudentInfoService extends BaseQueryService<StudentInfo, StudentInfoCondition, Long> {
    public final static String StudentInfo_TABLE = "student_info";

    @Transactional(rollbackFor = Exception.class)
    public int installStudentInfo(StudentInfo studentInfo) {
        try {
            return install(studentInfo);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public int installStudentInfo(List<StudentInfo> studentInfoList) {
        try {
            return installs(studentInfoList);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateStudentInfo(StudentInfo studentInfo) {
        return updateStudentInfo(studentInfo, null, true);
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateStudentInfo(StudentInfo studentInfo, boolean isNull) {
        try {
            return update(studentInfo, null, isNull);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateStudentInfo(StudentInfo studentInfo, StudentInfoCondition condition, boolean isNull) {
        try {
            return update(studentInfo, condition, isNull);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateStudentInfo(List<StudentInfo> studentInfoList) {
        return updateStudentInfo(studentInfoList, null, true);
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateStudentInfo(List<StudentInfo> studentInfoList, boolean isNull) {
        return updateStudentInfo(studentInfoList, null, isNull);
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateStudentInfo(List<StudentInfo> studentInfoList, StudentInfoCondition condition, boolean isNull) {
        try {
            return updates(studentInfoList, condition, isNull);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deletes(String[] str) throws Exception {
        StudentInfo studentInfo = null;
        for (int i = 0; i < str.length; i++) {
            Long id = Long.valueOf(str[i]);
            studentInfo = getEMStudentInfoOne(id);
            if (studentInfo == null) {
                throw new Exception(id + "找不到");
            }
            try {
                entityManager.createNativeQuery("DELETE FROM `student_info` WHERE (`id`='" + id + "');").executeUpdate();
            } catch (Exception e) {
                throw new Exception("数据库报错");
            }
        }
    }

    public StudentInfo getEMStudentInfoOne(Long id) {
        return getEMOne(id);
    }

    public StudentInfo getEMStudentInfo(StudentInfoCondition studentInfoCondition) {
        return getEMOne(studentInfoCondition);
    }

    public List<StudentInfo> getEMStudentInfoList(StudentInfoCondition studentInfoCondition) {
        return getEmList(studentInfoCondition);
    }

    public Page<StudentInfo> getEMPageStudentInfoList(StudentInfoCondition studentInfoCondition) {
        return getEMPageList(studentInfoCondition);
    }


    public JSONObject getJsonObjectStudentInfoOne(Long id) {
        JSONObject retObject = new JSONObject();
        StudentInfo studentInfo = getEMStudentInfoOne(id);
        if (studentInfo != null) {
            getStudentInfoItem(retObject, studentInfo);
        }
        return retObject;
    }

    public JSONObject getJsonObjectStudentInfo(StudentInfoCondition studentInfoCondition) {
        JSONObject retObject = new JSONObject();
        StudentInfo studentInfo = getEMStudentInfo(studentInfoCondition);
        if (studentInfo != null) {
            getStudentInfoItem(retObject, studentInfo);
        }
        return retObject;
    }

    public JSONArray getStudentInfoJsonArray(StudentInfoCondition studentInfoCondition) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = null;
        List<StudentInfo> studentInfoList = getEMStudentInfoList(studentInfoCondition);
        for (int i = 0; i < studentInfoList.size(); i++) {
            jsonObject = new JSONObject();
            getStudentInfoItem(jsonObject, studentInfoList.get(i));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public JSONObject getStudentInfoPageInfoJsonArray(StudentInfoCondition studentInfoCondition) throws IllegalAccessException {
        Page<StudentInfo> studentInfoPage = getEMPageStudentInfoList(studentInfoCondition);
        JSONObject jsonRetObject = new JSONObject();
        getPageInfo(jsonRetObject, studentInfoPage);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonItem = null;
        if (studentInfoPage.getTotalElements() > 0) {
            List<StudentInfo> studentInfoList = studentInfoPage.getContent();
            for (int i = 0; i < studentInfoList.size(); i++) {
                jsonItem = new JSONObject();
                getStudentInfoItem(jsonItem, studentInfoList.get(i));
                jsonArray.add(jsonItem);
            }
        }
        jsonRetObject.put("list", jsonArray);
        return jsonRetObject;
    }


    public Long countEmStudentInfo(StudentInfoCondition conditon) {
        return countPageList(conditon);
    }

    @Override
    public QueryExe getJoinList(From root, BaseCondition baseCondition) {
        if (baseCondition instanceof StudentInfoCondition) {
            StudentInfoCondition studentInfoCondition = (StudentInfoCondition) baseCondition;
            if (studentInfoCondition != null) {
                return super.getJoinList(root, baseCondition);
            }
        }
        if (baseCondition instanceof StudentCondition) {
            StudentCondition studentCondition = (StudentCondition) baseCondition;
            if (studentCondition != null) {
                Join<StudentInfo, Student> studentJoin = root.join("student");
                QueryExe queryExe = new QueryExe();
                queryExe.setTable(baseCondition.getMysqlTable());
                queryExe.setFrom(studentJoin);
                return queryExe;
            }
        }
        return null;
    }


    @Override
    public Class<StudentInfo> getMysqlBindClass() {
        return StudentInfo.class;
    }

    public static void getStudentInfoItem(JSONObject jsonObject, StudentInfo studentInfo) {
        settingJsonObject(jsonObject, "id", studentInfo.getId());
        settingJsonObject(jsonObject, "info", studentInfo.getInfo());
        settingJsonObject(jsonObject, "studentId", studentInfo.getStudentId());
        JSONArray jsonArray = null;
        JSONObject object = null;
        object = new JSONObject();
        if (studentInfo.getstudent() != null) {
            StudentService.getStudentItem(object, studentInfo.getstudent());
        }
        settingJsonObject(jsonObject, "student", object);

    }

}
