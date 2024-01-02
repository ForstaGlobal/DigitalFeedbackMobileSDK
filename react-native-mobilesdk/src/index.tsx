import { NativeModules, Platform } from 'react-native';
import type { IScenarioCallback, IServerModel, IWebSurveyModel } from './models/models';
import type { ISdkListener } from './program/triggerCallback';
import { TriggerManager } from './program/triggerCallback';
import SurveyWebView from './views/surveyWebView';

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

export type { IScenarioCallback, IServerModel, IWebSurveyModel, ISdkListener };

export { SurveyWebView, TriggerManager };

function transformServer(result: any): IServerModel {
    return {
        host: result.host,
        name: result.name,
        serverId: result.serverId
    };
}

export function injectWebView() {
    MobileSdk.injectWebView();
}

// Confirmit
export function initSdk() {
    return MobileSdk.initSdk();
}

export function enableLog(enable: boolean) {
    MobileSdk.enableLog(enable);
}

// Trigger
export function notifyEvent(event: string) {
    MobileSdk.notifyEvent(event);
}

export function notifyAppForeground(data: { [Name: string]: string }) {
    MobileSdk.notifyAppForeground(data);
}

export function deleteProgram(serverId: string, programKey: string, deleteCustomData: boolean): Promise<void> {
    return MobileSdk.deleteProgram(serverId, programKey, deleteCustomData);
}

export function deleteAll(deleteCustomData: boolean): Promise<void> {
    return MobileSdk.deleteAll(deleteCustomData);
}

export function setCallback(serverId: string, programKey: string) {
    MobileSdk.setCallback(serverId, programKey);
}

export function removeCallback(serverId: string, programKey: string) {
    MobileSdk.removeCallback(serverId, programKey);
}

export function triggerDownload(serverId: string, programKey: string): Promise<boolean> {
    return MobileSdk.triggerDownload(serverId, programKey);
}

export function notifyEventWithData(event: string, data: { [Name: string]: string }) {
    MobileSdk.notifyEventWithData(event, data);
}

// Server
export async function getUs(): Promise<IServerModel> {
    return transformServer(await MobileSdk.getUs());
}

export async function getUk(): Promise<IServerModel> {
    return transformServer(await MobileSdk.getUk());
}

export async function getAustralia(): Promise<IServerModel> {
    return transformServer(await MobileSdk.getAustralia());
}

export async function getCanada(): Promise<IServerModel> {
    return transformServer(await MobileSdk.getCanada());
}

export async function getGermany(): Promise<IServerModel> {
    return transformServer(await MobileSdk.getGermany());
}

export async function getHxPlatform(): Promise<IServerModel> {
    return transformServer(await MobileSdk.getHxPlatform());
}

export async function getHxAustralia(): Promise<IServerModel> {
    return transformServer(await MobileSdk.getHxAustralia());
}

export async function configureUs(clientId: string, clientSecret: string): Promise<void> {
    return await MobileSdk.configureUs(clientId, clientSecret);
}

export async function configureUk(clientId: string, clientSecret: string): Promise<void> {
    return await MobileSdk.configureUk(clientId, clientSecret);
}

export async function configureAustralia(clientId: string, clientSecret: string): Promise<void> {
    return await MobileSdk.configureAustralia(clientId, clientSecret);
}

export async function configureCanada(clientId: string, clientSecret: string): Promise<void> {
    return await MobileSdk.configureCanada(clientId, clientSecret);
}

export async function configureGermany(clientId: string, clientSecret: string): Promise<void> {
    return await MobileSdk.configureGermany(clientId, clientSecret);
}

export async function configureHxPlatform(clientId: string, clientSecret: string): Promise<void> {
    return await MobileSdk.configureHxPlatform(clientId, clientSecret);
}

export async function configureHxAustralia(clientId: string, clientSecret: string): Promise<void> {
    return await MobileSdk.configureHxAustralia(clientId, clientSecret);
}

export async function configureServer(name: string, host: string, clientId: string, clientSecret: string): Promise<IServerModel> {
    return transformServer(await MobileSdk.configureServer(name, host, clientId, clientSecret));
}

export async function getServer(serverId: string): Promise<IServerModel> {
    return transformServer(await MobileSdk.getServer(serverId));
}

export async function getServers(): Promise<IServerModel[]> {
    const result = await MobileSdk.getServers();
    const servers = [];
    for (const server of result) {
        servers.push(transformServer(server));
    }

    return servers;
}
