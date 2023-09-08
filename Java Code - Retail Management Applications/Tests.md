#Test Cases
##The order of these tests is important. Please complete them in order so that information is created and stored correctly for following tests.
## Preferably, follow the example inputs marked by "ie." within parentheses.
### Test cases do not cover all possible scenarios. They cover most of the important components implemented in our program.*

TEST 1 (Creating a customer account and logging out):
1. User launches application by running the Server.java file, then running the MainLogin.java file.
2. The main login screen pops up, prompting user to enter an email & password.
3. User selects the email textbox.
4. User enters email via the keyboard. (ie. "customer@email.com")
5. User selects the password textbox.
6. User enters password via the keyboard. (ie. "1234")
7. User selects the "Sign Up" button.  
*Expected result: Message with textbox pops up asking what user wants to be named.*  
8. User enters name via the keyboard. (ie. "Bob")
9. User selects the "OK" button.  
*Expected result: Message with textbox pops up prompting the user to select a role (customer/seller).*
10. User selects "customer" via the dropdown menu.  
*Expected result: Message is displayed informing user that an account has successfully been created.*.
11. User selects the "OK" button.  
*Expected result: User is brought to the customer main screen.
12. User selects the "Logout" button.  
*Expected result: Message pops up confirtming that user has successfully logged out.*
13. User selects the "OK" button.  
*Expected result: User is brought to the main login screen again.*

Test Status: Passed. 

-----------------------
TEST 2 (Creating a seller account, creating an appointment window, and logging out):
1. User launches application by running the Server.java file, then running the MainLogin.java file.
2. The main login screen pops up, prompting user to enter an email & password.
3. User selects the email textbox.
4. User enters email via the keyboard. (ie. "seller@email.com")
5. User selects the password textbox.
6. User enters password via the keyboard. (ie. "5678")
7. User selects the "Sign Up" button.  
*Expected result: Message with textbox pops up asking what user wants to be named.*
8. User enters name via the keyboard. (ie. "Billy")
9. User selects the "OK" button.  
*Expected result: Message with textbox pops up prompting the user to select a role (customer/seller).*
10. User selects "seller" via the dropdown menu.  
*Expected result: Message is displayed informing user that an account has successfully been created.*
11. User selects the "OK" button.  
*Expected result: User is brought to the seller main screen.*
12. User selects the "Create new escape room" button.
*Expected result: User is prompted to enter a name for the escape room.*
13. User enters name via the keyboard. (ie. "Island Mystery")
14. User selects the "OK" button.
*Expected result: User is brought back to the seller main screen.*
15. User selects the "View your escape rooms" button. 
*Expected result: User is brought to a new screen on which they can see all of their escape rooms.*
16. User utilizes the dropdown menu to select the number of the store they wish to enter. (ie. "1")
17. User selects the "Enter Store" button.
*Expected result: User now has access to a dropdown menu prompting them to choose an action option.*
18. User utilizes the dropdown menu to select the "Create a calendar" option.
19. User selects the "OK" button.
*Expected result: Another dropdown menu pops up prompting them to input a calendar title.*
20. User enters title via the keyboard. (ie. "Week 1")
21. User selects the "OK" button. 
*Expected result: A textbox appears prompting the user to enter a calendar description.*
22. User enters description via the keyboard. (ie. "Avaialable time slots for week 1")
23. User selects the "OK" button. 
*Expected result: A message appears notifying user that a calendar was created successfully.*
24. User selects the "OK" button. 
*Expected result: User is brought back to the screen on which they can view their escape rooms.*
25. User utilizes the dropdown menu to select the store they wish to enter. (ie. "1" again)
26. User selects the "Enter Store" button.
*Expected result: A dropdown menu appears prompting user to choose an action option.*
27. User utilizes the dropdown menu to select the "View calendars" option.
28. User selects the "OK" button. 
*Expected result: User can now see their calendars listed in the middle of the screen.*
29. User utilizes the center dropdown menu to select the calender they wish to enter. (ie. "1")
30. User selects the "Enter Calendar" button.
*Expected result: A dropdown menu appears prompting user to choose an action option.*
31. User utilizes the dropdown menu to select the "Create an appointment window" option. 
32. User selects the "OK" button. 
*Expected result: User is brought to a new screen that allows the user to enter appointment window information.*
32. User selects the title textbox.
33. User enters title via keyboard. (ie. "Window 1")
34. User utilizes the dropdown menu to select the day of the window from days 1-5 in the week. (ie. "1")
35. User utilizes the dropdown menu to select the start time of the window from hours 1-24 in the day. (ie. "8")
36. User utilizes the dropdown menu to select the end time of the window from hours 1-24 in the day. (ie. "9")
37. User selects the max attendees textbox. 
38. User enters number of max attendees via keyboard. (ie. "7")
39. User selects the "Enter" button. 
*Expected result: User is brought back to the screen on which they can view their escape rooms and calendars.*
40. User utilizes the dropdown menu to select the number of which calender they wish to enter. (ie. "1")
41. User selects the "Enter Calendar" button.
*Expected result: A dropdown menu appears prompting the user to choose an action option.*
42. User utilizes the dropdown menu to select an action option. (ie. "Create an appointment window")
43. User selects the "OK" button. 
*Expected result: User is brought back to the same screen as before. 
44. User utilizes the dropdown menu furthest right to select the appointment window number they wish to enter. (ie. "1")
45. User selects the "Enter Appointment Window" button.
*Expected result: A dropdown menu appears prompting user to select an action option.*
46. User utilizes the dropdown menu to select an action option. (ie. "Edit appointment window")
47. User selects the "OK" button. 
*Expected result: User is brought to the screen which allows them to enter new information for the appointment window.*
48. User edits information via interface options. (ie. selects max attendees textbox & enters "8")
49. User selects the "Enter" button. 
*Expected result: Message appears notifying user that the appointment window was edited successfully.*
50. User selects the "OK" button. 
*Expected result: The edited information has not updated yet.*
51. User selects the "Back" button.
*Expected result: User is brought back to the seller main screen.*
52. User selects the "View your escape rooms" button.
*Expected result: User is brought to the screen where they can view their escape rooms.*
53. User repeats steps 25-30, 40-41, 44-45.
*Expected result: The previously edited information is now updated.* 
54. User selects the "Back" button. 
*Expected result: User is brought back to the seller main screen.*
55. User selects the "Logout" button. 
*Expected result: User is brought back to the main login screen.*


