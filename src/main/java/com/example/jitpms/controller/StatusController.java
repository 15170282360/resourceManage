package com.example.jitpms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.jitpms.pojo.Role;
import com.example.jitpms.pojo.Status;
import com.example.jitpms.pojo.User;
import com.example.jitpms.service.IRoleService;
import com.example.jitpms.service.IStatusService;
import com.example.jitpms.service.IUserService;
import com.example.jitpms.vo.BaseRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
//@RequestMapping("/status")
public class StatusController {

    @Autowired
    private IStatusService statusService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IRoleService roleService;

    @PostMapping("statusList")
    List<Status> statusList() {
        return statusService.list();
    }

    @PostMapping("addStatus")
    Boolean addStatus(@RequestBody Status status) {
        QueryWrapper<Status> qw = new QueryWrapper<>();
        qw.eq("s_name", status.getS_name());
        Status status1 = statusService.getOne(qw);
        System.out.println(status1);
        if (status1 != null) {
            return false;
        }
        return statusService.save(status);
    }

    @PostMapping("delStatus")
    Boolean delStatus(@RequestBody Status status) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("s_id", status.getS_id());
        if (!userService.list(qw).isEmpty()) {
            return false;
        }
        QueryWrapper<Role> qw1 = new QueryWrapper<>();
        qw.eq("s_id", status.getS_id());
        if (!roleService.list(qw1).isEmpty()) {
            return false;
        }

        return statusService.removeById(status.getS_id());
    }

    @PostMapping("delStatuss")
    Boolean delStatuss(@RequestBody List<Integer> ids) {

        return statusService.removeByIds(ids);
    }

    @PostMapping("updateStatusById")
    BaseRes<Status> setStatusById(@RequestBody @Valid Status status) {
        System.out.println(status);
        BaseRes<Status> baseRes = new BaseRes<>(200, "成功", status);
        if (status == null || status.getS_id() == null || status.getS_name() == null || status.getS_name().equals("")) {
            baseRes.setCode(400);
            baseRes.setMsg("参数为空");
        } else {
            QueryWrapper<Status> qw = new QueryWrapper<>();
            qw.eq("s_name", status.getS_name());
            Status status1 = statusService.getOne(qw);
            if (status1 != null) {
                baseRes.setCode(300);
                baseRes.setMsg("name存在");
            } else if(!statusService.updateById(status)) {
                baseRes.setCM(300,"id不存在");
            }
        }
        return baseRes;
    }


}

