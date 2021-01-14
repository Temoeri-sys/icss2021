:: Clean the project
call mvn clean

:: G4 might be changed so make sure to update.
call mvn generate-resources

:: Compile the project
call mvn compile

:: Run the project
call mvn exec:java