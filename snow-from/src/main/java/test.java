/*

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
*/
/**
 * @program: csj-shark
 * @description: 自定义TypeHandler 把数据库的String和JAVA的String[]相互转换
 * @author: 阿吉
 * @create: 2021-03-19 09:00
 *//*

@MappedTypes(String[].class)
@MappedJdbcTypes({JdbcType.VARCHAR})
@Slf4j
public class test extends BaseTypeHandler<String[]> {
    private static final ObjectMapper mapper=new ObjectMapper();
    */
/**
     * 将数组以字符串的形式存在数据库
     * @param preparedStatement
     * @param i
     * @param parameter
     * @param jdbcType
     * @throws SQLException
     *//*

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, String[] parameter, JdbcType jdbcType) throws SQLException {
        log.info("存入数据库的数组数据:{}",toJson(parameter));
        preparedStatement.setString(i,toJson(parameter));
    }

    @Override
    public String[] getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        log.info("读取数据库的数据columnName:{}",resultSet.getString(columnName));
        return this.toObject(resultSet.getString(columnName));
    }

    @Override
    public String[] getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        log.info("读取数据库的数据columnIndex:{}",resultSet.getString(columnIndex));
        return  this.toObject(resultSet.getString(columnIndex));
    }

    @Override
    public String[] getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        log.info("读取数据库的数据callableStatement:{}",callableStatement.getString(columnIndex));
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
}*/
