package org.example.service;

import org.example.mapper.MyUserMapper;
import org.example.pojo.MyUser;
import org.example.tools.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    MyUserMapper myUserMapper;

    /**
     * 注册
     *
     * @param myUser 传递的用户数据
     * @return JsonResultObject
     */
    public JsonResultObject register(MyUser myUser) {
        MyUser user = myUserMapper.findByName(myUser.getName());
        //初始化 JSON 返回对象
        JsonResultObject jsonResultObject = this.initJsonResultObject();
        //用户已存在
        if (user != null) {
            jsonResultObject.setErrorMessage("用户已存在");
            jsonResultObject.setErrorCode(ErrorEnum.BAD_INPUT_PARAM.getErrorCode());
            return jsonResultObject;
        }


        //md5 处理密码
        String password = Md5Utils.stringToMD5(myUser.getPassword());
        myUser.setPassword(password);
        long unixTime = System.currentTimeMillis() / 1000L;
        int nowUnixTime = (int) unixTime;
        myUser.setRegTime(nowUnixTime);
        boolean addResult = myUserMapper.add(myUser);

        if (addResult) {
            jsonResultObject.setMessage("新建用户成功");
        } else {
            jsonResultObject.setErrorMessage("新建用户失败");
            jsonResultObject.setErrorCode("202311");
        }
        return jsonResultObject;
    }


    public JsonResultObject login(LoginUser loginUser) {
        JsonResultObject result = this.initJsonResultObject();
        ErrorEnum enum1 = ErrorEnum.valueOf(ErrorEnum.class, "PASSWORD_OR_USERNAME_WRONG");
        try {
            //md5处理密码
            String password = Md5Utils.stringToMD5(loginUser.getPassword());
            loginUser.setPassword(password);
            Integer userId = myUserMapper.doLogin(loginUser);
            if (userId == null) {
                result.setErrorMessage("用户名或密码错误");
                result.setErrorCode(enum1.getErrorCode());
                result.setErrorMessage(enum1.getErrorMsg());
            } else {
                //创建一个MyUser对象
                MyUser currentUser = new MyUser();
                currentUser.setId(userId);
                currentUser.setPassword(password);
                currentUser.setName(loginUser.getUsername());
                result.setData(currentUser);
                result.setMessage("登录成功");
            }
        } catch (Exception e) {
            result.setCode("200");
            result.setErrorMessage(e.getMessage());
            result.setErrorCode("100211");
        }
        return result;
    }


    private JsonResultObject initJsonResultObject() {
        JsonResultObject result = new JsonResultObject();
        result.setCode("200");
        result.setMessage("");
        result.setErrorMessage("");
        result.setErrorCode("");
        return result;
    }

    public MyUser findUserById(int userIdt) {
        MyUser userbyId = myUserMapper.findById(userIdt);
        return userbyId;
    }

    public JsonResultObject update(MyUser myUser) {
        LoginUser user = UserContext.getUser();
        int id = (int) user.getId();
        myUser.setId(id);
        JsonResultObject jsonResultObject = initJsonResultObject();
        boolean result = myUserMapper.update(myUser);
        if (!result) {
            jsonResultObject.setErrorCode(Enum.valueOf(ErrorEnum.class, "BAD_INPUT_PARAM").getErrorCode());
            jsonResultObject.setErrorMessage(ErrorEnum.BAD_INPUT_PARAM.getErrorMsg());
            return jsonResultObject;
        }
        jsonResultObject.setMessage("修改成功");
        return jsonResultObject;
    }
}
