package com.example.jitpms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.example.jitpms.pojo.Resource;
import com.example.jitpms.service.IResourceService;
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
//@RequestMapping("/resource")
public class ResourceController {

    @Autowired
    private IResourceService resourceService;

    @PostMapping("resourceList")
    BaseRes<List<Resource>> resourceList() {
        return new BaseRes<>(resourceService.list());
    }

    @PostMapping("addResource")
    BaseRes<Resource> addResource(@RequestBody Resource resource) {
        BaseRes<Resource> baseRes = new BaseRes<>(resource);
        QueryWrapper<Resource> qw = new QueryWrapper<>();
        if (resource.getRe_name() == null || resource.getRe_name().equals("")) {
            baseRes.setCM(400, "参数为空");
            return baseRes;
        }
        qw.eq("re_name", resource.getRe_name());
        if (resourceService.getOne(qw) != null) {
            baseRes.setCM(300, "name存在");
            return baseRes;
        }
        if (!resourceService.save(resource)) {
            baseRes.setCM(300, "失败");
            return baseRes;
        }
        baseRes.setData(resourceService.getOne(qw));
        return baseRes;
    }

    @PostMapping("updateResourceById")
    BaseRes<Resource> updateResourceById(@RequestBody Resource resource) {
        BaseRes<Resource> baseRes = new BaseRes<>(resource);
        if (resource.getRe_id() == null || resource.getRe_name() == null || resource.getRe_name().equals("")) {
            baseRes.setCM(400, "参数为空");
            return baseRes;
        }
        QueryWrapper<Resource> qw = new QueryWrapper<>();
        qw.eq("re_name", resource.getRe_name());
        if (resourceService.getOne(qw) != null) {
            baseRes.setCM(300, "name存在");
            return baseRes;
        }
        if (!resourceService.updateById(resource)) {
            baseRes.setCM(300, "失败，id不存在");
            return baseRes;
        }
        return baseRes;
    }

    @PostMapping("delResource")
    BaseRes<Resource> delResource(@RequestBody Resource resource) {
        BaseRes<Resource> baseRes = new BaseRes<>(resource);
        if (resource.getRe_id() == null) {
            baseRes.setCM(400,"参数为空");
            return baseRes;
        }
        if (resource.getRe_name() == null || resource.getRe_name().equals("")) {
            Resource resource1 = resourceService.getById(resource.getRe_id());
            if (resource1 == null) {
                baseRes.setCM(300, "id不存在");
                return baseRes;
            }
            baseRes.setData(resource1);
        }
        if (!resourceService.removeById(resource.getRe_id())) {
            baseRes.setCM(300,"失败,id不存在");
            baseRes.setData(resource);
            return baseRes;
        }
        return baseRes;
    }

    @PostMapping("getResourceByName")
    BaseRes<List<Resource>> getResourceByName(@RequestBody Resource resource) {
        BaseRes<List<Resource>> baseRes = new BaseRes<>(null);
        if (resource.getRe_name() == null || resource.getRe_name().equals("")) {
            baseRes.setCM(400,"参数为空");
            return baseRes;
        }
        QueryWrapper<Resource> qw = new QueryWrapper<>();
        qw.like("re_name", resource.getRe_name());
        List<Resource> list = resourceService.list(qw);
        if (list.isEmpty()) {
            baseRes.setCM(300, "未找到");
            baseRes.setData(list);
            return baseRes;
        }
        baseRes.setData(list);
        return baseRes;
    }
}


