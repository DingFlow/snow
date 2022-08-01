package com.snow.system.handler;

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
import java.util.List;

@MappedTypes(List.class)
@MappedJdbcTypes({JdbcType.VARCHAR})
@Slf4j
public class JsonStringListTypeHandler extends BaseTypeHandler<List> {

    private static final ObjectMapper mapper=new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List parameter, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i,toJson(parameter));
    }

    @Override
    public List getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return this.toObject(resultSet.getString(columnName));
    }

    @Override
    public List getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return  this.toObject(resultSet.getString(columnIndex));
    }

    @Override
    public List getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        return this.toObject(callableStatement.getString(columnIndex));
    }

    private String toJson(List parameter){
        try {
            return mapper.writeValueAsString(parameter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "[]";
    }

    private List toObject(String content){
        if(content!=null&&!content.isEmpty()){
            try {
                return(List) mapper.readValue(content,List.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else {
            return null;
        }
    }
}
