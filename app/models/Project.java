/*The Project class will represent projects that tasks can be a part of. A project also has a list of members that can be 
assigned to tasks in the project. Let’s do a first implementation:
*/
package models;

import java.util.*;
import javax.persistence.*;
import play.db.ebean.*;

@Entity
public class Project extends Model {

    @Id
    public Long id;
    public String name;
    public String folder;
    @ManyToMany(cascade = CascadeType.REMOVE)
    public List<User> members = new ArrayList<User>();

    public Project(String name, String folder, User owner) {
        this.name = name;
        this.folder = folder;
        this.members.add(owner);
    }

    public static Model.Finder<Long,Project> find = new Model.Finder(Long.class, Project.class);

    public static Project create(String name, String folder, String owner) {
        Project project = new Project(name, folder, User.find.ref(owner));
        project.save();
        project.saveManyToManyAssociations("members");
        return project;
    }

    public static List<Project> findInvolving(String user) {
        return find.where()
            .eq("members.email", user)
            .findList();
    }
}
/* A project has a name, a folder that it belongs to, and members. This time you can see that we again have the @Entity annotation 
on the class, extending Model, @Id on our id field and find for running queries. We have also declared a relation with the User class, 
declaring it as @ManyToMany. This means that each user can be member of many projects, and each project can have many users.

We have also implemented a create method. Note that the many to many members association has to be saved explicitly. Note also that
 we never actually assign the id property. This is because we are going to let the database generate an id for us.

Finally we have implemented another query method, one that finds all projects involving a particular user. 
You can see how the dot notation has been used to refer to the email property of User in the members list.*/