Test Status: Passed
-----------------------
TEST 3 (Customer logging in & creating an appointment):
1. User launches application by running the Server.java file, then running the MainLogin.java file.
2. The main login screen pops up, prompting user to enter an email & password.
3. User selects the email textbox.
4. User enters email via the keyboard. (ie. "customer@email.com")
5. User selects the password textbox.
6. User enters password via the keyboard. (ie. "1234")
7. User selects the "Login" button. 
*Expected result: Message is displayed informing user that they have successfully logged in.*
8. User selects the "OK" button.  
*Expected result: User is brought to the customer main screen.*
9. User selects the "View Stores" button.
*Expected result: User is brought to the creating an account screen.*
10. User enters the dropdown menu and selects the store they want to enter. (i.e. "1")
11. User selects the "Enter Store" button.
*Expected result: The right half of the screen updates to show available calendars.*
12. User enters the new dropdown menu and selects the calendar they want to enter. (1.e. "1")
13. User selects the "Enter Calendar" button.
*Expected result: A small popup window appears and tells you the calendar entered.*
14. User enters the dropdown menu in the popup and selects "Make an appointment."
*Expected result: A small popup window appears and asks what day to look at*
15. User enters a number 1-5. (i.e. "1")
*Expected result: A small popup window appears and asks user to select an appointment window from the day entered.*
16. User enters the dropdown and select an appointment by the starting and ending time.
*Expected result: A small popup window appears and asks user to enter how many people are attending and lists the max attendees.*
17. User lists the amount of people are attending. (i.e. "4")
*Expected result:  A small popup window appears and asks user to name the appointment.*
18. User types out a name for the appointment (i.e. "Appointment name")
*Expected result: A small popup window appears and tells the user that appointment was successfully made.*
19. User selects "Okay" on the popup and then press "Back" on the frame.
*Expected result: The popup window closes and user is brought back to the customer main screen.*
20. User selects the "View Pending Appointments button.
*Expected result: A new frame opens that shows the user's pending appointments.*
21. After seeing the new pending appointment made, user selects the "Back" button.
*Expected result: Pending appointments frame closes and user is brought back to the customer main screen.*
22. User selects the "Logout" button.
*Expected result: A small popup window opens that tells the user that they have successfully logged out and is brought back to the log in screen.*
