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
    public notifyEvent(event: string) {
        MobileSdk.notifyEvent(event);
    }

    public notifyAppForeground(data: { [Name: string]: string }) {
        MobileSdk.notifyAppForeground(data);
    }

    public async deleteProgram(serverId: string, programKey: string, deleteCustomData: boolean): Promise<void> {
        return MobileSdk.deleteProgram(serverId, programKey, deleteCustomData);
    }

    public async deleteAll(deleteCustomData: boolean): Promise<void> {
        return MobileSdk.deleteAll(deleteCustomData);
    }

    public setCallback(serverId: string, programKey: string) {
        MobileSdk.setCallback(serverId, programKey);
    }

    public removeCallback(serverId: string, programKey: string) {
        MobileSdk.removeCallback(serverId, programKey);
    }

    public async triggerDownload(serverId: string, programKey: string): Promise<boolean> {
        return MobileSdk.triggerDownload(serverId, programKey);
    }

    public notifyEventWithData(event: string, data: { [Name: string]: string }) {
        MobileSdk.notifyEventWithData(event, data);
    }

    public addJourneyLog(data: { [Name: string]: string }) {
        MobileSdk.addJourneyLog(data);
    }

    public addJourneyLogWithServer(serverId: string, programKey: string, data: { [Name: string]: string }) {
        MobileSdk.addJourneyLogWithServer(serverId, programKey, data);
    }
}

export const TriggerSdk = new Manager();
