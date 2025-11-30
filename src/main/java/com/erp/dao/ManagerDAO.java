package com.erp.dao;

import com.erp.dto.ManagerDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ManagerDAO {
    void addManager(ManagerDTO manager);
    void setManager(ManagerDTO manager);
    void removeManager(ManagerDTO manager);
    List<ManagerDTO> getManagers();
    ManagerDTO getManagerDetail(String managerId);
    ManagerDTO getManagerForLogin(String managerId);
}
