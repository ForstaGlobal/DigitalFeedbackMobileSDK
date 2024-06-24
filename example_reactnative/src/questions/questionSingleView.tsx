import React, { JSX, useEffect, useRef, useState } from 'react';
import { Text, View } from 'react-native';
import ErrorTextView from './components/errorTextView';
import RadioView from './components/radioView';
import StarView from './components/starView';
import { IQuestionProps } from './questionInfo';
import { ISingleQuestion, QuestionAnswer, SingleAppearance } from '../../../src/models/models';

const QuestionSingleView = (props: IQuestionProps) => {
    const styles = require('../utils/styles');
    const [selectedCode, setSelectedCode] = useState<string>('');

    const answers = useRef<QuestionAnswer[]>([]);
    const single = props.info as ISingleQuestion;
    const { pageControl } = props;

    let testIndex = 0;

    useEffect(() => {
        async function fetchData() {
            const answer = await pageControl.getSingle(props.info.id);
            setSelectedCode(answer.code);
        }
        fetchData();
    }, [pageControl, props.info.id]);

    useEffect(() => {
        let questionAnswers: QuestionAnswer[] = [];

        for (const answer of single.answers) {
            if (answer.isHeader) {
                questionAnswers = questionAnswers.concat(answer.answers);
                continue;
            }
            questionAnswers.push(answer);
        }
        answers.current = questionAnswers;
    });

    const onRadioPressCode = async (code: string) => {
        setSelectedCode(code);
        const selected = answers.current.find(x => x.code === code);
        if (selected == null) {
            return;
        }
        await pageControl.setSingle(props.info.id, selected.code);
    };

    const onStarPress = async (code: string) => {
        setSelectedCode(code);
        const selected = answers.current.find(x => x.code === code);
        if (selected == null) {
            return;
        }
        await pageControl.setSingle(props.info.id, selected.code);
    };

    const renderStars = () => {
        const elements: JSX.Element[] = [];
        const selectedIndex = single.answers.findIndex(x => x.code === selectedCode);

        for (let i = 0; i < single.answers.length; i++) {
            elements.push(<StarView key={i} code={single.answers[i]?.code || ''} fill={i <= selectedIndex} onStarPress={onStarPress} />);
        }
        return elements;
    };

    const renderQuestion = () => {
        switch (single.appearance) {
            case SingleAppearance.radioButton:
                return <View style={{ marginVertical: 8 }}>{renderRadioItems([], single.answers)}</View>;
            case SingleAppearance.startRating:
                return <View style={{ flexDirection: 'row', justifyContent: 'center', marginVertical: 8 }}>{renderStars()}</View>;
        }
        return <View style={{ marginVertical: 8 }}>{renderRadioItems([], single.answers)}</View>;
    };

    const renderRadioItems = (lastView: JSX.Element[], answers: QuestionAnswer[]) => {
        let lastAddedView: JSX.Element[] = lastView;
        // eslint-disable-next-line @typescript-eslint/prefer-for-of
        for (let i = 0; i < answers.length; i++) {
            if (answers[i]?.isHeader) {
                lastAddedView = createSubGroup(lastAddedView, answers[i]?.text || '', answers[i]?.answers || []);
                continue;
            }

            const code = answers[i]?.code || '';
            lastAddedView.push(
                <RadioView
                    testId={`single${testIndex}`}
                    key={code}
                    selected={answers[i]?.code === selectedCode}
                    onRadioPressCode={onRadioPressCode}
                    code={code}
                    text={answers[i]?.text || ''}
                    checkbox={false}
                />
            );
            testIndex += 1;
        }

        return lastAddedView;
    };

    const createSubGroup = (lastView: JSX.Element[], title: string, answers: QuestionAnswer[]) => {
        let lastAddedView = lastView;
        lastAddedView.push(<View key={`div-${lastAddedView.length}`} style={styles.questionDivider} />);

        // add group heading
        lastAddedView.push(
            <Text key={`title-${lastAddedView.length}`} style={styles.questionHeader}>
                {title}
            </Text>
        );

        lastAddedView = renderRadioItems(lastAddedView, answers);

        lastAddedView.push(<View key={`div-${lastAddedView.length}`} style={styles.questionDivider} />);

        return lastAddedView;
    };

    return (
        <View key={props.info.id} style={styles.questionBottom}>
            <Text style={styles.questionTitle}>{props.info.title}</Text>
            <ErrorTextView errors={single.errors} />
            {renderQuestion()}
        </View>
    );
};

export default QuestionSingleView;
