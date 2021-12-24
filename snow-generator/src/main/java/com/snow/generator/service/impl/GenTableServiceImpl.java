package com.snow.generator.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.snow.common.constant.Constants;
import com.snow.common.constant.GenConstants;
import com.snow.common.core.text.CharsetKit;
import com.snow.common.core.text.Convert;
import com.snow.common.exception.BusinessException;
import com.snow.common.utils.DateUtils;
import com.snow.common.utils.StringUtils;
import com.snow.common.utils.file.FileUtils;
import com.snow.generator.domain.GenTable;
import com.snow.generator.domain.GenTableColumn;
import com.snow.generator.mapper.GenTableColumnMapper;
import com.snow.generator.mapper.GenTableMapper;
import com.snow.generator.service.IGenTableService;
import com.snow.generator.util.GenUtils;
import com.snow.generator.util.VelocityInitializer;
import com.snow.generator.util.VelocityUtils;
import com.snow.system.domain.SysMenu;
import com.snow.system.service.ISysMenuService;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 业务 服务层实现
 * 
 * @author snow
 */
@Service
public class GenTableServiceImpl implements IGenTableService
{
    private static final Logger log = LoggerFactory.getLogger(GenTableServiceImpl.class);

    @Autowired
    private GenTableMapper genTableMapper;

    @Autowired
    private GenTableColumnMapper genTableColumnMapper;

    @Autowired
    private ISysMenuService sysMenuService;

    /**
     * 查询业务信息
     * 
     * @param id 业务ID
     * @return 业务信息
     */
    @Override
    public GenTable selectGenTableById(Long id)
    {
        GenTable genTable = genTableMapper.selectGenTableById(id);
        setTableFromOptions(genTable);
        return genTable;
    }

    /**
     * 查询业务列表
     * 
     * @param genTable 业务信息
     * @return 业务集合
     */
    @Override
    public List<GenTable> selectGenTableList(GenTable genTable)
    {
        return genTableMapper.selectGenTableList(genTable);
    }

    /**
     * 查询据库列表
     * 
     * @param genTable 业务信息
     * @return 数据库表集合
     */
    @Override
    public List<GenTable> selectDbTableList(GenTable genTable)
    {
        return genTableMapper.selectDbTableList(genTable);
    }

    /**
     * 查询据库列表
     * 
     * @param tableNames 表名称组
     * @return 数据库表集合
     */
    @Override
    public List<GenTable> selectDbTableListByNames(String[] tableNames)
    {
        return genTableMapper.selectDbTableListByNames(tableNames);
    }

    /**
     * 查询所有表信息
     * 
     * @return 表信息集合
     */
    @Override
    public List<GenTable> selectGenTableAll()
    {
        return genTableMapper.selectGenTableAll();
    }

    /**
     * 修改业务
     * 
     * @param genTable 业务信息
     * @return 结果
     */
    @Override
    @Transactional
    public void updateGenTable(GenTable genTable)
    {
        String options = JSON.toJSONString(genTable.getParams());
        genTable.setOptions(options);
        int row = genTableMapper.updateGenTable(genTable);
        if (row > 0)
        {
            for (GenTableColumn cenTableColumn : genTable.getColumns())
            {
                genTableColumnMapper.updateGenTableColumn(cenTableColumn);
            }
        }
    }

    /**
     * 删除业务对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional
    public void deleteGenTableByIds(String ids)
    {
        genTableMapper.deleteGenTableByIds(Convert.toLongArray(ids));
        genTableColumnMapper.deleteGenTableColumnByIds(Convert.toLongArray(ids));
    }

    /**
     * 创建表
     *
     * @param sql 创建表语句
     * @return 结果
     */
    @Override
    public boolean createTable(String sql)
    {
        return genTableMapper.createTable(sql) == 0;
    }

