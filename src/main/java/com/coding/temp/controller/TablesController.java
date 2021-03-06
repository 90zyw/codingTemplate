package com.coding.temp.controller;

import com.alibaba.fastjson.JSONObject;
import com.coding.temp.entity.Tables;
import com.coding.temp.service.TablesService;
import com.coding.temp.utils.Result;
import com.coding.temp.utils.SessionUtil;
import com.coding.temp.utils.ZipUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.zip.ZipOutputStream;

/**
 * @author Zhang Yongei
 * @version 1.0
 * @date 2019-03-22
 */
@Controller
@RequestMapping("tables")
public class TablesController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectController.class);
    @Autowired
    private TablesService tablesService;

    /**
     * 数据库首页
     * @return
     */
    @RequestMapping
    public String index(Long dataBaseId, Model model){
        model.addAttribute("dataBaseId",dataBaseId);
        return "tables/index";
    }

    /**
     * 列表数据
     * @return
     */
    @RequestMapping("page")
    @ResponseBody
    public Object list(Tables tables, HttpServletRequest request){
        try {
            tables.setUserId(SessionUtil.getUserId(request));
            return tablesService.selectPage(tables);
        }catch (Exception e){
            e.printStackTrace();
            return Result.error();
        }
    }

    /**
     * 添加或者修改
     * @param tables
     * @return
     */
    @RequestMapping("saveOrUpdate")
    @ResponseBody
    public Object saveOrUpdate(Tables tables){
        try{
            Date date  = new Date();
            if(tables.getId() != null){
                tables.setCreateTime(date);
                tablesService.insert(tables);
            }else{
                tablesService.updateByPrimaryKeySelective(tables);
            }
            return Result.ok();
        }catch (Exception e){
            e.printStackTrace();
            return Result.error();
        }
    }

    /**
     * 删除数据
     * @param id
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public Object delete(Long id){
        try{
            tablesService.deleteByPrimaryKey(id);
            return Result.ok();
        }catch (Exception e){
            return Result.error();
        }
    }

    /**
     * 生成字段
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("createColumn")
    @ResponseBody
    public Object createColumn(Long id,HttpServletRequest request){
        try{
            Boolean flag = tablesService.createColumn(id,SessionUtil.getUserId(request));
            if(flag){
                return Result.ok();
            }else{
                return Result.error();
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.error();
        }
    }

    /**
     * 单个下载
     * @param id
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("downLoad")
    @ResponseBody
    public Object downLoad(Long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
        try{
            JSONObject result = tablesService.tableGenerate(id,SessionUtil.getUserId(request));
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition","attachment; filename="+result.get("fileName"));
            ZipUtils.doCompress(result.get("url").toString(), out);
            response.flushBuffer();
            return Result.ok(result);
        }catch (Exception e){
            e.printStackTrace();
            return Result.error();
        }finally{
            out.close();
        }
    }

    /**
     * 批量下载
     * @param ids
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("batchDownLoad")
    @ResponseBody
    public Object batchDownLoad(String ids, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
        try{
            JSONObject result = tablesService.tableBatchGenerate(ids,SessionUtil.getUserId(request));
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition","attachment; filename="+result.get("fileName"));
            ZipUtils.doCompress(result.get("url").toString(), out);
            response.flushBuffer();
            return Result.ok(result);
        }catch (Exception e){
            e.printStackTrace();
            return Result.error();
        }finally{
            out.close();
        }
    }
}
