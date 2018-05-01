# How to clone the project

    git clone --recursive <address>

If you forgot to use --recursive, you can use:

    git submodule init
    git submodule update

Now, you need to teach NetBeans how to use it. Open the project in NetBeans (the directory "ToF"),
navigate to its libraries, "minimal-json" should appear. Double-click it, a new project appears in
the list (minimal-json), right-click on it, select "resolve project problems", then "resolve".
This will prompt you a path. Go to the directory in which you cloned the project, then
`json/com.eclipsesource.json/src/main/java` (choose the 'java' directory!). Now, the source files
should appear in the project's tree. Select one (any), hit "compile". You can now close the
minimal-json project, everything is initialized. You can go back to the project "Tombs of the 
Forgotten" and build it/run it...
