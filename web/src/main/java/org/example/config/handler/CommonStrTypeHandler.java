package org.example.config.handler;

import org.example.utils.AESEncryptor;
import org.example.utils.AESEncryptorNew;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 通用加密类型
 */
public class CommonStrTypeHandler extends BaseTypeHandler<String> {

    @Override
    public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        if (StringUtils.isBlank(parameter)) {
            if (jdbcType == null) {
                throw new TypeException("JDBC requires that the JdbcType must be specified for all nullable parameters.");
            }
            try {
                ps.setNull(i, jdbcType.TYPE_CODE);
            } catch (SQLException var7) {
                throw new TypeException("Error setting null for parameter #" + i + " with JdbcType " + jdbcType + " . Try setting a different JdbcType for this parameter or a different jdbcTypeForNull configuration property. Cause: " + var7, var7);
            }
        } else {
            try {
                String encrypt;
                try {
                    encrypt = AESEncryptorNew.encrypt(parameter);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                ps.setString(i, encrypt);
            } catch (Exception var6) {
                throw new TypeException("Error setting non null for parameter #" + i + " with JdbcType " + jdbcType + " . Try setting a different JdbcType for this parameter or a different configuration property. Cause: " + var6, var6);
            }
        }
    }

    /**
     * 按照列名提取数据
     */
    @Override
    public String getResult(ResultSet resultSet, String s) throws SQLException {
        String decrypt = resultSet.getString(s);
        String tmp = decrypt;
        if (StringUtils.isNotBlank(decrypt)) {
            try {
                tmp = AESEncryptor.decrypt(decrypt);
            } catch (Exception ignored) {

            }
        }
        return tmp;
    }

    /**
     * 按照列索引提取数据
     */
    @Override
    public String getResult(ResultSet resultSet, int i) throws SQLException {
        String decrypt = resultSet.getString(i);
        String tmp = decrypt;
        if (StringUtils.isNotBlank(decrypt)) {
            try {
                tmp = AESEncryptor.decrypt(decrypt);
            } catch (Exception ignored) {

            }
        }
        return tmp;
    }

    @Override
    public String getResult(CallableStatement callableStatement, int i) throws SQLException {
        String decrypt = callableStatement.getString(i);
        String tmp = decrypt;
        if (StringUtils.isNotBlank(decrypt)) {
            try {
                tmp = AESEncryptor.decrypt(decrypt);
            } catch (Exception ignored) {

            }
        }
        return tmp;
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, String s, JdbcType jdbcType) throws SQLException {
        // do nothing
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        // do nothing
        return "";
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        // do nothing
        return "";
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        // do nothing
        return "";
    }
}
