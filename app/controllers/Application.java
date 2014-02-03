package controllers;
import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import models.*;
import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render( 
            Project.find.all(),
            Task.find.all()
        )); 
    }
}
//  ??????  First major modification : 
/*The dashboard

This time, we can really start to code the dashboard.

Do you remember how the first page is displayed? First the routes file defines that the / URL will invoke the controllers.Application.index() action method. Then this method calls render() and executes the app/views/Application/index.scala.html template.

We will keep these components but add code to them to load the tasks list and display them.

Open the app/controllers/Application.java file, and modify the index() action to load the projects and tasks, like so:
package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import models.*;
import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render( 
            Project.find.all(),
            Task.find.all()
        )); 
    }
}

Can you see how we pass objects to the render method? If you try and run this now, you’ll find you get a compiler error, because 
if you remember, our index template only accepted one parameter, that being a String, but now we are passing a list of Project and
 a list of Task.
<<<< In fact here is the error:
render(java.lang.String) in views.html.index cannot be applied to (java.util.List<models.Project>,java.util.List<models.Task>) 
>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

*/