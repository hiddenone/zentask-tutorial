/*The last thing that we need for our model draft, and most important thing, is tasks.
*/
package models;

import java.util.*;
import javax.persistence.*;
import play.db.ebean.*;

@Entity
public class Task extends Model {

    @Id
    public Long id;
    public String title;
    public boolean done = false;
    public Date dueDate;
    @ManyToOne
    public User assignedTo;
    public String folder;
    @ManyToOne
    public Project project;

    public static Model.Finder<Long,Task> find = new Model.Finder(Long.class, Task.class);

    public static List<Task> findTodoInvolving(String user) {
       return find.fetch("project").where()
                .eq("done", false)
                .eq("project.members.email", user)
           .findList();
    }

    public static Task create(Task task, Long project, String folder) {
        task.project = Project.find.ref(project);
        task.folder = folder;
        task.save();
        return task;
    }
}

/*Each task has a generated id, a title, a flag to say whether it is done or not, a date that it must be completed by, a user 
it is assigned to, a folder and a project. The assignedTo and project relationships are mapped using @ManyToOne. This means a task may
 have one user, and one project, while each user may have many tasks assigned to them, and each project may have many tasks.

We also have a simple query, this time finding all the todo tasks, that is, those tasks that aren’t done, assigned to a particular 
user, and a create method.*/