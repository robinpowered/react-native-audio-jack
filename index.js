import {
  DeviceEventEmitter,
  NativeModules
} from 'react-native';

const {AudioJack} = NativeModules;

export default {
  ...AudioJack,
  addListener(callback) {
    return DeviceEventEmitter.addListener(
      AudioJack.AUDIO_CHANGED_NOTIFICATION,
      callback
    );
  }
};
