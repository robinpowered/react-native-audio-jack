import {
  DeviceEventEmitter,
  NativeModules
} from 'react-native';

const {RNAudioJack} = NativeModules;

export default {
  ...RNAudioJack,
  addListener(callback) {
    return DeviceEventEmitter.addListener(
      RNAudioJack.AUDIO_CHANGED_NOTIFICATION,
      callback
    );
  }
};
