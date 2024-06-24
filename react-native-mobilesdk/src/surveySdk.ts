import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
    `The package 'react-native-mobilesdk' doesn't seem to be linked. Make sure: \n\n${Platform.select({
        ios: "- You have run 'pod install'\n",
        default: ''
    })}- You rebuilt the app after installing the package\n` + '- You are not using Expo Go\n';

const MobileSdkSurvey = NativeModules.MobileSdkSurvey
    ? NativeModules.MobileSdkSurvey
    : new Proxy(
          {},
          {
              get() {
                  throw new Error(LINKING_ERROR);
              }
          }
      );

class Manager {
    public async startSurvey(
        serverId: string,
        programKey: string,
        surveyId: string,
        data: { [Name: string]: string },
        respondentValues: { [Name: string]: string }
    ): Promise<void> {
        return await MobileSdkSurvey.startSurvey(serverId, programKey, surveyId, data, respondentValues);
    }
}

export const SurveySdk = new Manager();
