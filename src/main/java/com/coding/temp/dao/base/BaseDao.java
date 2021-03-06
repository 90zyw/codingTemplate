package com.coding.temp.dao.base;

import java.util.List;
import java.util.Map;

/**
 * @author zhangyongwei
 * @date 2019-03-02 20:19:53
 */
public interface BaseDao<T>{

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    T selectByPrimaryKey(Long id);

    /**
     * 获取所有列表
     * @return
     */
    List<T> selectAllList();

    /**
     * 根据条件查询列表
     * @param obj
     * @return
     */
    List<T> selectListByParams(T obj);

    /**
     * 插入
     * @param obj
     * @return
     */
    int insert(T obj);

    /**
     * 修改
     * @param obj
     * @return
     */
    int updateByPrimaryKeySelective(T obj);

    /**
     * 删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 查询总条数
     * @return
     */
    int selectListCount();

    /**
     * 查询分页列表
     * @param obj
     * @return
     */
    List<T> selectPageList(T obj);

    /**
     * 查询分页总条数
     * @param obj
     * @return
     */
    int selectPageCount(T obj);

}
