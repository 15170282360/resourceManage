package com.example.jitpms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.example.jitpms.pojo.Role;
import com.example.jitpms.pojo.User;
import com.example.jitpms.pojo.User_role;
import com.example.jitpms.service.*;
import com.example.jitpms.vo.BaseRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/user_role")
public class User_roleController {
    @Autowired
    private IUser_roleService user_roleService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IStatusService statusService;

    @PostMapping("queryRoleByUId")
    BaseRes<List<Role>> queryRoleByUId(@RequestBody User user) {

        BaseRes<List<Role>> baseRes = new BaseRes<>(null);
        if (user.getU_id() == null) {
            baseRes.setCM(400, "参数为空");
            return baseRes;
        }
        User user1 = userService.getById(user.getU_id());
        if (user1 == null) {
            baseRes.setCM(400, "u_id不存在");
            return baseRes;
        }
        QueryWrapper<User_role> qw = new QueryWrapper<>();
        qw.eq("u_id", user.getU_id());
        List<User_role> list = user_roleService.list(qw);
        if (list.isEmpty()) {
            baseRes.setCM(300, "该用户无role");
            return baseRes;
        }

        QueryWrapper<Role> qw1 = new QueryWrapper<>();
        List<Integer> list1 = new ArrayList<>();
        for (User_role uRole : list) {
            list1.add(uRole.getR_id());
        }
        qw1.in("r_id", list1);
        baseRes.setData(roleService.list(qw1));
        return baseRes;
    }

    @PostMapping("queryNoNRoleByUId")
    BaseRes<List<Role>> queryNoNRoleByUId(@RequestBody User user) {
        BaseRes<List<Role>> baseRes = new BaseRes<>(null);
        if (user.getU_id() == null) {
            baseRes.setCM(400, "参数为空");
            return baseRes;
        }
        User user1 = userService.getById(user.getU_id());
        if (user1 == null) {
            baseRes.setCM(400, "u_id不存在");
            return baseRes;
        }
        QueryWrapper<User_role> qw = new QueryWrapper<>();
        qw.eq("u_id", user.getU_id());

        List<User_role> list = user_roleService.list(qw);
        QueryWrapper<Role> qw1 = new QueryWrapper<>();
        if (!list.isEmpty()) {
            List<Integer> list1 = new ArrayList<>();
            for (User_role uRole : list) {
                list1.add(uRole.getR_id());
            }
            qw1.notIn("r_id", list1);
        }
        qw1.eq("s_id", user1.getS_id());
        baseRes.setData(roleService.list(qw1));
        return baseRes;

    }

    @PostMapping("addRoleToUser")
    BaseRes<Object> addRoleToUser(@RequestBody User_role userRole) {
        BaseRes<Object> baseRes = new BaseRes<>(null);
        if (userRole.getU_id() == null || userRole.getR_id() == null) {
            baseRes.setCM(400, "参数为空");
            return baseRes;
        }
        QueryWrapper<User_role> qw = new QueryWrapper<>();
        qw.eq("u_id", userRole.getU_id());
        qw.eq("r_id", userRole.getR_id());
        if (user_roleService.getOne(qw) != null) {
            baseRes.setCM(300, "该用户已为该角色");
            return baseRes;
        }
        User user = userService.getById(userRole.getU_id());
        if (user == null) {
            baseRes.setCM(400, "u_id不存在");
            return baseRes;
        }
        Role role = roleService.getById(userRole.getR_id());
        if (role == null) {
            baseRes.setCM(400, "r_id不存在");
            return baseRes;
        }
        if (!user.getS_id().equals(role.getS_id())) {
            baseRes.setCM(400, "该用户不能拥有该角色，即二者身份s_id不匹配");
            return baseRes;
        }
        if (!user_roleService.save(userRole)) {
            baseRes.setCM(300, "失败");
            return baseRes;
        }
        return baseRes;
    }

    @PostMapping("delRoleOfUser")
    BaseRes<Object> delRoleOfUser(@RequestBody User_role userRole) {
        BaseRes<Object> baseRes = new BaseRes<>(null);
        if (userRole.getU_id() == null || userRole.getR_id() == null) {
            baseRes.setCM(400, "参数为空");
            return baseRes;
        }
        QueryWrapper<User_role> qw = new QueryWrapper<>();
        qw.eq("u_id", userRole.getU_id());
        qw.eq("r_id", userRole.getR_id());
        if (!user_roleService.remove(qw)) {
            baseRes.setCM(300, "失败,不存在");
        }
        return baseRes;
    }

    @PostMapping("queryUserStatusRole")
    List<Map<String, Object>> queryUserStatusRole() {
        List<Map<String, Object>> list = new ArrayList<>();
        List<User> users = userService.list();
        for (User u:users) {
            Map<String, Object> map = new HashMap<>();
            map.put("user", u);
            map.put("status", statusService.getById(u.getS_id()));
            map.put("role", queryRoleByUId(u).getData());
            list.add(map);
        }
        return list;
    }

    @PostMapping("queryUserByRId")
    BaseRes<List<Role>> queryRoleByUId(@RequestBody Role role) {

        BaseRes<List<Role>> baseRes = new BaseRes<>(null);
        if (role.getR_id() == null) {
            baseRes.setCM(400, "参数为空");
            return baseRes;
        }
        if (roleService.getById(role.getR_id()) == null) {
            baseRes.setCM(400, "r_id不存在");
            return baseRes;
        }
        QueryWrapper<User_role> qw = new QueryWrapper<>();
        qw.eq("r_id", role.getR_id());
        List<User_role> list = user_roleService.list(qw);
        if (list == null) {
            System.out.println(111111);
        }
        return null;
    }


}

