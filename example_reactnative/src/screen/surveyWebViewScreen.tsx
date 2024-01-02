// @ts-ignore
import { SurveyWebView } from '@forstaglobal/react-native-mobilesdk';
import React, { Component } from 'react';

export default class SurveyWebViewScreen extends Component {
    private onSurveyClosed = () => {
        // @ts-ignore
        this.props.navigation.navigate('Home', {});
    };

    public render() {
        // @ts-ignore
        const { token, url } = this.props.route.params;
        return (
            <SurveyWebView
                style={{
                    flexGrow: 1
                }}
                onSurveyClosed={this.onSurveyClosed}
                source={{
                    uri: url,
                    headers: {
                        authorization: token
                    }
                }}
            />
        );
    }
}
