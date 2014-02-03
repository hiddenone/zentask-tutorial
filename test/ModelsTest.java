package models;

import models.*;
import org.junit.*;
import static org.junit.Assert.*;
import play.test.WithApplication;
import static play.test.Helpers.*;
import java.util.List;  //Necessary for Projects Test cause of <List>

////////////////////////
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import play.test.FakeApplication;
import play.test.Helpers;

import com.avaje.ebean.Ebean;

/////////////////////////
 // git tag -a v1.0 -m 'End of the javaguide2: http://www.playframework.com/documentation/2.1.x/JavaGuide2
//????????????????????????????
import play.*;
import play.libs.*;

import java.util.*;

import com.avaje.ebean.*;

////import models.*;
//????????????????????????????

public class ModelsTest extends WithApplication {
    @Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase()));
    }
	
	
/////////////////////// First TEST ////////////////////////
/* We have extended the WithApplication class. This is optional, it provides the start() method that allows us to easily start a fake application, and it 
automatically cleans it up after each test has run. You could manage these yourself, but we are going to let Play manage it for us.

We have also implemented a @Before annotated method. This annotation means that this method will be run before each test.
In our case we are starting a new FakeApplication, and configuring this application to use a new in memory database. Because we are using an in memory database,
 we don’t need to worry about clearing the database before each test, since a new clean database is created for us before each test.

Now we will write our first test, which is just going to check that we can insert a row, and retrieve it again: */

    @Test
    public void createAndRetrieveUser() {
        new User("bob@gmail.com", "Bob", "secret").save();
        User bob = User.find.where().eq("email", "bob@gmail.com").findUnique();
        assertNotNull(bob);
        assertEquals("Bob", bob.name);
    }
/* 	You can see that we have programatically created a query using the User.find finder, to find a unqiue instance where email is equal to 
Bob’s email address.

To run this test case, make sure that you have stopped the running application by pressing Ctrl+D in the play console, and then run test. 
The test should pass. */

/*And now the test case:*/

    @Test
    public void tryAuthenticateUser() {
        new User("bob@gmail.com", "Bob", "secret").save();
        
        assertNotNull(User.authenticate("bob@gmail.com", "secret"));
        assertNull(User.authenticate("bob@gmail.com", "badpassword"));
        assertNull(User.authenticate("tom@gmail.com", "secret"));
    }

/*Each time you make a modification you can run all the tests from the play test runner to make sure you didn’t break 
anything.

The above authentication code stores the password in cleartext. This is considered very bad practice, you should hash the 
    password before storing it, and then hash it before running the query, but that is beyond the scope of this tutorial.
*/

////////////////////////   Projects Test ///////////////////////
    //Now we will write a new test in our ModelsTest class to test our Project class and the query with it:

    @Test
    public void findProjectsInvolving() {
        new User("bob@gmail.com", "Bob", "secret").save();
        new User("jane@gmail.com", "Jane", "secret").save();

        Project.create("Play 2", "play", "bob@gmail.com");
        Project.create("Play 1", "play", "jane@gmail.com");

        List<Project> results = Project.findInvolving("bob@gmail.com");
        assertEquals(1, results.size());
        assertEquals("Play 2", results.get(0).name);
    }

    //Don’t forget to import java.util.List or you will get a compilation error. for the above findProjectsInvolving test

    // This is also included in the task model:

/*Each task has a generated id, a title, a flag to say whether it is done or not, a date that it must be completed by, a user it is 
assigned to, a folder and a project. The assignedTo and project relationships are mapped using @ManyToOne. This means a task may 
have one user, and one project, while each user may have many tasks assigned to them, and each project may have many tasks.

We also have a simple query, this time finding all the todo tasks, that is, those tasks that aren’t done, assigned to a 
particular user, and a create method.

*/    //Let’s write a test for this class as well.

    @Test
    public void findTodoTasksInvolving() {
        User bob = new User("bob@gmail.com", "Bob", "secret");
        bob.save();

        Project project = Project.create("Play 2", "play", "bob@gmail.com");
        Task t1 = new Task();
        t1.title = "Write tutorial";
        t1.assignedTo = bob;
        t1.done = true;
        t1.save();

        Task t2 = new Task();
        t2.title = "Release next version";
        t2.project = project;
        t2.save();

        List<Task> results = Task.findTodoInvolving("bob@gmail.com");
        assertEquals(1, results.size());
        assertEquals("Release next version", results.get(0).title);
    }

    ///// Finally the Full test which should really be in the @Before, so hmmm what does that mean?????


/*  Edit the conf/test-data.yml file and start to describe a User:

- !!models.User
    email:      bob@gmail.com
    name:       Bob
    password:   secret
...

Notice that this object is defined as part of a root object that is a list. We can now define more objects to be a part of that, 
however, our dataset is a little large, so you can download a full dataset here:
http://www.playframework.com/documentation/2.1.x/resources/manual/javaGuide/tutorials/zentasks/files/test-data.yml

Now we create a test case that loads this data and runs some assertions over it:*/

    @Test
    public void fullTest() {
        Ebean.save((List) Yaml.load("test-data.yml"));

        // Count things
        assertEquals(3, User.find.findRowCount());
        assertEquals(7, Project.find.findRowCount());
        assertEquals(5, Task.find.findRowCount());

        // Try to authenticate as users
        assertNotNull(User.authenticate("bob@example.com", "secret"));
        assertNotNull(User.authenticate("jane@example.com", "secret"));
        assertNull(User.authenticate("jeff@example.com", "badpassword"));
        assertNull(User.authenticate("tom@example.com", "secret"));

        // Find all Bob's projects
        List<Project> bobsProjects = Project.findInvolving("bob@example.com");
        assertEquals(5, bobsProjects.size());

        // Find all Bob's todo tasks
        List<Task> bobsTasks = Task.findTodoInvolving("bob@example.com");
        assertEquals(4, bobsTasks.size());
    }

    
}


// We will add a fixture to test(see http://www.playframework.com/documentation/2.1.x/JavaGuide2), but where should that go and 
//what is that necessary for?
