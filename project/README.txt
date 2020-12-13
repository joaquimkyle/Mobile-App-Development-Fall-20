todoro
Kyle Joaquim (Kyle_Joaquim@student.uml.edu)
COMP4630

[Statement]

For my project, I proposed an app for managing tasks through checklists and facilitating the completion of those tasks with an implementation of the Pomodoro method, a time-based productivity technique. There are, of course, endless formulations of a productivity app, as each person has their own optimal system in this regard. However, I did not decide upon my app's functionality by comparison with others. Rather, I wanted to select a generic sort of app with which I would never struggle to know what to work on next. Productivity apps have straightforward functionalities, which would allow me to spend more time learning what works and what doesn't in arranging a UI. In other words, I wanted to work on something I would be able to experience from a user perspective. This is why I chose some productivity tools I personally like to implement. 

I find the experience I've gained with Google Firebase and Firebase UI very valuable. These tools simplify a great deal of the development process. I regret, though, not choosing to write my application with Kotlin. Through exposure to much documentation comparing Java and Kotlin, I can guess that the additional benefits Kotlin provides would have outweighed its learning curve.

[Installation]

An .apk executable is provided at \project\app\build\outputs\apk\debug\app-debug.apk
You can rebuild that executable, if necessary and so long as you have a JDK installation, by navigating to the root directory of the project in a command line and running 'gradlew assembleDebug'

[Instructions]

On opening the application, you should be presented with a sign-in interface. You can use several methods to create an account or proceed as a guest. Once signed in, you'll find a three tab navigation bar at the bottom of the app. The "Work" and "Routine" sections of the app are not yet available. 

On the "Tasks" tab, you can press the Floating Action Button in the bottom-right hand corner of the screen to create a new Task. You'll be greeted by a dialog. You must give the Task a name and you have the option of adding a description or adjusting the Task's priority from its default value. When all fields are satisfied, select 'SAVE' from the menu bar of the dialog. You'll see your new Task added in a Card. 

If you tap the options menu beside a Task - the button is on the right-hand side where the priority is indicated - you'll have the options of Editing or Deleting the Task. Editing a Task presents a Dialog, but there's a bug here: renamed Tasks will lose their SubTasks.

If you tap on a Task Card, that Task's SubTask list will open. Here, you can use the Floating Action Button to create a new SubTask. Once the SubTask is complete, you can mark it so with the checkbox. A long press on any SubTask will give the options of Editing or Deleting the SubTask. To return to your Tasks, use the navigational X on the top menu bar.

From the three main tabs, the top action bar's overflow menu will give you the option to Sign Out. This will recreate the Activity and return you to the sign-in interface. If you are signed in as a Guest, the app will present you with a dialog asking you to convert your account to a permenant one, or else lose your data. If you choose, you can provide the app with an e-mail and password, which it will link to your existing data.




