// @ts-ignore
import { SurveyWebView } from '@forstaglobal/react-native-mobilesdk';
import React from 'react';
import { Button, StyleSheet, View } from 'react-native';

interface ISurveyWebViewScreenProps {
    onClose: () => void;
    hidden: boolean;
    token: string;
    url: string;
}

const SurveyWebViewView = (props: ISurveyWebViewScreenProps) => {
    const { token, url } = props;

    const onSurveyClosed = () => {
        props.onClose();
    };

    const render = () => {
        if (props.hidden) {
            return <View />;
        }

        return (
            <View style={styles.container}>
                <View style={styles.topView}>
                    <Button title={'Close'} color={'#ff0000'} onPress={props.onClose} />
                </View>
                <View style={styles.divider} />
                <SurveyWebView
                    onSurveyClosed={onSurveyClosed}
                    source={{
                        uri: url,
                        headers: {
                            authorization: token
                        }
                    }}
                />
            </View>
        );
    };

    return render();
};

export default SurveyWebViewView;

const styles = StyleSheet.create({
    container: {
        position: 'absolute',
        top: 0,
        bottom: 0,
        left: 0,
        right: 0,
        flex: 1,
        flexDirection: 'column',
        justifyContent: 'space-between',
        alignItems: 'stretch',
        backgroundColor: '#fff'
    },
    topView: {
        padding: 16,
        justifyContent: 'center',
        alignItems: 'flex-end'
    },
    divider: {
        width: '100%',
        borderBottomWidth: 1,
        borderColor: 'lightgray',
        marginVertical: 4
    }
});
