import React from 'react';
import { Text, View } from 'react-native';
import { IQuestionProps } from './questionInfo';

const QuestionInfoView = (props: IQuestionProps) => {
    const styles = require('../utils/styles');

    return (
        <View key={props.info.id} style={styles.questionBottom}>
            <Text numberOfLines={0} style={{ fontSize: 16, color: '#000000' }}>
                {props.info.title}
            </Text>
        </View>
    );
};

export default QuestionInfoView;
