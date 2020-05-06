In the third assignment, your goal is to create a Petfinder application with offline mode.

API: https://www.petfinder.com/developers/v2/docs/ - here you will find API description and connection guide.

Main screen:
-    At the top of the screen, you should place two selectors (dropdown or any other) with animal type and breed. (You should get it from API and store in database) Selectors should support case Any. In this case, we don't use any type of filtration just get everything.
-    After both of them selected you should load a list of animals and show it in recycle view. (API supports pagination not required but it will be nice to use it) 
-    Every item should contain breed, age, name, description, and photo if any. Example of the item below.
![pic](https://i.imgur.com/u6r9f2Y.jpg)

Details screen:

-    Contain information from list + details (you can find them at API description)
-    UI part could be done in any way you want (check profile details of the telegram, or other applications for inspiration)

General requirements:

-    You may use any of the instruments mentioned in lections (native navigation, cicerone or AAC Navigation component (check slides from moodle)
-    You should use provided in lecture 7 set of network technologies + ReactiveX.
-    You should use at least two types of data storages for offline mode (database, shared preferences, file storage)
-    Application design should support any type of screens (forget about hardcoded width and height, check it in XML) 
-    If I change screen orientation application should store all information that was entered.
-    All resources should be stored in res files.


Testing scenario:

Step 1: Getting into the application will be selected type and breed randomly(maybe not selected case of getting all). 

Step 2:  A User will randomly select a few animals and check details. 

Step 3: The application will be closed completely and the internet will be turned off.

Step 4: Getting into the application selecting the same type and breed.

Step 5: Check details of the same animals.
The application should work in the same way as with the internet.

Few comments:

-    For storing access token you should use encrypted shared preferences. 
-    For storing images you may use libraries\db\file storage.
-    In the case when a user in offline mode will select something else you should show a message that there is no internet connection.

Submit:

-    The deadline is the 13th of May 23.59.
-    You should submit zip archive with project directory (without build directory)
-    The name of the archive should follow the next template <Last Name>.<Assigment number>.zip -> Simonenko.3.zip
