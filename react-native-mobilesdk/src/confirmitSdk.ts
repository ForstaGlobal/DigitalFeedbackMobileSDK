import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
    `The package 'react-native-mobilesdk' doesn't seem to be linked. Make sure: \n\n${Platform.select({
        ios: "- You have run 'pod install'\n",
        default: ''
    })}- You rebuilt the app after installing the package\n` + '- You are not using Expo Go\n';

const MobileSdk = NativeModules.MobileSdk
    ? NativeModules.MobileSdk
    : new Proxy(
          {},
          {
              get() {
                  throw new Error(LINKING_ERROR);
              }
          }
      );

class Manager {
    public injectWebView() {
        MobileSdk.injectWebView();
    }

    public async initSdk(): Promise<void> {
        return MobileSdk.initSdk();
    }

    public enableLog(enable: boolean) {
        MobileSdk.enableLog(enable);
    }
}

export const ConfirmitSdk = new Manager();
