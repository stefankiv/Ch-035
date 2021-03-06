package com.crsms.dao;

import java.util.List;

import com.crsms.domain.Test;


/**
 * @author Petro Andriets
 */

public interface TestDao extends BaseDao<Test> {
    
    List<Test> getAllByModuleId(Long moduleId);

    void deleteTestById(Long id);
    
    void disableTestById(Long id);
    
    void disable(Test test);
    
}
