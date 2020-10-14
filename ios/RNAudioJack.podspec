Pod::Spec.new do |s|
  s.name         = "RNAudioJack"
  s.version      = "1.1.1"
  s.summary      = "RNAudioJack"
  s.description  = <<-DESC
                  RNAudioJack
                   DESC
  s.homepage     = "https://github.com/robinpowered/react-native-audio-jack"
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "author@domain.cn" }
  s.platform     = :ios, "7.0"
  s.source       = { :git => "https://github.com/author/RNAudioJack.git", :tag => "master" }
  s.source_files  = "RNAudioJack.{h,m}"
  s.requires_arc = true


  s.dependency "React"
  #s.dependency "others"

end
