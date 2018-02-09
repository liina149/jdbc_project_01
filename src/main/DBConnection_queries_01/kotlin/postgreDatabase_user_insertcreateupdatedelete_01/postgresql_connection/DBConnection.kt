package postgresql_connection

import java.sql.*

public class DBConnection {

     var con: Connection? = null

    fun connect() {
        Class.forName("net.sourceforge.jtds.jdbc.Driver")
        con = DriverManager.getConnection(
                "jdbc:postgresql://localhost/myfitness", "liinatuttar",
                "")
    }

    fun selectQuery(query: String): ResultSet {
        connect()
        println("Connection established")
        var st: Statement = con!!.createStatement()
        var rs: ResultSet = st.executeQuery(query)
        return rs
    }

    fun insertUpdateDeleteQuery(query: String): Boolean {
        connect()
        var st: Statement = con!!.createStatement()
        disconnect()
        return  if(st.executeUpdate(query) > 0) true else false
    }



    fun disconnect() {
        if(con != null) con!!.close();
    }


}