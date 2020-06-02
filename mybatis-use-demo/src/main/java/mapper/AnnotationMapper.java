package mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.SelectKey;
import pojo.User;


public interface AnnotationMapper {

    @Insert("insert into user(username, age, birthday, address) values(#{userName}, #{age}, #{birthday}, #{address});")
    @SelectKey(statement = "select last_insert_id()", keyProperty = "id", resultType = int.class, before = false)
    int insert(User user);
}