    @Override
    public void createMenu(String tableName) {
        // 查询表信息
        GenTable table = genTableMapper.selectGenTableByName(tableName);
        // 设置主子表信息
        setSubTable(table);
        // 设置主键列信息
        setPkColumn(table);
        //取父级菜单
        String options = table.getOptions();
        JSONObject paramsObj = JSON.parseObject(options);
        String parentMenuId = VelocityUtils.getParentMenuId(paramsObj);
        //保存菜单
        SysMenu sysMenu=new SysMenu();
        sysMenu.setMenuName(table.getFunctionName());
        sysMenu.setParentId(Long.parseLong(parentMenuId));
        sysMenu.setOrderNum("1");
        sysMenu.setUrl(StrUtil.format("/{}/{}",table.getModuleName(),table.getBusinessName()));
        sysMenu.setMenuType("C");
        sysMenu.setPerms(StrUtil.format("{}:view",VelocityUtils.getPermissionPrefix(table.getModuleName(), table.getBusinessName())));
        sysMenu.setRemark(StrUtil.format("{}菜单",table.getFunctionName()));
        sysMenu.setCreateBy(table.getCreateBy());
        sysMenuService.save(sysMenu);
        //保存权限
        List<SysMenu> sysMenuList= Lists.newArrayList();
        SysMenu queryMenu=new SysMenu();
        queryMenu.setMenuName(StrUtil.format("{}查询",table.getFunctionName()));
        queryMenu.setParentId(sysMenu.getMenuId());
        queryMenu.setOrderNum("1");
        queryMenu.setUrl("#");
        queryMenu.setMenuType("F");
        queryMenu.setPerms(StrUtil.format("{}:list",VelocityUtils.getPermissionPrefix(table.getModuleName(), table.getBusinessName())));
        queryMenu.setRemark(StrUtil.format("{}查询",table.getFunctionName()));
        sysMenuList.add(queryMenu);
        SysMenu insertMenu=new SysMenu();
        insertMenu.setMenuName(StrUtil.format("{}新增",table.getFunctionName()));
        insertMenu.setParentId(sysMenu.getMenuId());
        insertMenu.setOrderNum("2");
        insertMenu.setUrl("#");
        insertMenu.setMenuType("F");
        insertMenu.setPerms(StrUtil.format("{}:add",VelocityUtils.getPermissionPrefix(table.getModuleName(), table.getBusinessName())));
        insertMenu.setRemark(StrUtil.format("{}新增",table.getFunctionName()));
        sysMenuList.add(insertMenu);
        SysMenu editMenu=new SysMenu();
        editMenu.setMenuName(StrUtil.format("{}修改",table.getFunctionName()));
        editMenu.setParentId(sysMenu.getMenuId());
        editMenu.setOrderNum("3");
        editMenu.setUrl("#");
        editMenu.setMenuType("F");
        editMenu.setPerms(StrUtil.format("{}:edit",VelocityUtils.getPermissionPrefix(table.getModuleName(), table.getBusinessName())));
        editMenu.setRemark(StrUtil.format("{}修改",table.getFunctionName()));
        sysMenuList.add(editMenu);
        SysMenu deleteMenu=new SysMenu();
        deleteMenu.setMenuName(StrUtil.format("{}删除",table.getFunctionName()));
        deleteMenu.setParentId(sysMenu.getMenuId());
        deleteMenu.setOrderNum("4");
        deleteMenu.setUrl("#");
        deleteMenu.setMenuType("F");
        deleteMenu.setPerms(StrUtil.format("{}:remove",VelocityUtils.getPermissionPrefix(table.getModuleName(), table.getBusinessName())));
        deleteMenu.setRemark(StrUtil.format("{}删除",table.getFunctionName()));
        sysMenuList.add(deleteMenu);
        SysMenu exportMenu=new SysMenu();
        exportMenu.setMenuName(StrUtil.format("{}导出",table.getFunctionName()));
        exportMenu.setParentId(sysMenu.getMenuId());
        exportMenu.setOrderNum("5");
        exportMenu.setUrl("#");
        exportMenu.setMenuType("F");
        exportMenu.setPerms(StrUtil.format("{}:export",VelocityUtils.getPermissionPrefix(table.getModuleName(), table.getBusinessName())));
        exportMenu.setRemark(StrUtil.format("{}导出",table.getFunctionName()));
        sysMenuList.add(exportMenu);
        sysMenuService.saveBatch(sysMenuList);
    }

