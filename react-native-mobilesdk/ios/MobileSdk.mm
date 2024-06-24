#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(MobileSdk, NSObject)

RCT_EXTERN_METHOD(injectWebView)

// Confirmit
RCT_EXTERN_METHOD(initSdk:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(enableLog:(BOOL)eanble)

// program
RCT_EXTERN_METHOD(triggerDownload:(NSString)serverId
                  withProgramKey:(NSString)programKey
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(deleteProgram:(NSString)serverId
                  withProgramKey:(NSString)programKey
                  withDeleteCustomData:(BOOL)deleteCustomData
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(deleteAll:(BOOL)deleteCustomData
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(setCallback:(NSString)serverId
                  withProgramKey:(NSString)programKey)

RCT_EXTERN_METHOD(removeCallback:(NSString)serverId
                  withProgramKey:(NSString)programKey)

RCT_EXTERN_METHOD(notifyEvent:(NSString)event)

RCT_EXTERN_METHOD(notifyAppForeground:(NSDictionary)data)

RCT_EXTERN_METHOD(notifyEventWithData:(NSString)event
                  withData:(NSDictionary)data)

RCT_EXTERN_METHOD(addJourney:(NSDictionary)data)

RCT_EXTERN_METHOD(addJourneyLogWithServer:(NSString)serverId
                  withProgramKey:(NSString)programKey
                  withData:(NSDictionary)data)

// servers
RCT_EXTERN_METHOD(getUs:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getUk:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getAustralia:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getCanada:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getGermany:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getHxPlatform:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getHxAustralia:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(configureUs:(NSString)clientId
                  withClientSecret:(NSString)clientSecret
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(configureUk:(NSString)clientId
                  withClientSecret:(NSString)clientSecret
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(configureAustralia:(NSString)clientId
                  withClientSecret:(NSString)clientSecret
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(configureCanada:(NSString)clientId
                  withClientSecret:(NSString)clientSecret
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(configureGermany:(NSString)clientId
                  withClientSecret:(NSString)clientSecret
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(configureHxPlatform:(NSString)clientId
                  withClientSecret:(NSString)clientSecret
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(configureHxAustralia:(NSString)clientId
                  withClientSecret:(NSString)clientSecret
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(configureServer:(NSString)name
                  withHost:(NSString)host
                  withClientId:(NSString)clientId
                  withClientSecret:(NSString)clientSecret
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getServer:(NSString)serverId
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getServers:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)


+ (BOOL)requiresMainQueueSetup
{
  return NO;
}

@end
