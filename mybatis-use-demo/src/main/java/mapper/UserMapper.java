package mapper;

import pojo.User;

public interface UserMapper {

    User findUserByName(String name);
}
