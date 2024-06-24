export interface IServerModel {
    serverId: string;
    name: string;
    host: string;
}

export interface IWebSurveyModel {
    token: string;
    url: string;
}

export interface ISurveyModel {
    serverId: string;
    programKey: string;
    surveyId: string;
    languageId: number | null;
    customData: { [key: string]: string };
    respondentValue: { [key: string]: string };
}

export interface IScenarioCallback {
    serverId: string;
    programKey: string;
    error: string;
}

export interface ISurveyFrameActionResult {
    success: string;
    message: string;
}

export enum NodeType {
    Single = 'SINGLE',
    Multi = 'MULTI',
    Text = 'TEXT',
    Numeric = 'NUMERIC',
    Info = 'INFO',
    NotSupported = 'NOT_SUPPORTED'
}

export interface IQuestion {
    id: string;
    nodeType: NodeType;
}

export interface IPageControl {
    forwardText: string;
    backwardText: string;
    okText: string;
    showForward: boolean;
    showBackward: boolean;
    serverId: string;
    surveyId: string;
    programKey: string;
}

export interface ISurveyErrored extends IPageControl {
    error: ISurveyError;
    values: { [key: string]: string };
}

export interface ISurveyFinished extends IPageControl {
    values: { [key: string]: string };
}

export interface ISurveyError {
    message: string;
    stack: string;
}

export interface IDefaultQuestion {
    id: string;
    nodeType: NodeType;
    title: string;
    titleSpan: string;
    text: string;
    instruction: string;
    errors: ISurveyError[];
}

export interface IInfoQuestion extends IDefaultQuestion {}

export interface ITextQuestion extends IDefaultQuestion {}

export interface INumericQuestion extends IDefaultQuestion {}

export interface ISingleQuestion extends IDefaultQuestion {
    appearance: number;
    answers: QuestionAnswer[];
}

export interface IMultiQuestion extends IDefaultQuestion {
    appearance: number;
    answers: QuestionAnswer[];
}

export interface QuestionAnswer {
    code: string;
    text: string;
    isHeader: boolean;
    answers: QuestionAnswer[];
}

export enum SingleAppearance {
    radioButton,
    answerButton,
    dropDown,
    slider,
    horizontalRatingScale,
    startRating,
    gridBars
}
