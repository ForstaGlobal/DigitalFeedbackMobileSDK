import React from 'react';
import { Text } from 'react-native';
import { ISurveyError } from '@forstaglobal/react-native-mobilesdk';

interface IErrorTextViewProps {
    errors: ISurveyError[];
}
const ErrorTextView = (props: IErrorTextViewProps) => {
    const styles = require('../../utils/styles');

    const renderError = () => {
        const elements: Element[] = [];
        for (let i = 0; i < props.errors.length; i++) {
            elements.push(
                <Text style={styles.questionError} key={i}>
                    {props.errors.map(x => x.message)[i]}
                </Text>
            );
        }
        return elements;
    };

    return <>{renderError()}</>;
};

export default ErrorTextView;
