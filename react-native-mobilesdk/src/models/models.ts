export interface IServerModel {
    serverId: string;
    name: string;
    host: string;
}

export interface IWebSurveyModel {
    token: string;
    url: string;
}

export interface IScenarioCallback {
    serverId: string;
    programKey: string;
    error: string;
}
