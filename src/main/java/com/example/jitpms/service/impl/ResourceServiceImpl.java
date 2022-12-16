package com.example.jitpms.service.impl;

import com.example.jitpms.pojo.Resource;
import com.example.jitpms.dao.IResourceDao;
import com.example.jitpms.service.IResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 吴伟龙
 * @since 2022-12-09
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<IResourceDao, Resource> implements IResourceService {

}
