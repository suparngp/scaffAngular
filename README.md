ScaffAngular (angular-iunit)
=============

An IntelliJ Unit Test Manager Plugin for Angular JS Modules

What is it about?
--------

This plugin aims at following three core issues:

1. Standardizing the naming convention of angular components across the application
2. Bootstrapping and managing the test files right from the component's source file
3. Bootstrapping documentation for the components and their respective test scenarios

How it works?
--------

The plugin presents a very simple wizard to create angular modules. The wizard asks for only 3 key pieces of information:

1. Component's type, e.g. service or a controller
2. Components's name, e.g. Main for MainCtrl or panel for panelDirective
3. Module's name, e.g. app.home for angular.module("app.home")

With this information available, the plugin 

1. creates the source and the test files with the appropriate suffix. e.g. MainCtrl.js and MainCtrlSpec.js
2. Adds the ngdocs based documentation to the component's source file, e.g. MainCtrl.js.
3. Bootstraps the controller with the basic template
4. Adds jsdocs documentation to the test spec file, e.g. MainCtrlSpec.js
5. Bootstraps the basic instantiation test! You don't need to write the instantiation tests anymore! Yay! (saves hell lot of time and great for basic test coverage)


<blockquote>
For Example, to create an angular controller with the name SillyCtrl.js, under the module name app.insane:

    <ol>
        <li>Select component type as controller</li>
        <li>Enter "Silly" in file name - without quotes of course</li>
        <li>Enter "app.insane" in module name - without quotes of course</li>
    </ol>

This will bootstrap the required component.
</blockquote>


How to install
--------

1. Clone the repository or download as a zip archive and unzip it.
2. There is an "angular-iunit.jar" file in the contents of the repository.
3. Go to any Jetbrains IDE like PyCharm or IntelliJ Idea and install the plugin from disk.
4. Select the "angular-iunit.jar" file during the install.

<b>Thats it</b>

Feel free to fork!
