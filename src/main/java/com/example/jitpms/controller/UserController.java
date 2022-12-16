package com.example.jitpms.controller;


import cn.dsna.util.images.ValidateCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.jitpms.pojo.Role;
import com.example.jitpms.pojo.Select;
import com.example.jitpms.pojo.Status;
import com.example.jitpms.pojo.User;
import com.example.jitpms.service.IStatusService;
import com.example.jitpms.service.IUserService;
import com.example.jitpms.vo.BaseRes;
import com.example.jitpms.vo.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 吴伟龙
 * @since 2022-12-09
 */
@RestController
//@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IStatusService statusService;

    @GetMapping("vCode")
    protected void vCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //生成验证码：ValidateCode
        ValidateCode vc = new ValidateCode(150, 40, 5, 100);
        //得到生成的验证码
        String code = vc.getCode();
        //把验证码存放到session中
        HttpSession session = request.getSession();
        session.setAttribute("code", code);
        //通过响应的输出流把验证码图片写到页面
        vc.write(response.getOutputStream());
        System.out.println(code);
    }

    @PostMapping("login")
    BaseRes<User> login(HttpSession session, @RequestBody Login login1) {
        BaseRes<User> baseRes = new BaseRes<>(null);
        if (login1.getCode() == null || login1.getU_id() == null || login1.getU_password() == null || login1.getCode().equals("") || login1.getU_password().equals("")) {
            baseRes.setCM(400, "参数为空");
            return baseRes;
        }
        if (!login1.getCode().equalsIgnoreCase((String) session.getAttribute("code"))) {
            baseRes.setCM(300, "验证码错误");
            return baseRes;
        }
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("u_id", login1.getU_id());
        qw.eq("u_password", DigestUtils.md5DigestAsHex(login1.getU_password().getBytes()));
        User user = userService.getOne(qw);
        if (user == null) {
            baseRes.setCM(300, "账号密码错误");
            return baseRes;
        }
        session.setAttribute("userInfo", user);
        baseRes.setData(user);
        return baseRes;
    }

    @RequestMapping("userPage")
    Page<User> userPage(@RequestParam(defaultValue = "1") Integer page,
                        @RequestParam(defaultValue = "50") Integer limit) {
        Page<User> users = new Page<>(page, limit);
        userService.page(users).getRecords();
        return users;
    }

    @PostMapping("addUser")
    Boolean addUser(@RequestBody User user) {
        System.out.println(user);

        if (user.getU_name() == null || user.getU_sex() == null || user.getU_age() == null || user.getS_id() == null || user.getU_name().equals("")) {
            return false;
        }
        if (!(user.getU_sex().equals("男") || user.getU_sex().equals("女"))) {
            return false;
        }
        if (user.getU_age()<0 || user.getU_age()>100) {
            return false;
        }
        if (statusService.getById(user.getS_id()) == null) {
            return false;
        }

        return userService.save(user);
    }

    @PostMapping("delUser")
    Boolean delUser(@RequestBody User user) {
        System.out.println(user.getU_id());
        return userService.removeById(user.getU_id());
    }

    @PostMapping("delUsers")
    Boolean delUsers(@RequestBody List<Integer> ids) {

        return userService.removeByIds(ids);
    }

    @PostMapping("getUserById")
    User getUserById(@RequestBody User user) {
        System.out.println(user.getU_id());
        return userService.getById(user.getU_id());
    }

    @PostMapping("getUserByName")
    List<User> getUserByName(@RequestBody User user) {
        System.out.println(user);
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.like("u_name", user.getU_name());
        return userService.list(qw);
    }

    @PostMapping("selectBy")
    List<User> selectBy(@RequestBody Select select) {
        System.out.println(select);
        QueryWrapper<User> qw = new QueryWrapper<>();
        if (select.getU_sex() != null) {
            if (select.getU_sex() == 1) {
                qw.eq("u_sex", "男");
            } else if (select.getU_sex() == 2) {
                qw.eq("u_sex", "女");
            }
        }
        if (select.getS_id() != null && select.getS_id() > 0) {
            qw.eq("s_id", select.getS_id());
        }
        if (select.getMinAge() != null && select.getMinAge() > 0) {
            qw.ge("u_age", select.getMinAge());
        }
        if (select.getMaxAge() != null && select.getMaxAge() < 100) {
            qw.le("u_age", select.getMaxAge());
        }
        if (select.getOrderByAge() != null) {
            if (select.getOrderByAge() == 1) {
                qw.orderByAsc("u_age");
            } else if (select.getOrderByAge() == 2) {
                qw.orderByDesc("u_age");
            }
        }
        if (select.getOrderById() != null) {
            if (select.getOrderById() == 1) {
                qw.orderByAsc("u_id");
            } else if (select.getOrderById() == 2) {
                qw.orderByDesc("u_id");
            }
        }

        return userService.list(qw);
    }

    @PostMapping("updateUserById")
    BaseRes<User> updateUserById(@RequestBody User user) {
        System.out.println(user);
        BaseRes<User> baseRes = new BaseRes<>(200, "成功", user);
        if (user == null || user.getU_id() == null) {
            baseRes.setCM(400, "参数为空");
            return baseRes;
        } else {
            UpdateWrapper<User> uw = new UpdateWrapper<>();
            uw.eq("u_id", user.getU_id());
            if (user.getU_name() != null && !user.getU_name().equals("")) {
                uw.set("u_name", user.getU_name());
            } else {
                baseRes.setCM(400, "姓名为空");
                return baseRes;
            }

            if (user.getU_sex() != null && !user.getU_sex().equals("")) {
                if (user.getU_sex().equals("男") || user.getU_sex().equals("女")) {
                    uw.set("u_sex", user.getU_sex());
                } else {
                    baseRes.setCM(400, "性别不合法");
                    return baseRes;
                }
            } else {
                baseRes.setCM(400, "性别为空");
                return baseRes;
            }
            if (user.getU_age() != null && user.getU_age()>0 && user.getU_age()<100) {
                uw.set("u_age", user.getU_age());
            } else {
                baseRes.setCM(400, "年龄为空");
                return baseRes;
            }
            if (!userService.update(uw)) {
                baseRes.setCM(300, "id不存在");
            }
        }
        return baseRes;
    }

    @PostMapping("countUserGroupBySId")
    List<Map<String, Object>> countUserBySId() {
        List<Map<String, Object>> list = new ArrayList<>();

        for (Status status : statusService.list()) {
            Map<String, Object> map = new HashMap<>();
            QueryWrapper<User> qw = new QueryWrapper<>();
            qw.eq("s_id", status.getS_id());
            map.put("name", status.getS_name());
            map.put("value", userService.count(qw));
            list.add(map);
        }
        return list;
    }

    @PostMapping("countGroupByUSex")
    List<Map<String, Object>> countGroupByUSex() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> map1 = new HashMap<>();
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("u_sex", "女");
        int i = userService.count(qw);
        map.put("name", "女");
        map.put("value", i);
        map1.put("name", "男");
        map1.put("value", userService.count() - i);
        list.add(map);
        list.add(map1);
        return list;
    }

    @PostMapping("test")
    Object test() {

        return null;
    }
}

