package org.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.pojo.MyUser;
import org.example.tools.LoginUser;

import java.util.List;

@Mapper
public interface MyUserMapper {
    @Select("SELECT * FROM my_users order by reg_time desc")
    List<MyUser> findAll();

    @Select("SELECT * FROM  my_users WHERE id = #{id}")
    MyUser findById(int id);


    @Select("SELECT * FROM my_users WHERE name = #{name}")
    MyUser findByName(String name);

    @Select("UPDATE my_users set status = 2 where id = #{id}")
    MyUser deleteUser(int id);

    @Insert("insert my_users(name, password, email, phone, role_id, status, reg_time) values (#{name},#{password},#{email},#{phone},#{roleId},#{status},#{regTime})")
    boolean add(MyUser myUser);

    @Select("SELECT id FROM my_users WHERE name=#{username} and password = #{password}")
    Integer doLogin(LoginUser loginUser);

    boolean update(MyUser myUser);
}