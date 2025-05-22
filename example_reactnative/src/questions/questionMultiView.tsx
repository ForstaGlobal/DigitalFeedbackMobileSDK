import React, { JSX, useEffect, useRef, useState } from 'react';
import { Text, View } from 'react-native';
import { IMultiQuestion, QuestionAnswer } from '@forstaglobal/react-native-mobilesdk';
import ErrorTextView from './components/errorTextView';
import RadioView from './components/radioView';
import { IQuestionProps } from './questionInfo';

const QuestionMultiView = (props: IQuestionProps) => {
    const styles = require('../utils/styles');
    const [selectedCodes, setSelectedIndex] = useState<string[]>([]);

    const answers = useRef<QuestionAnswer[]>([]);
    const multi = props.info as IMultiQuestion;
    const { pageControl } = props;

    let testIndex = 0;

    useEffect(() => {
        async function fetchData() {
            const answer = await pageControl.getMulti(props.info.id);
            setSelectedIndex(answer.map(x => x.code));
        }
        fetchData();
    }, [pageControl, props.info.id]);

    useEffect(() => {
        let questionAnswers: QuestionAnswer[] = [];
        for (const answer of multi.answers) {
            if (answer.isHeader) {
                questionAnswers = questionAnswers.concat(answer.answers);
                continue;
            }
            questionAnswers.push(answer);
        }
        answers.current = questionAnswers;
    });

    const onRadioPressCode = async (code: string) => {
        const questionAnswer = answers.current.find(x => x.code === code);
        if (questionAnswer == null) {
            return;
        }

        const selected = selectedCodes;
        if (selected.includes(code)) {
            const toRemoveIndex = selected.indexOf(code);
            if (toRemoveIndex > -1) {
                selected.splice(toRemoveIndex, 1);
                await pageControl.setMulti(props.info.id, questionAnswer.code, false);
            }
        } else {
            selected.push(code);
            await pageControl.setMulti(props.info.id, questionAnswer.code, true);
        }
        setSelectedIndex([...selected]);
    };

    const renderQuestion = () => {
        return <View style={{ marginTop: 8 }}>{renderRadioItems([], multi.answers)}</View>;
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
                    testId={`multi${testIndex}`}
                    key={code}
                    selected={selectedCodes.includes(code)}
                    onRadioPressCode={onRadioPressCode}
                    code={code}
                    text={answers[i]?.text || ''}
                    checkbox={true}
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
            <ErrorTextView errors={props.info.errors} />
            {renderQuestion()}
        </View>
    );
};

export default QuestionMultiView;
