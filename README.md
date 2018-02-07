# react-native-audio-jack

## Getting started

`$ npm install react-native-audio-jack --save`

### Mostly automatic installation

`$ react-native link react-native-audio-jack`

### Important Installation Step For Android
The line of code below should be added to `AndroidManifest.xml` for the audio jack listener to work.

```xml
<uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
```

## Usage
```javascript
import AudioJackManager from 'react-native-audio-jack';

// Determine if audio jack is plugged in
AudioJackManager.isPluggedIn().then(isPluggedIn => console.log(isPluggedIn));

// Listener to listen for change in audio jack status
var audioJackListener = AudioJackManager.addListener(({isPluggedIn}) => console.log(isPluggedIn));

// Later on.... (when component unmounts etc)
audioJackListener.remove();
```
