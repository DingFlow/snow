package com.snow.framework.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-04-17 12:17
 **/
@MappedTypes(String[].class)
@MappedJdbcTypes({JdbcType.VARCHAR})
@Slf4j
public class JsonStringArrayTypeHandler extends BaseTypeHandler<String[]> {
    private static final ObjectMapper mapper=new ObjectMapper();

    /**
     * 将数组以字符串的形式存在数据库
     * @param preparedStatement
     * @param i
     * @param parameter
     * @param jdbcType
     * @throws SQLException
     */
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, String[] parameter, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i,toJson(parameter));
    }

    @Override
    public String[] getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        log.info("读取数据库的数据columnName:{}",resultSet.getString(columnName));
        return this.toObject(resultSet.getString(columnName));
    }

    @Override
    public String[] getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return  this.toObject(resultSet.getString(columnIndex));
    }

    @Override
    public String[] getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        return this.toObject(callableStatement.getString(columnIndex));
    }

    private String toJson(String[] parameter){
        try {
            return mapper.writeValueAsString(parameter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "[]";
    }

    private String[] toObject(String content){
        if(content!=null&&!content.isEmpty()){
            try {
                return(String[]) mapper.readValue(content,String[].class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else {
            return null;
        }
    }
}
