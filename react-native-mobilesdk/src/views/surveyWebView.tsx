import React, { Component } from 'react';
import { NativeEventEmitter, NativeModules, Platform } from 'react-native';
import type { WebViewMessageEvent, WebViewProps } from 'react-native-webview';
import { WebView } from 'react-native-webview';
import { injectWebView } from '../index';

interface SurveyWebViewProps extends WebViewProps {
    onSurveyClosed?: () => void;
}

export default class SurveyWebView extends Component<SurveyWebViewProps> {
    private sdkEmitter = NativeModules.SdkEmitter;
    private triggerManagerEmitter = new NativeEventEmitter(this.sdkEmitter);

    private loaded = false;

    public constructor(props: SurveyWebViewProps) {
        super(props);

        this.triggerManagerEmitter.addListener('__mobileOnSurveyClosed', this.__onSurveyClosed);
    }

    private __onSurveyClosed = () => {
        this.props.onSurveyClosed?.();
    };

    private load() {
        if (!this.loaded) {
            injectWebView();
            this.loaded = true;
        }
    }

    private onMessage = (event: WebViewMessageEvent) => {
        this.__onSurveyClosed();
        this.props.onMessage?.(event);
    };

    public render() {
        let { injectedJavaScriptBeforeContentLoaded } = this.props;

        if (Platform.OS === 'ios') {
            injectedJavaScriptBeforeContentLoaded += ` \nwindow['mobileBridge'] = { onSurveyEnd: function() { window.ReactNativeWebView?.postMessage(''); } };`;
        }

        return (
            <WebView
                nativeID={'surveyWebViews'}
                {...this.props}
                domStorageEnabled={true}
                javaScriptEnabled={true}
                onLoad={() => {
                    this.load();
                }}
                injectedJavaScriptForMainFrameOnly={false}
                injectedJavaScriptBeforeContentLoaded={injectedJavaScriptBeforeContentLoaded}
                onMessage={this.onMessage}
            />
        );
    }
}
