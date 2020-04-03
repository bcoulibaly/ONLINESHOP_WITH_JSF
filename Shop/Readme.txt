Belisso Shop Info
==============

* Copyright (c), 2019-2020, Belisso Shop


REQUIREMENTS
------------

Belisso Shop 0.0.1-SNAPSHOT or later with Java EE 7.

#### Suggested extensions:
- Wildfly: Required for Server in version 10.1.0
- JSF : Required for the MVC-Model in version 2.2
- Primefaces: Framework required for JSF in version 7.0
- Bootfaces: Framework for JSF in version 1.4.2
- Blank archetyp Projekt Java EE 7

CURRENT TESTED PLATFORMS
------------------------

- Windows 10 
-Eclipse 


#### Platforms currently in progress:
- Windows

INSTALLATION AND CONFIGURATION
------------------------------

#### Typical installation without eclipse-ide

- Installation OpenJDK 8u222b10 
- Installation Maven 3.5.x 
- Installation Wildfly 10.1.0.Final 
- Start Wildfly
- Deployment und Test the shop.war on the server 
- Testen Sie auch „undeploy“ 

#### Typical installation with eclipse-ide

- Installation Eclipse IDE 2019-09 for Java EE Developers 
- Import the maven-Project in your ide (file -> Import -> Existing Maven Project -> Driectory of the Pom fle)
- Open the Termin in this Project (right click on the project name BelissoShop -> shop in local terminal - Terminal) then run this cmd :  mvn clean install
- Install JBoss-Tool as Eclipse Plug-In for the server  (help -> install new Software) then give this link (http://www.jboss.org/tools)
- Wildfly starten 
- Deployment via „Run Configurations“ (Goal: „package wildfly:deploy“)


WHAT TO DO IF IT DOESN'T WORK
-----------------------------

First make sure you've read this file completely, especially the
"INSTALLATION AND CONFIGURATION" section.  If it still doesn't work then
you can ask for help on:

-Eclipse : click at the menuitem on your Eclipse-IDE and choose "Help Contents".
-PrimeFaces for JSF : visite the link for Dokumentation (https://primefaces.github.io/primefaces/7_0/)
-Jboss-Tool: visite the link for Dokumentation (https://tools.jboss.org/documentation/faq/)

***!! If you have any problems, please start the server in DEBUG-Modus
and set Breakpoint in the class, that should have the problem***

OTHER NOTES
-----------

If you have a great idea or want to help out, just message me with your change proposal
at at my email : bcoulibaly93@hotmail.com, so that i can make a pull-request for you.

LICENSING
---------

This program is released under theApache License, Version 2.0
(at your option) any later version, please visite the site for details : 
(http://www.apache.org/licenses/LICENSE-2.0.html)
