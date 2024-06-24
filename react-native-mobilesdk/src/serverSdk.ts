import { NativeModules, Platform } from 'react-native';
import { IServerModel } from './models/models';

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
    public async getUs(): Promise<IServerModel> {
        return this.transformServer(await MobileSdk.getUs());
    }

    public async getUk(): Promise<IServerModel> {
        return this.transformServer(await MobileSdk.getUk());
    }

    public async getAustralia(): Promise<IServerModel> {
        return this.transformServer(await MobileSdk.getAustralia());
    }

    public async getCanada(): Promise<IServerModel> {
        return this.transformServer(await MobileSdk.getCanada());
    }

    public async getGermany(): Promise<IServerModel> {
        return this.transformServer(await MobileSdk.getGermany());
    }

    public async getHxPlatform(): Promise<IServerModel> {
        return this.transformServer(await MobileSdk.getHxPlatform());
    }

    public async getHxAustralia(): Promise<IServerModel> {
        return this.transformServer(await MobileSdk.getHxAustralia());
    }

    public async configureUs(clientId: string, clientSecret: string): Promise<void> {
        return await MobileSdk.configureUs(clientId, clientSecret);
    }

    public async configureUk(clientId: string, clientSecret: string): Promise<void> {
        return await MobileSdk.configureUk(clientId, clientSecret);
    }

    public async configureAustralia(clientId: string, clientSecret: string): Promise<void> {
        return await MobileSdk.configureAustralia(clientId, clientSecret);
    }

    public async configureCanada(clientId: string, clientSecret: string): Promise<void> {
        return await MobileSdk.configureCanada(clientId, clientSecret);
    }

    public async configureGermany(clientId: string, clientSecret: string): Promise<void> {
        return await MobileSdk.configureGermany(clientId, clientSecret);
    }

    public async configureHxPlatform(clientId: string, clientSecret: string): Promise<void> {
        return await MobileSdk.configureHxPlatform(clientId, clientSecret);
    }

    public async configureHxAustralia(clientId: string, clientSecret: string): Promise<void> {
        return await MobileSdk.configureHxAustralia(clientId, clientSecret);
    }

    public async configureServer(name: string, host: string, clientId: string, clientSecret: string): Promise<IServerModel> {
        return this.transformServer(await MobileSdk.configureServer(name, host, clientId, clientSecret));
    }

    public async getServer(serverId: string): Promise<IServerModel> {
        return this.transformServer(await MobileSdk.getServer(serverId));
    }

    public async getServers(): Promise<IServerModel[]> {
        const result = await MobileSdk.getServers();
        const servers = [];
        for (const server of result) {
            servers.push(this.transformServer(server));
        }

        return servers;
    }

    private transformServer(result: any): IServerModel {
        return {
            host: result.host,
            name: result.name,
            serverId: result.serverId
        };
    }
}

export const ServerSdk = new Manager();
