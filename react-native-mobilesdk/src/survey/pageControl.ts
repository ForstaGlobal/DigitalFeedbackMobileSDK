import { NativeModules, Platform } from 'react-native';
import { IDefaultQuestion, IPageControl, ISurveyFrameActionResult, QuestionAnswer } from 'react-native-mobilesdk';

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

export class PageControl {
    public forwardText: string;
    public backwardText: string;
    public showForward: boolean;
    public showBackward: boolean;
    public okText: string;
    public serverId: string;
    public programKey: string;
    public surveyId: string;

    public constructor(page: IPageControl) {
        this.forwardText = page.forwardText;
        this.backwardText = page.backwardText;
        this.showForward = page.showForward;
        this.showBackward = page.showBackward;
        this.okText = page.okText;
        this.serverId = page.serverId;
        this.programKey = page.programKey;
        this.surveyId = page.surveyId;
    }

    public async getQuestion(): Promise<IDefaultQuestion[]> {
        return await MobileSdkSurvey.getQuestions(this.serverId, this.programKey, this.surveyId);
    }

    public async getMulti(questionId: string): Promise<QuestionAnswer[]> {
        return await MobileSdkSurvey.getMulti(this.serverId, this.programKey, this.surveyId, questionId);
    }

    public async setMulti(questionId: string, code: string, selected: boolean) {
        return await MobileSdkSurvey.setMulti(this.serverId, this.programKey, this.surveyId, questionId, code, selected);
    }

    public async getSingle(questionId: string): Promise<QuestionAnswer> {
        return await MobileSdkSurvey.getSingle(this.serverId, this.programKey, this.surveyId, questionId);
    }

    public async setSingle(questionId: string, code: string): Promise<void> {
        return await MobileSdkSurvey.setSingle(this.serverId, this.programKey, this.surveyId, questionId, code);
    }

    public async getText(questionId: string): Promise<string> {
        return await MobileSdkSurvey.getText(this.serverId, this.programKey, this.surveyId, questionId);
    }

    public async setText(questionId: string, value: string): Promise<void> {
        return await MobileSdkSurvey.setText(this.serverId, this.programKey, this.surveyId, questionId, value);
    }

    public async getNumeric(questionId: string): Promise<string> {
        return await MobileSdkSurvey.getNumeric(this.serverId, this.programKey, this.surveyId, questionId);
    }

    public async setNumeric(questionId: string, value: number, isDouble: boolean): Promise<void> {
        return await MobileSdkSurvey.setNumeric(this.serverId, this.programKey, this.surveyId, questionId, value, isDouble);
    }

    public async next(): Promise<ISurveyFrameActionResult> {
        return await MobileSdkSurvey.next(this.serverId, this.programKey, this.surveyId);
    }

    public async back(): Promise<ISurveyFrameActionResult> {
        return await MobileSdkSurvey.back(this.serverId, this.programKey, this.surveyId);
    }

    public async quit(upload: boolean): Promise<ISurveyFrameActionResult> {
        return await MobileSdkSurvey.quit(this.serverId, this.programKey, this.surveyId, upload);
    }

    public async title(): Promise<string> {
        return await MobileSdkSurvey.getTitle(this.serverId, this.programKey, this.surveyId);
    }

    public async text(): Promise<string> {
        return await MobileSdkSurvey.getText(this.serverId, this.programKey, this.surveyId);
    }
}
