package com.example.jitpms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.jitpms.pojo.*;
import com.example.jitpms.service.*;
import com.example.jitpms.vo.BaseRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 吴伟龙
 * @since 2022-12-09
 */
@RestController
@RequestMapping("/user_resource")
public class User_resourceController {
    @Autowired
    private IUser_resourceService user_resourceService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IResourceService resourceService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IStatusService statusService;
    @Autowired
    private IStatus_resourceService status_resourceService;
    @Autowired
    private IRole_resourceService role_resourceService;
    @Autowired
    private IUser_roleService user_roleService;

    @PostMapping("queryResourceByUId")
    BaseRes<List<Resource>> queryResourceByUId(@RequestBody User user) {
        BaseRes<List<Resource>> baseRes = new BaseRes<>(null);
        if (user.getU_id() == null) {
            baseRes.setCM(400, "参数为空");
            return baseRes;
        }
        if (userService.getById(user.getU_id()) == null) {
            baseRes.setCM(400, "user不存在");
            return baseRes;
        }
        QueryWrapper<User_resource> qw = new QueryWrapper<>();
        qw.eq("u_id", user.getU_id());
        List<User_resource> list1 = user_resourceService.list(qw);
        if (list1.isEmpty()) {
            baseRes.setCM(300, "该用户暂无限权");
            return baseRes;
        }
        List<Integer> newList = list1.stream().map(User_resource::getRe_id).collect(Collectors.toList());
        List<Resource> list2 = resourceService.listByIds(newList);
        if (list2.isEmpty()) {
            baseRes.setCM(300, "该用户暂无限权");
            return baseRes;
        }
        baseRes.setData(list2);
        return baseRes;
    }

    @PostMapping("queryNONResourceByUId")
    BaseRes<List<Resource>> queryNONResourceByUId(@RequestBody User user) {
        BaseRes<List<Resource>> baseRes = new BaseRes<>(null);
        if (user.getU_id() == null) {
            baseRes.setCM(400, "参数为空");
            return baseRes;
        }
        if (userService.getById(user.getU_id()) == null) {
            baseRes.setCM(400, "user不存在");
            return baseRes;
        }
        QueryWrapper<User_resource> qw = new QueryWrapper<>();
        qw.eq("u_id", user.getU_id());
        List<User_resource> list1 = user_resourceService.list(qw);
        List<Integer> newList1 = list1.stream().map(User_resource::getRe_id).collect(Collectors.toList());

        List<Resource> list2 = resourceService.list();
        List<Integer> newList2 = list2.stream().map(Resource::getRe_id).collect(Collectors.toList());
        newList2.removeAll(newList1);
        if (newList2.isEmpty()) {
            baseRes.setCM(300, "该用户已拥有所有限权");
            return baseRes;
        }
        List<Resource> list3 = resourceService.listByIds(newList2);
        baseRes.setData(list3);
        return baseRes;
    }

    @PostMapping("addUserResource")
    BaseRes<User_resource> addUserResource(@RequestBody User_resource userResource) {

        BaseRes<User_resource> baseRes = new BaseRes<>(userResource);
        if (userResource.getU_id() == null || userResource.getRe_id() == null) {
            baseRes.setCM(400, "参数为空");
            return baseRes;
        }
        QueryWrapper<User_resource> qw = new QueryWrapper<>();
        qw.eq("u_id", userResource.getU_id());
        qw.eq("re_id", userResource.getRe_id());
        if (user_resourceService.getOne(qw) != null) {
            baseRes.setCM(300, "该关系已存在");
            return baseRes;
        }
        User user = userService.getById(userResource.getU_id());
        Resource resource = resourceService.getById(userResource.getRe_id());
        if (user == null || resource == null) {
            baseRes.setCM(400, "u_id或re_id不存在");
            return baseRes;
        }
        if (!user_resourceService.save(userResource)) {
            baseRes.setCM(300, "失败");
            return baseRes;
        }
        return baseRes;
    }

    @PostMapping("delUserResource")
    BaseRes<User_resource> delUserResource(@RequestBody User_resource userResource) {
        BaseRes<User_resource> baseRes = new BaseRes<>(userResource);
        if (userResource.getU_id() == null || userResource.getRe_id() == null) {
            baseRes.setCM(400, "参数为空");
            return baseRes;
        }
        QueryWrapper<User_resource> qw = new QueryWrapper<>();
        qw.eq("u_id", userResource.getU_id());
        qw.eq("re_id", userResource.getRe_id());
        if (!user_resourceService.remove(qw)) {
            baseRes.setCM(300, "该关系不存在");
            return baseRes;
        }
        return baseRes;
    }

    @PostMapping("queryUserResource")
    List<Map<String, Object>> queryUserResource() {
        List<Map<String, Object>> list = new ArrayList<>();
        List<User> user = userService.list();
        for (User s:user) {
            Map<String, Object> map = new HashMap<>();
            map.put("user", s);
            map.put("resource", queryResourceByUId(s).getData());
            list.add(map);
        }
        return list;
    }

    @PostMapping("queryResourceByUIdAll")
    BaseRes<TreeSet<Resource>> queryResourceByUIdAll(@RequestBody User user) {
        BaseRes<TreeSet<Resource>> baseRes = new BaseRes<>(null);
        if (user.getU_id() == null) {
            baseRes.setCM(400, "参数为空");
            return baseRes;
        }
        User user1 = userService.getById(user.getU_id());
        if (user1 == null) {
            baseRes.setCM(400, "user不存在");
            return baseRes;
        }

        TreeSet<Resource> set = new TreeSet<>(new Comparator<Resource>() {
            @Override
            public int compare(Resource o1, Resource o2) {
                return o1.getRe_id()-o2.getRe_id();
            }
        });

        List<Resource> resourceList = queryResourceByUId(user).getData();
        if (resourceList != null) {
            set.addAll(resourceList);
        }

        Status status = statusService.getById(user1.getS_id());
        QueryWrapper<Status_resource> qw1 = new QueryWrapper<>();
        qw1.eq("s_id", status.getS_id());
        List<Status_resource> status_resourceList = status_resourceService.list(qw1);
        List<Integer> newList1 = status_resourceList.stream().map(Status_resource::getRe_id).collect(Collectors.toList());
        if (!status_resourceList.isEmpty()) {
            set.addAll(resourceService.listByIds(newList1));
        }

        QueryWrapper<User_role> qw2 = new QueryWrapper<>();
        qw2.eq("u_id", user.getU_id());
        List<User_role> userRoleList = user_roleService.list(qw2);
        List<Integer> newList2 = userRoleList.stream().map(User_role::getR_id).collect(Collectors.toList());
        if (!newList2.isEmpty()) {
            QueryWrapper<Role_resource> qw3 = new QueryWrapper<>();
            qw3.in("r_id",newList2);
            List<Role_resource> role_resourceList = role_resourceService.list(qw3);
            List<Integer> newList3 = role_resourceList.stream().map(Role_resource::getRe_id).collect(Collectors.toList());
            if (!newList3.isEmpty()) {
                set.addAll(resourceService.listByIds(newList3));
            }
        }
        baseRes.setData(set);
        if (baseRes.getData().isEmpty()){
            baseRes.setCM(300,"该用户无任何限权");
        }
        return baseRes;
    }

}

