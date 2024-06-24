import React, { useEffect, useState } from 'react';
import { Text, TextInput, View } from 'react-native';
import ErrorTextView from './components/errorTextView';
import { IQuestionProps } from './questionInfo';

const QuestionNumericView = (props: IQuestionProps) => {
    const styles = require('../utils/styles');

    const [text, setText] = useState('');
    const { pageControl } = props;

    useEffect(() => {
        async function fetchData() {
            const answer = await pageControl.getNumeric(props.info.id);
            setText(answer);
        }
        fetchData();
    }, [pageControl, props.info.id]);

    const onChangeText = async (text: string) => {
        setText(text);

        if (text.indexOf('.') !== -1) {
            await pageControl.setNumeric(props.info.id, parseFloat(text), true);
        } else {
            await pageControl.setNumeric(props.info.id, parseInt(text), false);
        }
    };

    return (
        <View key={props.info.id} style={styles.questionBottom}>
            <Text style={styles.questionTitle}>{props.info.title}</Text>
            <ErrorTextView errors={props.info.errors} />
            <TextInput
                testID={'test-oen'}
                value={text}
                keyboardType={'numeric'}
                onChangeText={onChangeText}
                style={{ padding: 10, borderRadius: 8, borderColor: '#000000', borderWidth: 1 }}
            />
        </View>
    );
};

export default QuestionNumericView;
