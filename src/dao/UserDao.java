package dao;

import domain.User;

import java.util.List;
import java.util.Map;

/**
 * 用户操作的Dao
 */
public interface UserDao {
    public List<User> findAll();
    public User findUserByUsernameAndPassword(String username, String password);

    void add(User user);

    void delete(int parseInt);

    User findById(int parseInt);

    void upDate(User user);

    int findTotalCount(Map<String, String[]> condition);


    List<User> findByPage(int start, int rows, Map<String, String[]> condition);

}
