package DAO

import domain.User
import java.sql.*
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.DriverManager.getConnection




object UserDAO {

    fun findUser(id: Long): User? {
        var rs: ResultSet? = null;
        var statement: PreparedStatement? = null
        var connection: Connection? = null

        try {
            connection = getConnection("jdbc:postgresql://localhost/myfitness", "liinatuttar",
                    "")
            val sql = "select * from users where id=?"
            val statement = connection?.prepareStatement(sql)
            statement!!.setLong(1, id)
            rs = statement.executeQuery()

            return if (!rs.next()) {
                null
            } else {
                readUser(rs)
            }

        } catch (e: SQLException) {
            throw RuntimeException(e)
        } finally {
            dbClose(rs, statement, connection)
        }
    }

    private fun readUser(rs: ResultSet): User {
        val id = rs.getLong("id")
        val name = rs.getString("name")
        val birthday = rs.getDate("birthday")
        val join_date = rs.getDate("join_date")
        val user = User(id, name, birthday.toLocalDate(), join_date.toLocalDate())
        return user
    }

    fun createUser(user: User): Boolean {
        var statement: PreparedStatement? = null
        var connection: Connection? = null
        try {
            connection = getConnection("jdbc:postgresql://localhost/myfitness", "liinatuttar",
                    "")

            val sql = ("insert into users " + "(name, birthday, join_date) "

                    + "values (?, ?, ?)")
            statement = connection!!.prepareStatement(sql)
            statement.setString(1, user.name)
            statement.setDate(2, Date.valueOf(user.birthday))
            statement.setDate(3, Date.valueOf(user.join_date))
            return if(statement.executeUpdate() > 0) true else false
        } catch (e: SQLException) {
            throw RuntimeException(e)
        } finally {
            dbClose(null, statement, connection)
        }
    }

    /**
     * Get the highest id+1 from the users table add 1
     *
     * @return  id+1 or null
     */

    fun getUniqueId(): Long? {
        var connection: Connection? = null
        var stmt: Statement? = null
        var rs: ResultSet? = null

        try {
            connection = getConnection("jdbc:postgresql://localhost/myfitness", "liinatuttar",
                    "")
            val query = "select MAX(id) from user"
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                return rs.getLong("id")+1
            }
        } catch (e: Exception) {
            println("Couldn't get unique id")
        } finally {
            dbClose(rs, stmt, connection)
        }
        return null
    }

    fun updateUser(user: User, id: Long): Boolean {
        var statement: PreparedStatement? = null
        var connection: Connection? = null
        try {
            connection = getConnection("jdbc:postgresql://localhost/myfitness", "liinatuttar",
                    "")
            val sql = "UPDATE users SET name=?, birthday =?, join_date=? where id=?"
            statement = connection!!.prepareStatement(sql)

            statement!!.setString(1, user.name)
            var birthdaySql = Date.valueOf(user.birthday)
            var join_dateSql = Date.valueOf(user.join_date)
            statement.setDate(2, birthdaySql)
            statement.setDate(3, join_dateSql)
            statement.setLong(4, id)
            return statement.executeUpdate() > 0
        } catch (e: SQLException) {
            throw RuntimeException(e)
        } finally {
            dbClose(null, statement, connection)
        }
    }

    fun deleteUser(id: Int): Boolean {
        var statement: PreparedStatement? = null
        var connection: Connection? = null
        try {
            connection = getConnection("jdbc:postgresql://localhost/myfitness", "liinatuttar", "")
            val sql = "delete from users where id=?"
            statement = connection!!.prepareStatement(sql)
            statement!!.setLong(1, id.toLong())
            return statement.executeUpdate() > 0
        } catch (e: SQLException) {
            throw RuntimeException(e)
        } finally {
            dbClose(null, statement, connection)
        }
    }

    private fun dbClose(rs: ResultSet?, st: Statement?, conn: Connection?) {
        rs?.close()
        st?.close()
        conn?.close()
    }
}