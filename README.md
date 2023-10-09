How to run the program:

1 Method:
- open terminal or cmd
- execute this command:
java --module-path "{path of the javafx sdk}\lib" --add-modules=javafx.controls,javafx.fxml,javafx.swing,javafx.base,javafx.graphics,javafx.media,javafx.web -jar {path of the file path.jar}

!! Pay attention to the corrects paths !!

2 Method:
- Open the project in eclips or any other ide
- click on the package main then the class MainActivity.java
- modify the VM arguments of the class by this:
--module-path "{path of the javafx sdk}\lib" --add-modules=javafx.controls,javafx.fxml,javafx.swing,javafx.base,javafx.graphics,javafx.media,javafx.web
- run

How to open the javadoc:

1 Method:

-click on the file index.html - Shortcut

2 Method:
- enter the folder doc
- search for the file index.html and click on it


How to generate the javadoc:
- for eclipse, open the project in the workspace
- in the upper bar search for "project"
- then click on Generate JavaDoc
- follow the instructions then click on finish
- follow the 2 Method to open the generated javadoc

