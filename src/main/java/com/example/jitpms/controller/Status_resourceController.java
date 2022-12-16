package com.example.jitpms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.jitpms.pojo.Resource;
import com.example.jitpms.pojo.Role;
import com.example.jitpms.pojo.Status;
import com.example.jitpms.pojo.Status_resource;
import com.example.jitpms.service.IResourceService;
import com.example.jitpms.service.IStatusService;
import com.example.jitpms.service.IStatus_resourceService;
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
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author long
 * @since 2022-12-15
 */
@RestController
@RequestMapping("/status_resource")
public class Status_resourceController {
    @Autowired
    private IStatus_resourceService status_resourceService;
    @Autowired
    private IStatusService statusService;
    @Autowired
    private IResourceService resourceService;

    @PostMapping("queryResourceBySId")
    BaseRes<List<Resource>> queryResourceBySId(@RequestBody Status status) {
        BaseRes<List<Resource>> baseRes = new BaseRes<>(null);
        if (status.getS_id() == null) {
            baseRes.setCM(400, "参数为空");
            return baseRes;
        }

        if (statusService.getById(status.getS_id()) == null) {
            baseRes.setCM(400, "status不存在");
            return baseRes;
        }
        QueryWrapper<Status_resource> qw = new QueryWrapper<>();
        qw.eq("s_id", status.getS_id());
        List<Status_resource> list1 = status_resourceService.list(qw);
        if (list1.isEmpty()) {
            baseRes.setCM(300, "该身份暂无限权");
            return baseRes;
        }
        List<Integer> newList = list1.stream().map(Status_resource::getRe_id).collect(Collectors.toList());
        List<Resource> list2 = resourceService.listByIds(newList);
        if (list2.isEmpty()) {
            baseRes.setCM(300, "该身份暂无限权");
            return baseRes;
        }
        baseRes.setData(list2);
        return baseRes;
    }

    @PostMapping("queryNONResourceBySId")
    BaseRes<List<Resource>> queryNONResourceBySId(@RequestBody Status status) {
        BaseRes<List<Resource>> baseRes = new BaseRes<>(null);
        if (status.getS_id() == null) {
            baseRes.setCM(400, "参数为空");
            return baseRes;
        }
        if (statusService.getById(status.getS_id()) == null) {
            baseRes.setCM(400, "status不存在");
            return baseRes;
        }
        QueryWrapper<Status_resource> qw = new QueryWrapper<>();
        qw.eq("s_id", status.getS_id());
        List<Status_resource> list1 = status_resourceService.list(qw);
        List<Integer> newList1 = list1.stream().map(Status_resource::getRe_id).collect(Collectors.toList());

        List<Resource> list2 = resourceService.list();
        List<Integer> newList2 = list2.stream().map(Resource::getRe_id).collect(Collectors.toList());
        newList2.removeAll(newList1);
        if (newList2.isEmpty()) {
            baseRes.setCM(300, "该身份已拥有所有限权");
            return baseRes;
        }
        List<Resource> list3 = resourceService.listByIds(newList2);
        baseRes.setData(list3);
        return baseRes;
    }

    @PostMapping("addStatusResource")
    BaseRes<Status_resource> addStatusResource(@RequestBody Status_resource statusResource) {

        BaseRes<Status_resource> baseRes = new BaseRes<>(statusResource);
        if (statusResource.getS_id() == null || statusResource.getRe_id() == null) {
            baseRes.setCM(400, "参数为空");
            return baseRes;
        }
        QueryWrapper<Status_resource> qw = new QueryWrapper<>();
        qw.eq("s_id", statusResource.getS_id());
        qw.eq("re_id", statusResource.getRe_id());
        if (status_resourceService.getOne(qw) != null) {
            baseRes.setCM(300, "该关系已存在");
            return baseRes;
        }
        Status status = statusService.getById(statusResource.getS_id());
        Resource resource = resourceService.getById(statusResource.getRe_id());
        if (status == null || resource == null) {
            baseRes.setCM(400, "s_id或re_id不存在");
            return baseRes;
        }
        if (!status_resourceService.save(statusResource)) {
            baseRes.setCM(300, "失败");
            return baseRes;
        }
        return baseRes;
    }

    @PostMapping("delStatusResource")
    BaseRes<Status_resource> delStatusResource(@RequestBody Status_resource statusResource) {
        BaseRes<Status_resource> baseRes = new BaseRes<>(statusResource);
        if (statusResource.getS_id() == null || statusResource.getRe_id() == null) {
            baseRes.setCM(400, "参数为空");
            return baseRes;
        }
        QueryWrapper<Status_resource> qw = new QueryWrapper<>();
        qw.eq("s_id", statusResource.getS_id());
        qw.eq("re_id", statusResource.getRe_id());
        if (!status_resourceService.remove(qw)) {
            baseRes.setCM(300, "该关系不存在");
            return baseRes;
        }
        return baseRes;
    }

    @PostMapping("queryStatusResource")
    List<Map<String, Object>> queryStatusResource() {
        List<Map<String, Object>> list = new ArrayList<>();
        List<Status> status = statusService.list();
        for (Status s:status) {
            Map<String, Object> map = new HashMap<>();
            map.put("status", s);
            map.put("resource", queryResourceBySId(s).getData());
            list.add(map);
        }
        return list;
    }

}

