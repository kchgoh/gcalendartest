gcalendartest
=============

A learning exercise to try my hands on OAuth and some Google API with a Java webapp.


Requirements
------------

- Web page to allow user to login and view his/her Google Calendar events of the next 2 weeks.
- User can click a button to open a GMail compose window with the calendar details pre-poulated in it.
- Server-side needs to use Java 6 and Tomcat 6.0


Screenshots
-----------

![Calendar view after logging in](/resources/screenshots/list.jpg)


Build
-----

A maven file is provided on the root level.


Design Notes
------------

To prevent race condition when user tries to do multiple clicks on log in and log out, I found it necessary to create a per session lock to ensure that within a session, only one thing can happen at a time.

Although Google does provide a fair amount of sample code, often they are not very consistent (one place suggests using this approach, another place suggests another). I tried to encapsulate all the Google-specific calls in separate implementation classes. This also helps in doing unit test, though I found it to be of limited use in this case, as this exercise is more about whether I am calling the external API correctly. 
