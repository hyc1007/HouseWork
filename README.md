This project is a written test for Chief , The development environment used is as follows:
Android Studio Giraffe
Jdk 17
compileSdkVersion 34
compose-bom 2023.03.00

because the back-end interface does not provide the parameters required for page loading (such as the number of data per page, the current loading page, etc.), so the dropdown loading in the app uses the repeated data on the first page to demonstrate page loading. You can use the getAccountData method of ChiefApi class in the code. Change the names of the two query parameters to actually implement page loading.
app-debug.apk is the compiled apk, and apk_test.mp4 is the demo demo video.
It uses compose to draw the ui interface,kotlin flow to process the data, and Retrofit to make network requests. The project is divided into 4 modules build-logic module uses the official recommended versionCatalogs to manage the version numbers of kotlin,java,compose and other dependencies; The UI-core module includes the basic ui components; The pagerhelper module abstracts the page loading logic. Users only need to inherit the PageSourceUnit class and implement the load method to realize page loading easily, without caring about the specific page loading logic. The app module contains the interface ui and data loading logic.
