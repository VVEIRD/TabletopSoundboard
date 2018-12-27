# Create Spotify App 
After visiting https://developer.spotify.com/dashboard you will be prompted to login with you spotify account. After logging in you'll need to Create an app:

![Create app](https://raw.githubusercontent.com/WElRD/Images/master/spotify/01-Dashboard.png)

A dialog will pop up and ask you for the name and description of the app, aswell as what app you are creating. Choose whatever you like for the first two and choose "Desktop App" for the type of app:

![Create dialog](https://raw.githubusercontent.com/WElRD/Images/master/spotify/02-Create-App-Dialog.png)

When you click next you will be prompted if your app is commercial or non-commercial, choose the apropiate (Here we chose non-commercial):

![Commercial usage](https://raw.githubusercontent.com/WElRD/Images/master/spotify/03-Non-Commercial-Usage.png)

For the non-commercial choice you'll have so accept the TOS of spotify before creating the app:

![TOS](https://raw.githubusercontent.com/WElRD/Images/master/spotify/04-Accept-TOS.png)

Once you accepted the TOS you will be rediercted to your newly created app. Click on "Show client secret" to display the client secret. You need both Client-Id and Client secret for the sound board app. Copy those to use with the ESD Soundboard App.

![Client Id and secret](https://raw.githubusercontent.com/WElRD/Images/master/spotify/05-Get-Client-Id-and-Secret.png)

Enter your Id and Secret into your ESD Soundboard App:

![Config](https://raw.githubusercontent.com/WElRD/Images/master/ESDSoundboardApp/Frontend-06-Spotify-Options.png)

Once you copied your Id and secret click on the "Edit Settings" button and add "https://localhost:5000/spotify-redirect" to the Redirect URIs:

![Whitelist](https://raw.githubusercontent.com/WElRD/Images/master/spotify/06-Add-Redirect_URI.png)

Click on save and your done setting up your spotify app.

Now click on Login in the Options panel of your ESD Soundboard App and if the login was successful, click on save. After that your Spotify Playlists should be present when you edit the themes of your Soundboards.
