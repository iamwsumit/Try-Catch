# TryCatch

Hi evryone, I hope you are all good. Today I come with a little extension but very useful for you. This extension allows you to call methods and set the properties with try and catch. WHat is Try & Catch? Try and catch place around the code to catch the exception like if you have a list of 3 items and you are selecting 4th then it will throw a runtime error with a dialog that impact very bad on UI and to user. So that's why I come up with this little extension. It has few blocks that will help you to catch the exception while calling the method and setting the properties of the components. As it works througn Java reflection so you can also call method of extensions even this extension.

So let see its blocks and their documentation.

## All Blocks

![image](https://user-images.githubusercontent.com/74917290/126427522-c1899b71-d8e6-431e-a4bb-cf84e0306b60.png)

## Documentation

![component_event (4)](https://user-images.githubusercontent.com/74917290/126427597-b1d1dac1-388f-4692-aade-1a09956c0745.png)

This event raises when any exception is caught. It gives the exception in string. For ex, `java.lang.ArrayIndexOutOfBoundsException`.

![component_method (2)](https://user-images.githubusercontent.com/74917290/126427707-504a8107-5dc8-4cbe-86d2-26e50d4065f5.png)

This block is for selecting the item of a list with catch exceptions. If the index is greater than the list length then it will simply call the `catch` event with `java.lang.ArrayIndexOutOfBoundsException` exception.

![component_method (3)](https://user-images.githubusercontent.com/74917290/126427711-4c60bcea-f30f-4814-a155-0317eeac521f.png)

This block invokes the methods and properties of the component as well as extensions. You just need to pass the component or extension to `component` parmaeter and method or property name to the `name` parameter and then just provide a list of parameters of that method that you are calling and the method will be invoke or called. If the called method is void type then you will get nothing in return but if not then it will return the result of the invoked or called method. If any error occured then `catch` event will be raised.

I hope you understood the documentaion. So let's usage of it.

# Usage

There is very simple blocks and usage.

![image](https://user-images.githubusercontent.com/74917290/126428564-5ad62852-1902-4bef-9e1b-f3eac7c253a1.png)

Selecting a item from a list for given index.

![image](https://user-images.githubusercontent.com/74917290/126428802-b9efd46b-33bc-4e02-8176-77de12eb4aad.png)

Calling a return a method of a label

![image](https://user-images.githubusercontent.com/74917290/126428677-dea26e28-9dc5-48e1-965c-062695cef847.png)


