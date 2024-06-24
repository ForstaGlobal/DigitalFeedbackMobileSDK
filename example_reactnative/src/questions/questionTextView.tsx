import React, { useEffect, useState } from 'react';
import { Text, TextInput, View } from 'react-native';
import ErrorTextView from './components/errorTextView';
import { IQuestionProps } from './questionInfo';

const QuestionTextView = (props: IQuestionProps) => {
    const styles = require('../utils/styles');

    const [text, setText] = useState('');
    const { pageControl } = props;

    useEffect(() => {
        async function fetchData() {
            const answer = await pageControl.getText(props.info.id);
            setText(answer);
        }
        fetchData();
    }, [pageControl, props.info.id]);

    const onChangeText = async (text: string) => {
        setText(text);
        await pageControl.setText(props.info.id, text);
    };

    return (
        <View key={props.info.id} style={styles.questionBottom}>
            <Text style={styles.questionTitle}>{props.info.title}</Text>
            <ErrorTextView errors={props.info.errors} />
            <TextInput
                testID={'test-oet'}
                value={text}
                multiline={true}
                numberOfLines={4}
                textAlignVertical={'top'}
                style={{ padding: 10, borderRadius: 8, borderColor: '#000000', borderWidth: 1 }}
                onChangeText={onChangeText}
            />
        </View>
    );
};

export default QuestionTextView;
