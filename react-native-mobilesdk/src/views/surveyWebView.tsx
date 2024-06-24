import React, { useEffect } from 'react';
import { EmitterSubscription, NativeEventEmitter, NativeModules, Platform } from 'react-native';
import type { WebViewMessageEvent, WebViewProps } from 'react-native-webview';
import { WebView } from 'react-native-webview';
import { ConfirmitSdk } from '../confirmitSdk';

interface ISurveyWebViewProps extends WebViewProps {
    onSurveyClosed?: () => void;
}

const sdkEmitter = NativeModules.SdkEmitter;
const triggerManagerEmitter = new NativeEventEmitter(sdkEmitter);

const SurveyWebView = (props: ISurveyWebViewProps) => {
    let loaded = false;

    useEffect(() => {
        const pageReady: EmitterSubscription = triggerManagerEmitter.addListener('__mobileOnSurveyClosed', onSurveyClosed);

        return () => {
            pageReady.remove();
        };
    });

    const onSurveyClosed = () => {
        props.onSurveyClosed?.();
    };

    const onMessage = (event: WebViewMessageEvent) => {
        onSurveyClosed();
        props.onMessage?.(event);
    };

    const load = () => {
        if (!loaded) {
            ConfirmitSdk.injectWebView();
            loaded = true;
        }
    };

    let { injectedJavaScriptBeforeContentLoaded } = props;

    if (Platform.OS === 'ios') {
        injectedJavaScriptBeforeContentLoaded += " \nwindow['mobileBridge'] = { onSurveyEnd: function() { window.ReactNativeWebView?.postMessage(''); } };";
    }

    return (
        <WebView
            nativeID={'surveyWebViews'}
            {...props}
            domStorageEnabled={true}
            javaScriptEnabled={true}
            onLoad={() => {
                load();
            }}
            injectedJavaScriptForMainFrameOnly={false}
            injectedJavaScriptBeforeContentLoaded={injectedJavaScriptBeforeContentLoaded}
            onMessage={onMessage}
        />
    );
};

export default SurveyWebView;
