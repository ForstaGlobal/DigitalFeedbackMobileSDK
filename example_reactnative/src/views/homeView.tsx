import { ConfirmitSdk, IScenarioCallback, ISurveyModel, IWebSurveyModel, ServerSdk, SurveySdk, TriggerManager, TriggerSdk, IServerModel } from '@forstaglobal/react-native-mobilesdk';
import * as React from 'react';
import { useEffect, useState } from 'react';
import { Button, EmitterSubscription, ScrollView, StyleSheet, Text, TextInput, View } from 'react-native';
import SurveyWebViewView from './surveyWebViewView';
import DialogView from '../questions/dialogView';
import { Dropdown } from "react-native-element-dropdown";

const HomeView = () => {
    const [selectedServer, setSelectedServer] = useState<IServerModel>({ host: '', serverId: '', name: '' });
    const [dialogHidden, setDialogHidden] = useState(true);
    const [webViewHidden, setWebDialogHidden] = useState(true);
    const [config, setConfig] = useState<ISurveyModel>({
        serverId: '',
        programKey: '',
        surveyId: '',
        languageId: null,
        customData: {},
        respondentValue: {}
    });
    const [programKey, setProgramKey] = useState('');
    const [event, setEvent] = useState('');
    const [webViewConfig, setWebViewConfig] = useState<IWebSurveyModel>({
        token: '',
        url: ''
    });

    const [servers, setServers] = useState<IServerModel[]>([]);

    useEffect(() => {
        ConfirmitSdk.enableLog(true);

        async function runAsync() {
            await ConfirmitSdk.initSdk();
            await ServerSdk.configureUk("<Client ID>", "<Client Secret>");

            const configuredServers = await ServerSdk.getServers();
            const servers: IServerModel[] = [];
            for (const server of configuredServers) {
                servers.push(server);
            }

            if (servers.length !== 0 && servers[0] != null) {
                setSelectedServer(servers[0]);
            }

            setServers(servers);
        }
        runAsync();
    }, []);

    useEffect(() => {
        const webview: EmitterSubscription = TriggerManager.setOnWebSurveyStart(onWebSurvey);
        const surveyStart: EmitterSubscription = TriggerManager.setOnSurveyStart(onSurveyStart);
        const scenarioLoad: EmitterSubscription = TriggerManager.setOnScenarioLoad(onScenarioLoad);

        return () => {
            webview.remove();
            surveyStart.remove();
            scenarioLoad.remove();
        };
    }, []);

    const onWebSurvey = (events: IWebSurveyModel) => {
        setWebViewConfig(events);
        setWebDialogHidden(false);
    };

    const onSurveyStart = (events: ISurveyModel) => {
        const { serverId, programKey, surveyId } = events;
        SurveySdk.startSurvey(serverId, programKey, surveyId, { key1: 'value1' }, { respondentKey: 'respondentValue' });

        setDialogHidden(false);
        setConfig(events);
    };

    // @ts-ignore
    const onScenarioLoad = (events: IScenarioCallback) => {};

    const onDialogCloseAction = () => {
        setDialogHidden(true);
    };

    return (
        <View style={{ flex: 1 }}>
            <ScrollView>
                <View style={styles.container}>
                    <View style={[ styles.viewContainers ]}>
                        <Text>Host</Text>
                        <Dropdown
                            style={[styles.dropdown]}
                            data={servers}
                            labelField={"name"}
                            valueField={"host"}
                            placeholder={"Select Server"}
                            value={selectedServer}
                            onChange={(item: any) => {
                                console.log(item);
                                setSelectedServer(item);
                            }}
                        />
                    </View>
                    <View style={[styles.viewContainers]}>
                        <Text>Program Key</Text>
                        <TextInput
                            testID={'programKeyInput'}
                            style={[styles.textInput]}
                            onChangeText={(text: string) => {
                                setProgramKey(text);
                            }}
                        />
                        <Button
                            title={'SAVE'}
                            onPress={async () => {
                                const { serverId } = selectedServer;
                                await TriggerSdk.triggerDownload(serverId, programKey);

                                TriggerSdk.setCallback(serverId, programKey);
                            }}
                        />
                    </View>
                    <View style={[styles.viewContainers]}>
                        <Text>Event</Text>
                        <TextInput
                            testID={'eventInput'}
                            style={[styles.textInput]}
                            onChangeText={(text: string) => {
                                setEvent(text);
                            }}
                        />
                        <Button
                            title={'TRIGGER'}
                            onPress={() => {
                                TriggerSdk.notifyEventWithData(event, {});
                            }}
                        />
                    </View>
                </View>
            </ScrollView>
            <SurveyWebViewView
                onClose={() => {
                    setWebDialogHidden(true);
                }}
                hidden={webViewHidden}
                token={webViewConfig.token}
                url={webViewConfig.url}
            />
            <DialogView hidden={dialogHidden} onClose={onDialogCloseAction} config={config} />
        </View>
    );
};

export default HomeView;

const styles = StyleSheet.create({
    container: {
        flex: 1,
        alignItems: 'stretch',
        justifyContent: 'center',
        padding: 16
    },
    dropdown: {
        height: 50,
        borderColor: "gray",
        borderWidth: 0.5,
        borderRadius: 8,
        padding: 8,
        marginVertical: 16
    },
    textInput: {
        height: 40,
        borderColor: 'gray',
        borderWidth: 0.5,
        borderRadius: 8,
        padding: 8,
        marginVertical: 16
    },
    viewContainers: {
        marginTop: 16
    }
});
