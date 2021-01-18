"# TestFramework"
Sample project with a file-based artifact test mechanism.  This is also using jacoco and codestyle

Maven command to test and invoke everything: mvn clean test site

This project should fail jacoco intentionally because of inadequate metrics.  A test is commented out
    and the enabling of this test will change the build results accordingly. 