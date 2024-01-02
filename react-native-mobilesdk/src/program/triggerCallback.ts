import { NativeEventEmitter, NativeModules } from 'react-native';
import type { IScenarioCallback, IWebSurveyModel } from '../models/models';

export interface ISdkListener {
    onSurveyWebview(model: IWebSurveyModel): void;
    onScenarioLoad(model: IScenarioCallback): void;
    onScenarioError(model: IScenarioCallback): void;
}

class Manager {
    private listener: ISdkListener | null = null;
    private sdkEmitter = NativeModules.SdkEmitter;
    private triggerManagerEmitter = new NativeEventEmitter(this.sdkEmitter);

    public constructor() {
        this.triggerManagerEmitter.addListener('__mobileOnWebSurveyStart', this.onWebSurveyStart);
        this.triggerManagerEmitter.addListener('__mobileOnScenarioLoad', this.onScenarioLoad);
        this.triggerManagerEmitter.addListener('__mobileOnScenarioError', this.onScenarioError);
    }

    public setSdkListener(callbackListener: ISdkListener) {
        this.listener = callbackListener;
    }

    public removeSdkListener() {
        this.listener = null;
    }

    private onWebSurveyStart = (event: IWebSurveyModel) => {
        this.listener?.onSurveyWebview(event);
    };

    private onScenarioLoad = (event: IScenarioCallback) => {
        this.listener?.onScenarioLoad(event);
    };

    private onScenarioError = (event: IScenarioCallback) => {
        this.listener?.onScenarioError(event);
    };
}

export const TriggerManager = new Manager();
