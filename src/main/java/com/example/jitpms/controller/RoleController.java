package com.example.jitpms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.example.jitpms.pojo.Role;
import com.example.jitpms.pojo.Status;
import com.example.jitpms.service.IRoleService;
import com.example.jitpms.service.IStatusService;
import com.example.jitpms.service.IUserService;
import com.example.jitpms.vo.BaseRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 吴伟龙
 * @since 2022-12-09
 */
@RestController
//@RequestMapping("/role")
public class RoleController {
    @Autowired
    private IRoleService roleService;

    @PostMapping("roleList")
    List<Role> roleList(@RequestBody Role role) {
        System.out.println(role);
        QueryWrapper<Role> qw = new QueryWrapper<>();
        if (role.getS_id() != null && role.getS_id() > 0) {
            qw.eq("s_id", role.getS_id());
        }
        return roleService.list(qw);
    }

    @PostMapping("addRole")
    Boolean addRole(@RequestBody Role role) {
        System.out.println(role);
        QueryWrapper<Role> qw = new QueryWrapper<>();
        qw.eq("r_name", role.getR_name());
        if (roleService.getOne(qw) != null) {
            return false;
        }
        try {
            return roleService.save(role);
        } catch (Exception e) {
            return false;
        }
    }

    @PostMapping("delRole")
    Boolean delRole(@RequestBody Role role) {

        return roleService.removeById(role.getR_id());
    }

    @PostMapping("delRoles")
    Boolean delRoles(@RequestBody List<Integer> rIds) {

        return roleService.removeByIds(rIds);
    }

    @PostMapping("queryRole")
    BaseRes<List<Role>> queryRole(@RequestBody Role role) {
        System.out.println(role);
        BaseRes<List<Role>> baseRes = new BaseRes<>();
        QueryWrapper<Role> qw = new QueryWrapper<>();
        if (role.getR_id() != null) {
            qw.eq("r_id", role.getR_id());
        }
        if (role.getR_name() != null && !role.getR_name().equals("")) {
            qw.eq("r_name", role.getR_name());
        }
        if (role.getS_id() != null) {
            qw.eq("S_id", role.getS_id());
        }
        if (qw.isEmptyOfWhere()) {
            baseRes.setCM(400, "参数空");
            return baseRes;
        }
        List<Role> list = roleService.list(qw);
        baseRes.setData(list);
        if (list.isEmpty()) {
            baseRes.setCM(300, "未找到");
        } else {
            baseRes.setCM(200, "成功找到");
        }
        return baseRes;
    }

    @PostMapping("updateRoleById")
    BaseRes<Role> updateRoleById(@RequestBody Role role) {
        BaseRes<Role> baseRes = new BaseRes<>(role);
        if (role == null || role.getR_id() == null) {
            baseRes.setCM(400, "参数为空");
        } else {
            Role role1 = new Role();
            UpdateWrapper<Role> uw = new UpdateWrapper<>();
            uw.eq("r_id", role.getR_id());
            if (role.getR_name() != null && !role.getR_name().equals("")) {
                role1.setR_name(role.getR_name());
                if (queryRole(role1).getCode() == 300) {
                    uw.set("r_name", role.getR_name());
                } else {
                    baseRes.setCM(300, "name存在");
                    return baseRes;
                }
            } else {
                baseRes.setCM(400, "参数为空");
                return baseRes;
            }
            if (!roleService.update(uw)) {
                baseRes.setCM(300, "id不存在");
            }
        }
        return baseRes;
    }

}

