import { NativeEventEmitter, NativeModules } from 'react-native';
import { IScenarioCallback, ISurveyModel, IWebSurveyModel } from '../models/models';

class Manager {
    private sdkEmitter = NativeModules.SdkEmitter;
    private triggerManagerEmitter = new NativeEventEmitter(this.sdkEmitter);

    public setOnWebSurveyStart(callback: (event: IWebSurveyModel) => void) {
        return this.triggerManagerEmitter.addListener('__mobileOnWebSurveyStart', callback);
    }

    public setOnSurveyStart(callback: (event: ISurveyModel) => void) {
        return this.triggerManagerEmitter.addListener('__mobileOnSurveyStart', callback);
    }

    public setOnScenarioLoad(callback: (event: IScenarioCallback) => void) {
        return this.triggerManagerEmitter.addListener('__mobileOnScenarioLoad', callback);
    }

    public setOnScenarioError(callback: (event: IScenarioCallback) => void) {
        return this.triggerManagerEmitter.addListener('__mobileOnScenarioError', callback);
    }
}

export const TriggerManager = new Manager();
