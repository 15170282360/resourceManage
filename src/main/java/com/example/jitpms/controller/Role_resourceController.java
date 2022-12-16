package com.example.jitpms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.jitpms.pojo.Resource;
import com.example.jitpms.pojo.Role;
import com.example.jitpms.pojo.Role_resource;
import com.example.jitpms.service.IResourceService;
import com.example.jitpms.service.IRoleService;
import com.example.jitpms.service.IRole_resourceService;
import com.example.jitpms.service.IUser_resourceService;
import com.example.jitpms.vo.BaseRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 吴伟龙
 * @since 2022-12-09
 */
@RestController
@RequestMapping("/role_resource")
public class Role_resourceController {

    @Autowired
    private IRole_resourceService role_resourceService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IResourceService resourceService;

    @PostMapping("queryResourceByRId")
    BaseRes<List<Resource>> queryResourceByRId(@RequestBody Role role) {
        BaseRes<List<Resource>> baseRes = new BaseRes<>(null);
        if (role.getR_id() == null) {
            baseRes.setCM(400, "参数为空");
            return baseRes;
        }
        if (roleService.getById(role.getR_id()) == null) {
            baseRes.setCM(400, "role不存在");
            return baseRes;
        }
        QueryWrapper<Role_resource> qw = new QueryWrapper<>();
        qw.eq("r_id", role.getR_id());
        List<Role_resource> list1 = role_resourceService.list(qw);
        if (list1.isEmpty()) {
            baseRes.setCM(300, "该角色暂无限权");
            return baseRes;
        }
        List<Integer> newList = list1.stream().map(Role_resource::getRe_id).collect(Collectors.toList());
        List<Resource> list2 = resourceService.listByIds(newList);
        if (list2.isEmpty()) {
            baseRes.setCM(300, "该角色暂无限权");
            return baseRes;
        }
        baseRes.setData(list2);
        return baseRes;
    }

    @PostMapping("queryNONResourceByRId")
    BaseRes<List<Resource>> queryNONResourceByRId(@RequestBody Role role) {
        BaseRes<List<Resource>> baseRes = new BaseRes<>(null);
        if (role.getR_id() == null) {
            baseRes.setCM(400, "参数为空");
            return baseRes;
        }
        if (roleService.getById(role.getR_id()) == null) {
            baseRes.setCM(400, "role不存在");
            return baseRes;
        }
        QueryWrapper<Role_resource> qw = new QueryWrapper<>();
        qw.eq("r_id", role.getR_id());
        List<Role_resource> list1 = role_resourceService.list(qw);
        List<Integer> newList1 = list1.stream().map(Role_resource::getRe_id).collect(Collectors.toList());

        List<Resource> list2 = resourceService.list();
        List<Integer> newList2 = list2.stream().map(Resource::getRe_id).collect(Collectors.toList());
        newList2.removeAll(newList1);
        if (newList2.isEmpty()) {
            baseRes.setCM(300, "该角色已拥有所有限权");
            return baseRes;
        }
        List<Resource> list3 = resourceService.listByIds(newList2);
        baseRes.setData(list3);
        return baseRes;
    }

    @PostMapping("addRoleResource")
    BaseRes<Role_resource> addRoleResource(@RequestBody Role_resource roleResource) {

        BaseRes<Role_resource> baseRes = new BaseRes<>(roleResource);
        if (roleResource.getR_id() == null || roleResource.getRe_id() == null) {
            baseRes.setCM(400, "参数为空");
            return baseRes;
        }
        QueryWrapper<Role_resource> qw = new QueryWrapper<>();
        qw.eq("r_id", roleResource.getR_id());
        qw.eq("re_id", roleResource.getRe_id());
        if (role_resourceService.getOne(qw) != null) {
            baseRes.setCM(300, "该关系已存在");
            return baseRes;
        }
        Role role = roleService.getById(roleResource.getR_id());
        Resource resource = resourceService.getById(roleResource.getRe_id());
        if (role == null || resource == null) {
            baseRes.setCM(400, "r_id或re_id不存在");
            return baseRes;
        }
        if (!role_resourceService.save(roleResource)) {
            baseRes.setCM(300, "失败");
            return baseRes;
        }
        return baseRes;
    }

    @PostMapping("delRoleResource")
    BaseRes<Role_resource> delRoleResource(@RequestBody Role_resource roleResource) {
        BaseRes<Role_resource> baseRes = new BaseRes<>(roleResource);
        if (roleResource.getR_id() == null || roleResource.getRe_id() == null) {
            baseRes.setCM(400, "参数为空");
            return baseRes;
        }
        QueryWrapper<Role_resource> qw = new QueryWrapper<>();
        qw.eq("r_id", roleResource.getR_id());
        qw.eq("re_id", roleResource.getRe_id());
        if (!role_resourceService.remove(qw)) {
            baseRes.setCM(300, "该关系不存在");
            return baseRes;
        }
        return baseRes;
    }

    @PostMapping("queryRoleResource")
    List<Map<String, Object>> queryRoleResource() {
        List<Map<String, Object>> list = new ArrayList<>();
        List<Role> roles = roleService.list();
        for (Role r:roles) {
            Map<String, Object> map = new HashMap<>();
            map.put("role", r);
            map.put("resource", queryResourceByRId(r).getData());
            list.add(map);
        }
        return list;
    }
}

