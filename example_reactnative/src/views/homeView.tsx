// @ts-ignore
import * as MobileSdk from "@forstaglobal/react-native-mobilesdk";
// @ts-ignore
import {TriggerManager} from "@forstaglobal/react-native-mobilesdk";
// @ts-ignore
import {IScenarioCallback, IWebSurveyModel,} from "@forstaglobal/react-native-mobilesdk";
import {Component} from "react";
import * as React from "react";
import {
    Button,
    ScrollView,
    StyleSheet,
    Text,
    TextInput,
    View,
} from "react-native";
import {Dropdown} from "react-native-element-dropdown";
import TriggerListener from "../triggerListener";
import MessageHub from "../utils/messageHub";

export default class HomeScreen extends Component {
    state = {
        servers: [],
        programKey: '',
        selectedServer: {
            serverId: '',
            host: ''
        },
        event: ''
    };

    constructor(props: any) {
        super(props);

        MobileSdk.enableLog(true);
        MobileSdk.initSdk();

        TriggerManager.setSdkListener(new TriggerListener());
    }

    async componentDidMount() {
        MessageHub.register(this, "webSurvey", this._onWebSurvey);
        MessageHub.register(this, "onScenarioLoad", this._onScenarioLoad);

        await MobileSdk.configureUk("<Client ID>", "<Client Secret>");

        const configuredServers = await MobileSdk.getServers();
        const servers = [];
        for (const server of configuredServers) {
            servers.push({
                label: server.name,
                value: server.host,
                serverId: server.serverId
            });
        }

        this.setState({
            servers,
        });
    }

    _onWebSurvey = (events: IWebSurveyModel) => {
        // @ts-ignore
        this.props.navigation.navigate("Survey", {
            token: events.token,
            url: events.url,
        });
    };

    _onScenarioLoad = (events: IScenarioCallback) => {
        // @ts-ignore
        this.setState({
            textValue: JSON.stringify(events),
        });
    };

    render() {
        return (
            <ScrollView>
                <View style={styles.container}>
                    <View style={[ styles.viewContainers ]}>
                        <Text>Host</Text>
                        <Dropdown
                            style={[styles.dropdown]}
                            data={this.state.servers}
                            labelField={"label"}
                            valueField={"value"}
                            placeholder={"Select Server"}
                            value={this.state.servers.length != 0 ? this.state.servers[0] : ''}
                            onChange={(item: any) => {
                                this.setState({
                                    selectedServer: item
                                });
                            }}
                        />
                    </View>
                    <View style={[ styles.viewContainers ]}>
                        <Text>Program Key</Text>
                        <TextInput
                            style={[styles.textInput]}
                            onChangeText={(text: string) => {
                                this.setState({
                                    programKey: text
                                })
                            }}/>
                        <Button
                            title={"Save"}
                            onPress={async () => {
                                const {programKey, selectedServer} = this.state;
                                const {serverId} = selectedServer;
                                MobileSdk.triggerDownload(serverId, programKey);

                                MobileSdk.setCallback(serverId, programKey);
                            }}
                        />
                    </View>
                    <View style={[ styles.viewContainers ]}>
                        <Text>Event</Text>
                        <TextInput
                            style={[styles.textInput]}
                            onChangeText={(text: string) => {
                                this.setState({
                                    event: text
                                })
                            }}/>
                        <Button
                            title={"Trigger"}
                            onPress={() => {
                                MobileSdk.notifyEventWithData(this.state.event, {});
                            }}
                        />
                    </View>
                </View>
            </ScrollView>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        alignItems: "stretch",
        justifyContent: "center",
        padding: 16,
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
        borderColor: "gray",
        borderWidth: 0.5,
        borderRadius: 8,
        padding: 8,
        marginVertical: 16
    },
    viewContainers: {
        marginTop: 16
    }
});
