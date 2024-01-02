// @ts-ignore
import type { IScenarioCallback, ISdkListener, IWebSurveyModel } from '@forstaglobal/react-native-mobilesdk';
import MessageHub from './utils/messageHub';

export default class TriggerListener implements ISdkListener {
    public onSurveyWebview(model: IWebSurveyModel): void {
        MessageHub.send('webSurvey', model);
    }

    public onScenarioLoad(model: IScenarioCallback): void {
        MessageHub.send('onScenarioLoad', model);
    }

    public onScenarioError(model: IScenarioCallback): void {
        MessageHub.send('onScenarioError', model);
    }
}
