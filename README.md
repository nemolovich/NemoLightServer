NemoLightServer
===============

A **very light server**. Using its owned Servlet definition.
Using:
* <a href="http://sparkjava.com/">Spark</a> to listen Web requests actions (like Servlets).
* <a href="http://freemarker.org/">Freemarker</a> to compile web pages (like JSP/JSF).

The project use the master module: **NemoLightMaster** (_fr.nemolivich.apps.nemolight.nemolight-master_) including submodules:
* **NemoLightAdminCommon** (_fr.nemolivich.apps.nemolight.nemolight-admin-common_): definition of common admin code;
* **NemoLightServer** (_fr.nemolivich.apps.nemolight.nemolight-server_): definition of Interfaces and Classes used to deploy applications;
* **NemoLightCore** (_fr.nemolivich.apps.nemolight.nemolight-core_): server deployment manager;
* **NemoLightAdmin** (_fr.nemolivich.apps.nemolight.nemolight-admin_): admin client to connect to server administration (console);
* **NemoLightAdminGUI** (_fr.nemolivich.apps.nemolight.nemolight-admin-gui_): admin client to connect to server administration (GUI).

To deploy an application:
* Go to **NemoLightCore** module;
* Put **JAR file** in the "deploy" folder (default _./deploy_);
[ * If the application depends of others libraries:
		* Extern JARS: put it in the folder "dependencies" (default _./dependencies_);
		* Maven dependencies: If the dependencies are embedded in the pom in JAR the deploy will download it;]
* Run Launcher in **NemoLightCore**.
