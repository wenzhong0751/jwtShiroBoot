package com.shadow.springboot.application.service;

import com.shadow.springboot.application.domain.bo.Resource;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ResourceService {
    List<Resource> getMenus();

    List<Resource> getAuthorityMenusByUid(Long uid);

    Boolean addMenu(Resource resource);

    Boolean modifyMenu(Resource resource);

    Boolean deleteMenuByMenuId(Integer rid);

    List<Resource> getApiTeamList();

    Page<Resource> getApiList(int pageNum, int pageSize);

    Page<Resource> getApiListByTeamId(Integer teamId, int pageNum, int pageSize);
}
