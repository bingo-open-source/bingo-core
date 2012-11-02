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
package bingo.lang.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import bingo.lang.ThrowableAction1;
import bingo.lang.ThrowableFunc1;
import bingo.lang.exceptions.UncheckedSQLException;
import bingo.lang.logging.Log;
import bingo.lang.logging.LogFactory;

public class JDBC {
    
    private static final Log log = LogFactory.get(JDBC.class);
    
    protected JDBC(){
    	
    }
    
    public static void execute(DataSource ds,ThrowableAction1<Connection> action) throws UncheckedSQLException {
        Connection connection = null;
        try{
            connection = ds.getConnection();
            
            action.execute(connection);
        }catch(Throwable e) {
            throw new UncheckedSQLException(e.getMessage(),e);
        }finally{
            close(connection);
        }
    }
    
    public static void execute(Connection connection,ThrowableAction1<Connection> action) throws UncheckedSQLException {
    	execute(connection,action,true);
    }    
    
    public static void execute(Connection connection,ThrowableAction1<Connection> action,boolean closeConnection) throws UncheckedSQLException {
        try{
            action.execute(connection);
        }catch(Throwable e) {
            throw new UncheckedSQLException(e.getMessage(),e);
        }finally{
        	if(closeConnection){
        		close(connection);
        	}
        }
    }
    
    public static <R> R execute(DataSource ds,ThrowableFunc1<Connection,R> func) throws UncheckedSQLException {
        Connection connection = null;
        try{
            connection = ds.getConnection();
            
            return func.apply(connection);
        }catch(Throwable e) {
            throw new UncheckedSQLException(e.getMessage(),e);
        }finally{
            close(connection);
        }
    }
    
    public static <R> R execute(Connection connection,ThrowableFunc1<Connection,R> func) throws UncheckedSQLException {
    	return execute(connection,func,true);
    }    
    
    public static <R> R execute(Connection connection,ThrowableFunc1<Connection,R> func,boolean closeConnection) throws UncheckedSQLException {
        try{
            return func.apply(connection);
        }catch(Throwable e) {
            throw new UncheckedSQLException(e.getMessage(),e);
        }finally{
        	if(closeConnection){
        		close(connection);
        	}
        }
    }

    public static void close(Statement statement,boolean closeConnection){
        if(null != statement){
            try {
                statement.close();
            } catch (Throwable e) {
                log.warn("Error closing statement : {} ",e.getMessage(),e);
            }finally{
                if(closeConnection){
                    try {
                        close(statement.getConnection());
                    } catch (Throwable e) {
                        log.warn("Error closing connection of statement : {}",e.getMessage(),e);
                    }
                }
            }
        }
    }
    
    public static void closeStatementOnly(Statement statement){
    	if(null != statement){
    		try{
    			statement.close();	
    		}catch(Throwable e){
    			log.warn("Error closing statement : {} ",e.getMessage(),e);
    		}
    	}
    }
    
    public static void closeResultSetOnly(ResultSet rs){
    	if(null != rs){
    		try{
    			rs.close();	
    		}catch(Throwable e){
    			log.warn("Error closing result set : {} ",e.getMessage(),e);
    		}
    	}
    }
    
    public static void close(ResultSet rs,boolean closeConnection) {
        if(null != rs){
            try {
                rs.close();
            } catch (Throwable e) {
                log.warn("Error closing result set : {} ",e.getMessage(),e);
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
                log.warn("Error closing connection : {}",e.getMessage(),e);
            }
        }
    }
}
