require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name         = 'RNAudioJack'
  s.version      = package['version']
  s.summary      = package['description']
  s.license      = package['license']
  s.authors      = package['author']  
  s.platform     = :ios, "13.0"
  s.ios.deployment_target = "13.0"
  s.swift_version = "4.2"
  s.homepage     = "https://github.com/robinpowered/react-native-audio-jack"
  s.source       = { :git => "https://github.com/robinpowered/react-native-audio-jack.git", :tag => "v#{s.version}" }
  s.source_files  = "ios/**/*.{h,m}"
  s.dependency "React"
end
