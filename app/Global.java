/*
Bootstrapping with default data

In fact before coding the first screen we need one more thing. Working on a web application without test data is not fun. You can’t 
even test what you’re doing. But because we haven’t developed the screens for managing tasks yet, we can’t populate the task 
dashboard with tasks ourselves.

One way to inject default data into the blog is to load a YAML file at application load time, the same way we did for testing. To do 
that we will hook into Plays startup to bootstrap the application with data. Hooking into Plays startup is as simple as creating a 
class called Global that implements GlobalSettings in the root package, and overriding the onApplicationStart() method. Let’s do 
that now, by creating the app/Global.java file:
*/
import play.*;
import play.libs.*;
import com.avaje.ebean.Ebean;
import models.*;
import java.util.*;

public class Global extends GlobalSettings {
    @Override
    public void onStart(Application app) {
        // Check if the database is empty
        if (User.find.findRowCount() == 0) {
            Ebean.save((List) Yaml.load("initial-data.yml"));
        }
    }
}

/*
Now this will be called whenever play starts up.

    In fact this job will be run differently in dev or prod modes. In dev mode, play waits for a first request to start. So this job 
    will be executed synchronously at the first request. That way, if the job fails, you will get the error message in your browser. 
    In prod mode however, the job will be executed at application start (synchronously with the start command) and will prevent the 
    application from starting in case of an error.

You have to create an initial-data.yml in the conf directory. You can of course reuse the test-data.yml content that we just used 
for tests previously.

Now run the application using play run and display the http://localhost:9000 page in the browser.

*/