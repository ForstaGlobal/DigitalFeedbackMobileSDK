#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(MobileSdkSurvey, NSObject)

// survey page
RCT_EXTERN_METHOD(getTitle:(NSString)serverId
                  withProgramKey:(NSString)programKey
                  withSurveyId:(NSString)surveyId
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getText:(NSString)serverId
                  withProgramKey:(NSString)programKey
                  withSurveyId:(NSString)surveyId
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getQuestions:(NSString)serverId
                  withProgramKey:(NSString)programKey
                  withSurveyId:(NSString)surveyId
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(setText:(NSString)serverId
                  withProgramKey:(NSString)programKey
                  withSurveyId:(NSString)surveyId
                  withQuestionId:(NSString)questionId
                  withAnswer:(NSString)answer)

RCT_EXTERN_METHOD(getText:(NSString)serverId
                  withProgramKey:(NSString)programKey
                  withSurveyId:(NSString)surveyId
                  withQuestionId:(NSString)questionId
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(setNumeric:(NSString)serverId
                  withProgramKey:(NSString)programKey
                  withSurveyId:(NSString)surveyId
                  withQuestionId:(NSString)questionId
                  withAnswer:(double)answer
                  withIsDouble:(BOOL)isDouble)

RCT_EXTERN_METHOD(getNumeric:(NSString)serverId
                  withProgramKey:(NSString)programKey
                  withSurveyId:(NSString)surveyId
                  withQuestionId:(NSString)questionId
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(setSingle:(NSString)serverId
                  withProgramKey:(NSString)programKey
                  withSurveyId:(NSString)surveyId
                  withQuestionId:(NSString)questionId
                  withCode:(NSString)code)

RCT_EXTERN_METHOD(getSingle:(NSString)serverId
                  withProgramKey:(NSString)programKey
                  withSurveyId:(NSString)surveyId
                  withQuestionId:(NSString)questionId
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(setMulti:(NSString)serverId
                  withProgramKey:(NSString)programKey
                  withSurveyId:(NSString)surveyId
                  withQuestionId:(NSString)questionId
                  withCode:(NSString)answer
                  withSelected:(BOOL)selected)

RCT_EXTERN_METHOD(getMulti:(NSString)serverId
                  withProgramKey:(NSString)programKey
                  withSurveyId:(NSString)surveyId
                  withQuestionId:(NSString)questionId
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

// Survey Control
RCT_EXTERN_METHOD(startSurvey:(NSString)serverId
                  withProgramKey:(NSString)programKey
                  withSurveyId:(NSString)surveyId
                  withData:(NSDictionary)data
                  withRespondentValues:(NSDictionary)respondentValues
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(next:(NSString)serverId
                  withProgramKey:(NSString)programKey
                  withSurveyId:(NSString)surveyId
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(back:(NSString)serverId
                  withProgramKey:(NSString)programKey
                  withSurveyId:(NSString)surveyId
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(quit:(NSString)serverId
                  withProgramKey:(NSString)programKey
                  withSurveyId:(NSString)surveyId
                  withUpload:(NSString)upload
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

+ (BOOL)requiresMainQueueSetup
{
  return NO;
}

@end
