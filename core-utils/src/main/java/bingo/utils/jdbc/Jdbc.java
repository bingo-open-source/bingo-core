/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bingo.utils.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import bingo.lang.logging.Logger;
import bingo.lang.logging.LoggerFactory;

public final class Jdbc {
    
    private static final Logger log = LoggerFactory.getLogger(Jdbc.class);

    public static void close(Statement statement,boolean closeConnection){
        if(null != statement){
            try {
                statement.close();
            } catch (Throwable e) {
                log.warn("close statement error : {} ",e.getMessage(),e);
            }finally{
                if(closeConnection){
                    try {
                        close(statement.getConnection());
                    } catch (Throwable e) {
                        log.warn("close connection of statement error : {}",e.getMessage(),e);
                    }
                }
            }
        }
    }
    
    public static void close(ResultSet rs,boolean closeConnection) {
        if(null != rs){
            try {
                rs.close();
            } catch (SQLException e) {
                log.warn("close result set error : {} ",e.getMessage(),e);
            } finally {
                try {
                    close(rs.getStatement(),closeConnection);
                } catch (Throwable e) {
                    ;
                }
            }
        }
    }
    
    public static void close(Connection connection){
        if(null != connection){
            try{
                connection.close();
            }catch(Throwable e){
                log.warn("close connection error : {}",e.getMessage(),e);
            }
        }
    }
    
    public static <T> T execute(DataSource ds,JdbcCallbackWithResult<T> callback) throws JdbcException {
        Connection connection = null;
        try{
            connection = ds.getConnection();
            return callback.executeWithResult(connection);
        }catch(SQLException e) {
            throw new JdbcException(e);
        }finally{
            close(connection);
        }
    }
    
    public static <T> T execute(Connection connection,JdbcCallbackWithResult<T> callback) throws JdbcException {
        try{
            return callback.executeWithResult(connection);
        }catch(SQLException e) {
            throw new JdbcException(e);
        }
    }
}
