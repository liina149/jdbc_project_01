import DAO.UserDAO
import domain.User
import java.sql.Date
import java.time.LocalDate

fun main(args: Array<String>) {

    // INSERT USER
    var joinDate: LocalDate = LocalDate.now()
    var dateSql: Date = Date.valueOf(joinDate)
    var birthday: LocalDate =  LocalDate.of(2018, 2, 18)
    val birthdaySql: Date = Date.valueOf(birthday)
    var user: User = User(null, "liina", birthday, joinDate )
    if(UserDAO.createUser(user)) println("User created") else println("Couldn't create a user")


    // FIND USER FROM DATABASE
    if(UserDAO.findUser( 1) != null) {
        println("Name is ${user?.name}/n birthday is ${user?.birthday}/n joined at ${user?.join_date}")
    } else {
        println("Sellise id-ga kasutajat ei leia")
    }

    // DELETE USER
    if(UserDAO.deleteUser(2))  println("user is deleted") else println("Couldn't delete user")


    // UPDATE USER

    var idToChange: Long = 2
    var findUser = UserDAO.findUser(idToChange)

    if(findUser != null) {
        findUser.id = idToChange
        findUser!!.name = "Janek"
        findUser!!.birthday = LocalDate.of(1977, 6, 14)
        findUser!!.join_date = LocalDate.now().plusDays(1)

        //if (UserDAO.updateUser(user, idToChange)) println("User updated") else println("Problems with updating the user")
        if(UserDAO.updateUser(findUser, idToChange)) println("Update successful") else println("Couldn't update the user")
    }



}