    /**
     * 导入表结构
     * 
     * @param tableList 导入表列表
     * @param operName 操作人员
     */
    @Override
    @Transactional
    public void importGenTable(List<GenTable> tableList, String operName) {
        try {
            for (GenTable table : tableList) {
                String tableName = table.getTableName();
                GenUtils.initTable(table, operName);
                int row = genTableMapper.insertGenTable(table);
                if (row > 0) {
                    // 保存列信息
                    List<GenTableColumn> genTableColumns = genTableColumnMapper.selectDbTableColumnsByName(tableName);
                    for (GenTableColumn column : genTableColumns) {
                        GenUtils.initColumnField(column, table);
                        genTableColumnMapper.insertGenTableColumn(column);
                    }
                }
            }
        } catch (Exception e) {
            throw new BusinessException("导入失败：" + e.getMessage());
        }
    }
    /**
     * 同步数据库
     *
     * @param tableName 表名称
     */
    @Override
    @Transactional
    public void synchDb(String tableName) {
        GenTable table = genTableMapper.selectGenTableByName(tableName);
        List<GenTableColumn> tableColumns = table.getColumns();
        List<String> tableColumnNames = tableColumns.stream().map(GenTableColumn::getColumnName).collect(Collectors.toList());

        List<GenTableColumn> dbTableColumns = genTableColumnMapper.selectDbTableColumnsByName(tableName);
        if (StringUtils.isEmpty(dbTableColumns)) {
            throw new BusinessException("同步数据失败，原表结构不存在");
        }
        List<String> dbTableColumnNames = dbTableColumns.stream().map(GenTableColumn::getColumnName).collect(Collectors.toList());

        dbTableColumns.forEach(column -> {
            if (!tableColumnNames.contains(column.getColumnName())) {
                GenUtils.initColumnField(column, table);
                genTableColumnMapper.insertGenTableColumn(column);
            }
        });

        List<GenTableColumn> delColumns = tableColumns.stream()
                .filter(column -> !dbTableColumnNames.contains(column.getColumnName())).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(delColumns)) {
            genTableColumnMapper.deleteGenTableColumns(delColumns);
        }
    }
    /**
     * 预览代码
     * 
     * @param tableId 表编号
     * @return 预览数据列表
     */
    @Override
    public Map<String, String> previewCode(Long tableId)
    {
        Map<String, String> dataMap = new LinkedHashMap<>();
        // 查询表信息
        GenTable table = genTableMapper.selectGenTableById(tableId);
        // 设置主子表信息
        setSubTable(table);
        // 设置主键列信息
        setPkColumn(table);
        VelocityInitializer.initVelocity();

        VelocityContext context = VelocityUtils.prepareContext(table);

        // 获取模板列表
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
        for (String template : templates)
        {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, Constants.UTF8);
            tpl.merge(context, sw);
            dataMap.put(template, sw.toString());
        }
        return dataMap;
    }

    /**
     * 生成代码（下载方式）
     * 
     * @param tableName 表名称
     * @return 数据
     */
    @Override
    public byte[] downloadCode(String tableName)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        generatorCode(tableName, zip);
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }
    
    /**
     * 生成代码（自定义路径）
     * 
     * @param tableName 表名称
     * @return 数据
     */
    @Override
    public void generatorCode(String tableName) {
        // 查询表信息
        GenTable table = genTableMapper.selectGenTableByName(tableName);
        // 设置主子表信息
        setSubTable(table);
        // 设置主键列信息
        setPkColumn(table);

        VelocityInitializer.initVelocity();

        VelocityContext context = VelocityUtils.prepareContext(table);

        // 获取模板列表
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
        for (String template : templates) {
            if (!StringUtils.contains(template, "sql.vm")) {
                // 渲染模板
                StringWriter sw = new StringWriter();
                Template tpl = Velocity.getTemplate(template, Constants.UTF8);
                tpl.merge(context, sw);
                try {
                    String path = getGenPath(table, template);
                    FileUtils.writeStringToFile(new File(path), sw.toString(), CharsetKit.UTF_8);
                } catch (IOException e) {
                    throw new BusinessException("渲染模板失败，表名：" + table.getTableName());
                }
            }
        }
    }

    public void genCodeProject(String tableName){
        // 查询表信息
        GenTable table = genTableMapper.selectGenTableByName(tableName);
        // 设置主子表信息
        setSubTable(table);
        // 设置主键列信息
        setPkColumn(table);

        VelocityInitializer.initVelocity();

        VelocityContext context = VelocityUtils.prepareContext(table);

        // 获取模板列表
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
        for (String template : templates) {
            if (!StringUtils.contains(template, "sql.vm")) {
                // 渲染模板
                StringWriter sw = new StringWriter();
                Template tpl = Velocity.getTemplate(template, Constants.UTF8);
                tpl.merge(context, sw);
                try {
                    String path = getProjectGenPath(table, template);
                    FileUtils.writeStringToFile(new File(path), sw.toString(), CharsetKit.UTF_8);
                } catch (Exception e) {
                    throw new BusinessException("渲染模板失败，表名：" + table.getTableName());
                }
            }
        }
    }
    /**
     * 批量生成代码
     * 
     * @param tableNames 表数组
     * @return 数据
     */
    @Override
    public byte[] downloadCode(String[] tableNames)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String tableName : tableNames)
        {
            generatorCode(tableName, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /**
     * 查询表信息并生成代码
     */
    private void generatorCode(String tableName, ZipOutputStream zip)
    {
        // 查询表信息
        GenTable table = genTableMapper.selectGenTableByName(tableName);
        // 设置主子表信息
        setSubTable(table);
        // 设置主键列信息
        setPkColumn(table);

        VelocityInitializer.initVelocity();

        VelocityContext context = VelocityUtils.prepareContext(table);

        // 获取模板列表
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
        for (String template : templates)
        {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, Constants.UTF8);
            tpl.merge(context, sw);
            try
            {
                // 添加到zip
                zip.putNextEntry(new ZipEntry(VelocityUtils.getFileName(template, table)));
                IOUtils.write(sw.toString(), zip, Constants.UTF8);
                IOUtils.closeQuietly(sw);
                zip.flush();
                zip.closeEntry();
            }
            catch (IOException e)
            {
                log.error("渲染模板失败，表名：" + table.getTableName(), e);
            }
        }
    }

    /**
     * 修改保存参数校验
     * 
     * @param genTable 业务信息
     */
    @Override
    public void validateEdit(GenTable genTable)
    {
        if (GenConstants.TPL_TREE.equals(genTable.getTplCategory()))
        {
            String options = JSON.toJSONString(genTable.getParams());
            JSONObject paramsObj = JSONObject.parseObject(options);
            if (StringUtils.isEmpty(paramsObj.getString(GenConstants.TREE_CODE)))
            {
                throw new BusinessException("树编码字段不能为空");
            }
            else if (StringUtils.isEmpty(paramsObj.getString(GenConstants.TREE_PARENT_CODE)))
            {
                throw new BusinessException("树父编码字段不能为空");
            }
            else if (StringUtils.isEmpty(paramsObj.getString(GenConstants.TREE_NAME)))
            {
                throw new BusinessException("树名称字段不能为空");
            }
        }
        else if (GenConstants.TPL_SUB.equals(genTable.getTplCategory()))
        {
            if (StringUtils.isEmpty(genTable.getSubTableName()))
            {
                throw new BusinessException("关联子表的表名不能为空");
            }
            else if (StringUtils.isEmpty(genTable.getSubTableFkName()))
            {
                throw new BusinessException("子表关联的外键名不能为空");
            }
        }
    }

    /**
     * 设置主键列信息
     * 
     * @param table 业务表信息
     */
    public void setPkColumn(GenTable table)
    {
        for (GenTableColumn column : table.getColumns())
        {
            if (column.isPk())
            {
                table.setPkColumn(column);
                break;
            }
        }
        if (StringUtils.isNull(table.getPkColumn()))
        {
            table.setPkColumn(table.getColumns().get(0));
        }
        if (GenConstants.TPL_SUB.equals(table.getTplCategory()))
        {
            for (GenTableColumn column : table.getSubTable().getColumns())
            {
                if (column.isPk())
                {
                    table.getSubTable().setPkColumn(column);
                    break;
                }
            }
            if (StringUtils.isNull(table.getSubTable().getPkColumn()))
            {
                table.getSubTable().setPkColumn(table.getSubTable().getColumns().get(0));
            }
        }
    }

    /**
     * 设置主子表信息
     * 
     * @param table 业务表信息
     */
    public void setSubTable(GenTable table)
    {
        String subTableName = table.getSubTableName();
        if (StringUtils.isNotEmpty(subTableName))
        {
            table.setSubTable(genTableMapper.selectGenTableByName(subTableName));
        }
    }

    /**
     * 设置代码生成其他选项值
     * 
     * @param genTable 设置后的生成对象
     */
    public void setTableFromOptions(GenTable genTable)
    {
        JSONObject paramsObj = JSONObject.parseObject(genTable.getOptions());
        if (StringUtils.isNotNull(paramsObj))
        {
            String treeCode = paramsObj.getString(GenConstants.TREE_CODE);
            String treeParentCode = paramsObj.getString(GenConstants.TREE_PARENT_CODE);
            String treeName = paramsObj.getString(GenConstants.TREE_NAME);
            String parentMenuId = paramsObj.getString(GenConstants.PARENT_MENU_ID);
            String parentMenuName = paramsObj.getString(GenConstants.PARENT_MENU_NAME);
            
            genTable.setTreeCode(treeCode);
            genTable.setTreeParentCode(treeParentCode);
            genTable.setTreeName(treeName);
            genTable.setParentMenuId(parentMenuId);
            genTable.setParentMenuName(parentMenuName);
        }
    }

    /**
     * 获取代码生成地址
     * 
     * @param table 业务表信息
     * @param template 模板文件路径
     * @return 生成地址
     */
    public static String getGenPath(GenTable table, String template)
    {
        String genPath = table.getGenPath();
        if (StringUtils.equals(genPath, "/"))
        {
            return System.getProperty("user.dir") + File.separator + "src" + File.separator + VelocityUtils.getFileName(template, table);
        }
        return genPath + File.separator + VelocityUtils.getFileName(template, table);
    }

    /**
     * 获取代码生成项目中的位置
     *
     * @param table 业务表信息
     * @param template 模板文件路径
     * @return 生成地址
     */
    public static String getProjectGenPath(GenTable table, String template) {
        return System.getProperty("user.dir") + VelocityUtils.getFileName(template, table);
    }
}