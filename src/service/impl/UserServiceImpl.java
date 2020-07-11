package service.impl;

import dao.UserDao;
import dao.impl.UserDaoImpl;
import domain.PageBean;
import domain.User;
import service.UserService;

import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

    private UserDao dao = new UserDaoImpl();

    @Override
    public List<User> findAll() {
        //调用Dao完成查询
        return dao.findAll();
    }

    @Override
    public User login(User user) {
        return dao.findUserByUsernameAndPassword(user.getUsername(),user.getPassword());
    }
    @Override
    public void addUser(User user){
        dao.add(user);
    }

    @Override
    public void deleteUser(String id) {
        dao.delete(Integer.parseInt(id));
    }

    @Override
    public User findUserById(String id) {
        return dao.findById(Integer.parseInt(id));
    }

    @Override
    public void updateUser(User user) {
        dao.upDate(user);
    }

    @Override
    public void delSelectedUser(String[] ids) {
        if(ids!=null&& ids.length>0) {
            for (String id : ids) {
                dao.delete(Integer.parseInt(id));
            }
        }
    }

    @Override
    public PageBean<User> findUserByPage(String strCurrentPage, String strRows, Map<String, String[]> condition) {
        int currentPage = Integer.parseInt(strCurrentPage);
        int rows = Integer.parseInt(strRows);



        //1.创建PB对象
        PageBean<User> pb = new PageBean<>();
        //2.设置参数

        pb.setRows(rows);
        //3.调用dao查询总记录数
        int totalCount = dao.findTotalCount(condition);
        pb.setTotalCount(totalCount);

        //计算总页码
        int totalPage = totalCount%rows==0? totalCount/rows:totalCount/rows+1;
        pb.setTotalPage(totalPage);

        if(currentPage<=0){
            currentPage=1;
        }
        if(currentPage>totalPage){
            currentPage=totalPage;
        }

        //4.调用dao查询list集合
        //计算开始的记录索引
        int start = (currentPage-1)*rows;
        List<User> list = dao.findByPage(start,rows,condition);
        pb.setList(list);


        pb.setCurrentPage(currentPage);

        return pb;
    }
}
