package dao.impl;

import dao.UserDao;
import domain.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import util.JDBCUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserDaoImpl implements UserDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());


    @Override
    public List<User> findAll() {
        //使用JDBC操作数据库
        String sql = "select * from user";
        List<User> users = template.query(sql, new BeanPropertyRowMapper<User>(User.class));

        return users;
    }

    @Override
    public User findUserByUsernameAndPassword(String username, String password) {
        String sql = "select * from user where username=? and password =?";
        User user = null;
        try {
            user = template.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),username,password);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    @Override
    public void add(User user) {
        String sql = "insert into user values(null,?,?,?,?,?,?,null,null)";
        template.update(sql,user.getName(),user.getGender(),user.getAge(),user.getAddress(),user.getQq(),user.getEmail());

    }

    @Override
    public void delete(int id) {
        String sql = "delete from user where id = ?";
        template.update(sql,id);
    }

    @Override
    public User findById(int id) {
        String sql = "select * from user where id = ?";
        User user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), id);
        return user;
    }

    @Override
    public void upDate(User user) {
        String sql = "update user set name=?,gender=?,age=?,address=?,qq=?,email=? where id = ?";
        template.update(sql,user.getName(),user.getGender(),user.getAge(),user.getAddress(),user.getQq(),user.getEmail(),user.getId());

    }

    @Override
    public int findTotalCount(Map<String, String[]> condition) {
        String sql = "select count(*) from user where 1 = 1";

        StringBuilder sb = new StringBuilder(sql);
        //遍历map

        Set<String> keySet = condition.keySet();
        //定义一个参数集合
        List<Object> params = new ArrayList<>();

        for (String key : keySet) {
            //排除分页参数
            if("currentPage".equals(key)||"rows".equals(key)){
                continue;
            }

            //获取value
            String value = condition.get(key)[0];
            //判断value是否有值
            if(value!=null &&!"".equals(value)){
                sb.append("  and  "+key+"  like  ?  ");
                params.add("%"+value+"%");//条件的值
            }
        }

//        System.out.println(sb.toString());
//        System.out.println(params);
        Integer integer = template.queryForObject(sb.toString(), Integer.class, params.toArray());
        return integer;
    }



    @Override
    public List<User> findByPage(int start, int rows, Map<String, String[]> condition) {
        String sql = "select * from user where 1 = 1 ";

        StringBuilder sb = new StringBuilder(sql);
        //遍历map

        Set<String> keySet = condition.keySet();
        //定义一个参数集合
        List<Object> params = new ArrayList<>();

        for (String key : keySet) {
            //排除分页参数
            if("currentPage".equals(key)||"rows".equals(key)){
                continue;
            }

            //获取value
            String value = condition.get(key)[0];
            //判断value是否有值
            if(value!=null &&!"".equals(value)){
                sb.append("  and  "+key+"  like  ?  ");
                params.add("%"+value+"%");//条件的值
            }
        }
        //添加分页
        sb.append(" limit ?,?");
        //添加分页查询参数值
        params.add(start);
        params.add(rows);

        sql=sb.toString();

        System.out.println(sql);
        System.out.println(params);

        List<User> list = template.query(sql, new BeanPropertyRowMapper<User>(User.class), params.toArray());
        return list;
    }
}
