import { NativeEventEmitter, NativeModules } from 'react-native';
import { PageControl } from './pageControl';
import { IPageControl, ISurveyErrored, ISurveyFinished } from '../models/models';

class Manager {
    private sdkEmitter = NativeModules.SdkEmitter;
    private triggerManagerEmitter = new NativeEventEmitter(this.sdkEmitter);

    public setOnSurveyPageReady(callback: (event: PageControl) => void) {
        return this.triggerManagerEmitter.addListener('__mobileOnSurveyPageReady', (event: IPageControl) => {
            callback(new PageControl(event));
        });
    }

    public setOnSurveyErrored(callback: (event: ISurveyErrored) => void) {
        return this.triggerManagerEmitter.addListener('__mobileOnSurveyErrored', callback);
    }

    public setOnSurveyFinished(callback: (event: ISurveyFinished) => void) {
        return this.triggerManagerEmitter.addListener('__mobileOnSurveyFinished', callback);
    }

    public setOnSurveyQuit(callback: (values: { [key: string]: string }) => void) {
        return this.triggerManagerEmitter.addListener('__mobileOnSurveyQuit', callback);
    }
}

export const SurveyFrameManager = new Manager();
