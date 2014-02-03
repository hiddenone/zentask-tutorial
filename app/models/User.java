package models;

import javax.persistence.*;
import play.db.ebean.*;
import com.avaje.ebean.*;

@Entity
public class User extends Model {

    @Id
    public String email;
    public String name;
    public String password;
    
    public User(String email, String name, String password) {
      this.email = email;
      this.name = name;
      this.password = password;
    }

    public static Finder<String,User> find = new Finder<String,User>(
        String.class, User.class
    ); 


/*Although we could use the find object from anywhere in our code to create queries for users, it’s 
not good practice to spread that code all through our application. One such query that we need is a query 
that will authenticate users. In User.java, add the authenticate() method:
*/
    public static User authenticate(String email, String password) {
        return find.where().eq("email", email)
            .eq("password", password).findUnique();
    }

}

/* The @Entity annotation marks this class as a managed Ebean entity, and the Model superclass automatically provides a set of useful JPA helpers that we will discover later. All fields of this class will be automatically persisted to the database.
It’s not required that your model objects extend the play.db.ebean.Model class. You can use plain Ebean as well. But extending this class is a good choice in most 
cases as it will make a lot of the Ebean stuff easier. If you have used JPA before, you know that every JPA entity must provide an @Id property. 
In this case, we are choosing email to be the id field.

The find field will be used to programatically make queries, which we will see later.

Now if you’re a Java developer with any experience at all, warning sirens are probably clanging like mad at the sight of a public variable. In Java (as in other 
object-oriented languages), best practice says to make all fields private and provide accessors and mutators. This is to promote encapsulation, 
a concept critical to object oriented design. In fact, play takes care of that for you and automatically generates getters and setters while
 preserving encapsulation; we will see how it works later in this tutorial.

You can now refresh the application homepage. This time you should see something different: */