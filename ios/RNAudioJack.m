#import "RNAudioJack.h"
#import <AVFoundation/AVFoundation.h>

// Used to send events to JS
#if __has_include(<React/RCTBridge.h>)
#import <React/RCTBridge.h>
#import <React/RCTEventDispatcher.h>
#elif __has_include("RCTBridge.h")
#import "RCTBridge.h"
#import "RCTEventDispatcher.h"
#else
#import "React/RCTBridge.h"
#import "React/RCTEventDispatcher.h"
#endif

@implementation RNAudioJack

RCT_EXPORT_MODULE(@"AudioJack")

@synthesize bridge = _bridge;

static NSString * const AUDIO_CHANGED_NOTIFICATION = @"AUDIO_CHANGED_NOTIFICATION";
static NSString * const IS_PLUGGED_IN = @"isPluggedIn";

- (instancetype)init
{
    if (self = [super init]) {
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(audioRouteChangeListenerCallback:) name:AVAudioSessionRouteChangeNotification object:nil];
    }
    return self;
}

- (void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}


- (void)audioRouteChangeListenerCallback:(NSNotification*)notification
{
    [_bridge.eventDispatcher sendDeviceEventWithName:AUDIO_CHANGED_NOTIFICATION
      body:(@{
          IS_PLUGGED_IN: @([RNAudioJack isAudioJackInUse])
      })];
}

+ (BOOL)isAudioJackInUse
{
    AVAudioSessionRouteDescription* route = [[AVAudioSession sharedInstance] currentRoute];

    for (AVAudioSessionPortDescription* desc in [route outputs]) {
        if ([[desc portType] isEqualToString:AVAudioSessionPortHeadphones]) {
            return YES;
        }
    }

    return NO;
}

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

- (NSDictionary *)constantsToExport
{
    return @{ @"AUDIO_CHANGED_NOTIFICATION": AUDIO_CHANGED_NOTIFICATION };
}

RCT_EXPORT_METHOD(isPluggedIn:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
    resolve(@([RNAudioJack isAudioJackInUse]));
}

@end
